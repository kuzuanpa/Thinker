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
import cn.kuzuanpa.thinker.util.GLUT;
import cpw.mods.fml.client.config.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.text.NumberFormat;

public class NumberConfigButton extends CommonGuiButton {
    public NumberConfigButton(int id, int xPos, int yPos, int width, int height,String displayText, configHandler.configNumber config) {
        super(id, xPos, yPos,width,height,displayText);
        this.config=config;
        this.originalY=yPos;
    }
    public configHandler.configNumber config;
    public int originalY;
    public void drawButton(Minecraft p_146112_1_, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            GL11.glPushMatrix();
            animeList.forEach(anime -> anime.animeDrawPre(initTime));
            FontRenderer fontrenderer = p_146112_1_.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean isMouseHovering=this.updateHoverState(mouseX,mouseY);
            int k = this.getHoverState(isMouseHovering);
            animeList.forEach(anime -> anime.animeDraw(initTime));
            float progress=config.get()/(config.max()-config.min());
            drawRect(xPosition+2,yPosition+height/2,xPosition+width-2,yPosition+(height/2)+1,-2132680325);
            drawRect((int) (xPosition+2+(width-4)*progress)-1,yPosition+10, (int) (xPosition+2+(width-4)*progress)+1,yPosition+height-10,-1);
            if(k==2&& Mouse.isButtonDown(0)) config.update(mouseX<xPosition+2?config.min():mouseX>xPosition+width-2?config.max():(config.max()-config.min())*(mouseX-2-xPosition)/(width-4));
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(2);
            this.drawString(fontrenderer, this.displayString, this.xPosition, this.yPosition, -1);
            this.drawString(fontrenderer, nf.format(config.min()), this.xPosition, this.yPosition+height-10, -1);
            this.drawCenteredString(fontrenderer, nf.format(config.get()), this.xPosition+width/2, this.yPosition+height-10, -1);
            this.drawCenteredString(fontrenderer, nf.format(config.max()), this.xPosition+width, this.yPosition+height-10, -1);

            animeList.forEach(anime -> anime.animeDrawAfter(initTime));
            GL11.glPopMatrix();
        }
    }
}
