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

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

public class ThinkingBackground extends CommonGuiButton{
    ResourceLocation textures=new ResourceLocation(MOD_ID,"textures/gui/think/background.png");

    public ThinkingBackground(int id,int width,int height){
        super(id, 0, 0,width,height,"");

    }
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
        if (this.visible)
        {
            ResourceLocation buttontextures=new ResourceLocation(MOD_ID,"textures/gui/think/background.png");
            p_146112_1_.getTextureManager().bindTexture(buttontextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
            this.drawTexturedModalRect(0,0, 0, 0, width, height);
        }
    }
}
