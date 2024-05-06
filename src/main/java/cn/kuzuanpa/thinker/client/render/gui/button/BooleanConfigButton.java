/*
 * This class was created by <kuzuanpa>. It is distributed as
 * part of the Thinker Mod. Get the Source Code in github:
 * https://github.com/kuzuanpa/Thinker
 *
 * Thinker is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 */

package cn.kuzuanpa.thinker.client.render.gui.button;

import cn.kuzuanpa.thinker.client.configHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class BooleanConfigButton extends ThinkerButtonBase {
    public BooleanConfigButton(int id, int xPos, int yPos, int width, int height, String displayText, configHandler.configBoolean config) {
        super(id, xPos, yPos,width,height,displayText);
        this.config=config;
        this.originalY=yPos;
    }
    public configHandler.configBoolean config;
    public boolean mouseHolding=false;
    public int originalY;
    public void drawButton(Minecraft p_146112_1_, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            GL11.glPushMatrix();
            GuiAnimeList.forEach(anime -> anime.animeDrawPre(timer));
            FontRenderer fontrenderer = p_146112_1_.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean isMouseHovering=this.updateHoverState(mouseX,mouseY);
            int k = this.getHoverState(isMouseHovering);
            GuiAnimeList.forEach(anime -> anime.animeDraw(timer));
            drawRect(xPosition+2,yPosition+height/2,xPosition+width-2,yPosition+(height/2)+1,config.get()?0xff99ffcc:0x99ff99cc);
            if(k==2&& Mouse.isButtonDown(0)&&!mouseHolding){
                config.set(!config.get());
                mouseHolding=true;
            }
            if(!Mouse.isButtonDown(0))mouseHolding=false;
            this.drawString(fontrenderer, this.displayString, this.xPosition, this.yPosition, -1);
            GuiAnimeList.forEach(anime -> anime.animeDrawAfter(timer));
            GL11.glPopMatrix();
        }
    }
}
