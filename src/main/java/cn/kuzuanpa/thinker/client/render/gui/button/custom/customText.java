package cn.kuzuanpa.thinker.client.render.gui.button.custom;

import cn.kuzuanpa.thinker.client.render.gui.button.ThinkerButton;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class customText extends ThinkerButton {
    int color;
    String text;
    public customText(int id, String text, int posX, int posY){
        super(id,posX,posY,text.length(),12,"");
        this.text=text;
        this.color=-1;
        this.initTime=System.currentTimeMillis();
    }
    public customText(int id, String text, int posX, int posY, int color){
        super(id,posX,posY,text.length()*5,12,"");
        this.text=text;
        this.color=color;
        this.initTime=System.currentTimeMillis();
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY){
        if (this.visible) {
            GL11.glPushMatrix();
            animeList.forEach(anime -> anime.animeDrawPre(initTime));
            GL11.glTranslatef(xPosition + (height / 2F), yPosition + (width / 2F),0);
            animeList.forEach(anime -> anime.animeDraw(initTime));
            GL11.glTranslatef(-(xPosition + (height / 2F)), -(yPosition + (width / 2F)),0);
            this.drawString(Minecraft.getMinecraft().fontRenderer, text, xPosition, yPosition, color);
            animeList.forEach(anime -> anime.animeDrawAfter(initTime));
            GL11.glPopMatrix();
        }
    }

}
