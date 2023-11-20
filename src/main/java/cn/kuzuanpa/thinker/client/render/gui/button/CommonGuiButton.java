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

import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

public class CommonGuiButton extends GuiButton {
    ResourceLocation baseTexture=new ResourceLocation(MOD_ID,"textures/gui/think/base.png");
    public long initTime=0;
    public int animeXModify=0,animeYModify=0,animeWidthModify=0,animeHeightModify=0;
    public CommonGuiButton(int id, int xPos, int yPos, int width, int height, String displayText) {
        super(id, xPos, yPos,width,height,displayText);
    }
    public void drawButton(Minecraft p_146112_1_, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            GL11.glPushMatrix();
            animeList.forEach(anime -> anime.animeDrawPre(initTime));
            FontRenderer fontrenderer = p_146112_1_.fontRenderer;
            p_146112_1_.getTextureManager().bindTexture(buttonTextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean isMouseHovering=this.updateHoverState(mouseX,mouseY);
            int k = this.getHoverState(isMouseHovering);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            int l = 14737632;
            if (packedFGColour != 0)l = packedFGColour;
            else if (!this.enabled)l = 10526880;
            else if (isMouseHovering)l = 16777120;
            animeList.forEach(anime -> anime.animeDraw(initTime));
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
            this.mouseDragged(p_146112_1_, mouseX, mouseY);
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
            animeList.forEach(anime -> anime.animeDrawAfter(initTime));
            GL11.glPopMatrix();
        }
    }
    public ArrayList<IGuiAnime> animeList=new ArrayList<>();
    public CommonGuiButton addAnime(IGuiAnime anime){
        animeList.add(anime);
        return this;
    }
    public CommonGuiButton addToList(List<CommonGuiButton> list){
        list.add(this);
        return this;
    }

    public boolean updateHoverState(int mouseX, int mouseY)
    {
        animeXModify=animeYModify=animeHeightModify=animeWidthModify=0;
        animeList.forEach(anime -> anime.updateButton(initTime,this));
        return this.enabled && this.visible && mouseX >= this.xPosition+animeXModify && mouseY >= this.yPosition+animeYModify && mouseX < this.xPosition+animeXModify + this.width+animeWidthModify && mouseY < this.yPosition+animeYModify + this.height+animeHeightModify;
    }
    public void doAnime(long initTime){this.initTime=initTime;}
}
