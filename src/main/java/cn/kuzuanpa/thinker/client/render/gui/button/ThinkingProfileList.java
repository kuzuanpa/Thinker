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
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
        if (this.visible)
        {
            GL11.glPushMatrix();
            ResourceLocation buttontextures=new ResourceLocation(MOD_ID,"textures/gui/think/base.png");
            for (int i=0;i<thinkingProfileHandler.profileList.size();i++){
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.65F);
                p_146112_1_.getTextureManager().bindTexture(buttontextures);
                this.drawTexturedModalRect(0,thinkingProfileHandler.YOffset+i*(16+thinkingProfileHandler.PROFILE_GAP), 64, 0, 64, 16);
                if(thinkingProfileHandler.profileList.get(i).icon!=null){
                    thinkingProfileHandler.thinkingProfile profile = thinkingProfileHandler.profileList.get(i);
                    GL11.glColor4f(profile.iconR,profile.iconG,profile.iconB,profile.iconA);
                    p_146112_1_.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
                    this.drawTexturedModelRectFromIcon(0,thinkingProfileHandler.YOffset+i*(16+thinkingProfileHandler.PROFILE_GAP),profile.icon, 16, 16);
            }}
            GL11.glPopMatrix();
        }
    }
}
