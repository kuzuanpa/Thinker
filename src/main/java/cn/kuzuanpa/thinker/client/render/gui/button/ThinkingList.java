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

public class ThinkingList extends CommonGuiButton{

    public ThinkingList(int id, int x,int y){
        super(id, x, y,64,16,"sad");

    }
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
        if (this.visible)
        {
            ResourceLocation buttontextures=new ResourceLocation(MOD_ID,"textures/gui/think/base.png");
            p_146112_1_.getTextureManager().bindTexture(buttontextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.65F);
            this.drawTexturedModalRect(xPosition,yPosition, 64, 0, 64, 16);
        }
    }
}
