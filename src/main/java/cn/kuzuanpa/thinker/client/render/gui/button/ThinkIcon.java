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

import cn.kuzuanpa.thinker.util.GLUT;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

public class ThinkIcon extends CommonGuiButton{

    public ThinkIcon(int id,int xPos,int yPos, int width, int height){
        super(id, xPos, yPos,width,height,"");
    }
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
        if (this.visible)
        {
            String time = Long.toString(Minecraft.getSystemTime());
            if (time.length()<5) time = Long.toString(Minecraft.getSystemTime()+10000);
            GL11.glPushMatrix();
            p_146112_1_.getTextureManager().bindTexture(textures);
            GLUT.glRotateCenterd(Integer.parseInt(time.substring(time.length()-4))/10000F*360,0,0,1,xPosition,yPosition,0,width,height,0);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(xPosition,yPosition, 0, 0, this.width, this.height);
            GL11.glPopMatrix();
        }
    }
}
