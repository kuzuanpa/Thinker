/*
 * This class was created by <kuzuanpa>. It is a part of Thinker.
 * Get the Source Code in github:
 * https://github.com/kuzuanpa/Thinker
 *
 * Thinker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * Thinker is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 */

/*
 * This class was created by <kuzuanpa>. It is a part of Thinker.
 * Get the Source Code in github:
 * https://github.com/kuzuanpa/Thinker
 *
 * Thinker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * Thinker is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 */
package cn.kuzuanpa.thinker.client.objects.gui;

import cn.kuzuanpa.thinker.client.handler.configHandler;
import cn.kuzuanpa.thinker.client.handler.profileHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.util.Color;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;
import static cn.kuzuanpa.thinker.client.handler.profileHandler.currentProfileLayer;
import static cn.kuzuanpa.thinker.client.handler.profileHandler.profileListHeight;

public class ThinkingProfileList extends ThinkerButtonBase {

    public ThinkingProfileList(int id, int x, int y,int height){
        super(id, x, y,66+currentProfileLayer*8,height,"");

    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        updateHoverState(mouseX,mouseY);
        if (!this.visible)return;
        GL11.glPushMatrix();

        GuiAnimeList.forEach(anime->anime.animeDrawPre(timer));
        GuiAnimeList.forEach(anime->anime.animeDraw(timer));

        AtomicInteger i= new AtomicInteger();
        walkMaps(0,profileHandler.rootDir,mc,i,"");

        this.zLevel-=10;
        this.drawGradientRect(xPosition, yPosition, width, height, 0x22000000, 0x22000000);
        this.zLevel+=10;

        GL11.glEnable(GL11.GL_BLEND);

        GuiAnimeList.forEach(anime->anime.animeDrawAfter(timer));
        GL11.glPopMatrix();
    }
    public void walkMaps(int depth, Map<String,Object> map,Minecraft mc, AtomicInteger i,String path){
        map.forEach((key,value)->{
            if(!path.equals("")&&!profileHandler.unfoldedDirs.contains(path))return;
            if(value instanceof profileHandler.thinkingProfile) {
                profileListHeight=Math.max(profileListHeight,i.get());
                drawProfileAt(mc,depth*8, (int) (profileHandler.YOffset+i.get()),(profileHandler.thinkingProfile)value,depth);
                i.getAndAdd(16+configHandler.themeSelectorProfileGap.getI());
            }
            if(value instanceof Map){
                drawDirAt(mc,depth*8, (int) (profileHandler.YOffset+i.get()),key,depth);
                profileListHeight=Math.max(profileListHeight,i.get());
                i.getAndAdd(16+configHandler.themeSelectorProfileGap.getI()/2);
                walkMaps(depth+1,(Map) value,mc,i,path+"/"+key);
            }
        });
    }
    private void drawDirAt(Minecraft mc, int x, int y, String id,int depth){
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.65F);
        ResourceLocation buttontextures=new ResourceLocation(MOD_ID,"textures/base.png");
        mc.getTextureManager().bindTexture(buttontextures);
        //draw folders tag
        for (int j=0;j<depth;j++)this.drawTexturedModalRect(2+j*8, y+6, 32, 32, 4, 4);
        this.drawTexturedModalRect(x, y, 0, 48, 64, 16);
        this.drawCenteredString(mc.fontRenderer,id,x+40, y+4, 0xffffff);
        GL11.glPopMatrix();
    }
    private void drawProfileAt(Minecraft mc, int x, int y, profileHandler.thinkingProfile profile,int depth){
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.65F);
        ResourceLocation buttontextures=new ResourceLocation(MOD_ID,"textures/base.png");
        mc.getTextureManager().bindTexture(buttontextures);
        this.drawTexturedModalRect(x, y, 64, 0, 64, 16);
        //draw folders tag
        for (int j=0;j<depth;j++)this.drawTexturedModalRect(2+j*8, y+6, 32, 32, 4, 4);
        //Draw select tag
        if(profileHandler.selectedProfile!=null&&profileHandler.selectedProfile.id.equals(profile.id))this.drawTexturedModalRect(x, y, 0, 64, 64, 16);
        this.drawCenteredString(mc.fontRenderer,profile.id,x+40, y+4, 0xffffff);

        if(profile.icon==null){GL11.glPopMatrix();return;}
        //draw icon
        GL11.glColor4f(profile.iconR,profile.iconG,profile.iconB,profile.iconA);
        mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
        this.drawTexturedModelRectFromIcon(x, y,profile.icon, 16, 16);
        GL11.glPopMatrix();
    }
    public String onMouseClick(int y,thinkerImage buttonFolder){
        AtomicInteger i = new AtomicInteger();
        String result = searchButtons(0,profileHandler.rootDir,Minecraft.getMinecraft(),i,y,"");
        if(result.startsWith("Profile:"))return result.replaceFirst("Profile:","");
        if(result.startsWith("Dir:")) triggerDir("/"+result.replaceFirst("Dir:",""),buttonFolder);
        return "";
    }
    public void triggerDir(String path,thinkerImage buttonFolder){
        profileListHeight=0;
        if(profileHandler.unfoldedDirs.contains(path)) profileHandler.unfoldedDirs.removeIf(p->p.startsWith(path));
        else profileHandler.unfoldedDirs.add(path);
        int oldProfileLayer = currentProfileLayer;
        currentProfileLayer=0;
        profileHandler.unfoldedDirs.forEach(dir->currentProfileLayer=Math.max(currentProfileLayer,dir.split("/").length - 1));
        this.width+=(currentProfileLayer-oldProfileLayer)*8;
        buttonFolder.xPosition += (currentProfileLayer-oldProfileLayer)*8;
    }
    public String searchButtons(int depth, Map<String,Object> map,Minecraft mc, AtomicInteger i,int mouseY,String path){
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if(!path.equals("")&&!profileHandler.unfoldedDirs.contains(path))return "";
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof profileHandler.thinkingProfile) {
                if (isXYinButton((int) (profileHandler.YOffset + i.get()), mouseY)) return "Profile:"+key;
                i.getAndAdd(16 + configHandler.themeSelectorProfileGap.getI());
            }
            if (value instanceof Map) {
                if (isXYinButton((int) (profileHandler.YOffset + i.get()), mouseY)) return "Dir:"+key;
                i.getAndAdd(16 + configHandler.themeSelectorProfileGap.getI() / 2);
                String str = searchButtons(depth + 1, (Map) value, mc, i, mouseY,path+"/"+key);
                if(str.startsWith("Dir:"))return "Dir:"+key+"/"+str.replaceFirst("Dir:","");
                if(!str.equals(""))return str;
            }
        }
        return "";
    }
    public boolean isXYinButton(int buttonY,int mouseY){
        return mouseY>=buttonY&&mouseY<=buttonY+16;
    }
}
