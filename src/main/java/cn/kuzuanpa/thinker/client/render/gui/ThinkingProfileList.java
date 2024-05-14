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
package cn.kuzuanpa.thinker.client.render.gui;

import cn.kuzuanpa.thinker.client.handler.configHandler;
import cn.kuzuanpa.thinker.client.handler.profileHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

public class ThinkingProfileList extends ThinkerButtonBase {

    public ThinkingProfileList(int id, int x, int y,int height){
        super(id, x, y,64,height,"");

    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        updateHoverState(mouseX,mouseY);
        if (!this.visible)return;
        GL11.glPushMatrix();
        GuiAnimeList.forEach(anime->anime.animeDrawPre(timer));
        ResourceLocation buttontextures=new ResourceLocation(MOD_ID,"textures/gui/think/base.png");
        for (int i : profileHandler.displayProfileIDMap.keySet()){
            if(profileHandler.getProfile(profileHandler.displayProfileIDMap.get(i))==null)return;
            profileHandler.thinkingProfile profile = profileHandler.getProfile(profileHandler.displayProfileIDMap.get(i));
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.65F);
            mc.getTextureManager().bindTexture(buttontextures);
            GuiAnimeList.forEach(anime->anime.animeDraw(timer));
            this.drawTexturedModalRect(0, (int) (Math.floor(profileHandler.YOffset)+i*(16+configHandler.themeSelectorProfileGap.get())), 64, 0, 64, 16);
            //Draw select tag
            if(profileHandler.selectedProfile!=null&&profileHandler.selectedProfile.id.equals(profile.id))this.drawTexturedModalRect(0, (int) (Math.floor(profileHandler.YOffset)+i*(16+configHandler.themeSelectorProfileGap.get())), 0, 64, 64, 16);
            this.drawCenteredString(mc.fontRenderer,profile.id,40, (int) (Math.floor(profileHandler.YOffset)+i*(16+configHandler.themeSelectorProfileGap.get()))+4, 0xffffff);
            //if icon==null, return
            if(profile.icon==null){GL11.glPopMatrix();continue;}
            //draw icon
            GL11.glColor4f(profile.iconR,profile.iconG,profile.iconB,profile.iconA);
            mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
            this.drawTexturedModelRectFromIcon(0, (int) (Math.floor(profileHandler.YOffset)+i*(16+configHandler.themeSelectorProfileGap.get())),profile.icon, 16, 16);
            GL11.glPopMatrix();
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiAnimeList.forEach(anime->anime.animeDrawAfter(timer));
        GL11.glPopMatrix();
    }
}
