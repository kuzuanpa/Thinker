/*
 * This class was created by <kuzuanpa>. It is distributed as
 * part of the kTFRUAddon Mod. Get the Source Code in github:
 * https://github.com/kuzuanpa/kTFRUAddon
 *
 * kTFRUAddon is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 */

package cn.kuzuanpa.thinker.client.render.gui.button;

import cn.kuzuanpa.thinker.client.configHandler;
import cn.kuzuanpa.thinker.client.thinkingProfileHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

public class ThinkingProfileList extends CommonGuiButton{

    public ThinkingProfileList(int id, int x, int y,int height){
        super(id, x, y,64,height,"");

    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        updateHoverState(mouseX,mouseY);
        if (!this.visible)return;
        GL11.glPushMatrix();
        animeList.forEach(anime->anime.animeDrawPre(initTime));
        ResourceLocation buttontextures=new ResourceLocation(MOD_ID,"textures/gui/think/base.png");
        for (int i=0;i<thinkingProfileHandler.profileList.size();i++){
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.65F);
            mc.getTextureManager().bindTexture(buttontextures);
            animeList.forEach(anime->anime.animeDraw(initTime));
            this.drawTexturedModalRect(0, (int) (thinkingProfileHandler.YOffset+i*(16+configHandler.themeSelectorProfileGap.get())), 64, 0, 64, 16);
            if(thinkingProfileHandler.profileList.get(i).icon==null){GL11.glPopMatrix();continue;}
            thinkingProfileHandler.thinkingProfile profile = thinkingProfileHandler.profileList.get(i);
            GL11.glColor4f(profile.iconR,profile.iconG,profile.iconB,profile.iconA);
            mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
            this.drawTexturedModelRectFromIcon(0, (int) (thinkingProfileHandler.YOffset+i*(16+configHandler.themeSelectorProfileGap.get())),profile.icon, 16, 16);
            GL11.glPopMatrix();
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        animeList.forEach(anime->anime.animeDrawAfter(initTime));
        GL11.glPopMatrix();
    }
}
