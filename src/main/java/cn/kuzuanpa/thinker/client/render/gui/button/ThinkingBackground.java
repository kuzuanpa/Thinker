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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;


import java.awt.*;

public class ThinkingBackground extends ThinkerButton {

    public ThinkingBackground(int id,int width,int height){
        super(id, 0, 0,width,height,"");
        this.zLevel=-255;
    }
    public void drawButton(Minecraft p_146112_1_, int mouseX, int mouseY) {
        if (this.visible)
        {
            GL11.glPushMatrix();
            drawRect(0,0,width,height,new Color(configHandler.HUDBackgroundColorR.getI(),configHandler.HUDBackgroundColorG.getI(),configHandler.HUDBackgroundColorB.getI(),configHandler.HUDBackgroundColorA.getI()).getRGB());
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1,1,1,1);
            GL11.glPopMatrix();
        }
    }
}
