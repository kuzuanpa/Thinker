package cn.kuzuanpa.thinker.client.render.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

public class thinkerImage extends ThinkerButton {
    public thinkerImage(int id, int x, int y, int u, int v, int width, int height, String path, String displayText) {
        super(id, x, y,width,height,displayText);
        this.id=id;
        this.x=x;
        this.y=y;
        this.u=u;
        this.v=v;
        this.width=width;
        this.height=height;
        this.path=path;
        rotateAngle=0;
    }
    public int id,x,y,rotateAngle, u, v, width, height;
    public String path;
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        updateHoverState(mouseX,mouseY);
        if (this.visible) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            animeList.forEach(anime->anime.animeDrawPre(initTime));
            GL11.glColor4f(1.0F,1.0F,1.0F,1.0F);
            mc.getTextureManager().bindTexture(new ResourceLocation(MOD_ID,path));
            GL11.glTranslatef(x + (height / 2F), y + (width / 2F),0);
            animeList.forEach(anime->anime.animeDraw(initTime));
            GL11.glTranslatef(-(x + (height / 2F)), -(y + (width / 2F)),0);
            this.drawTexturedModalRect(x, y, u, v, this.width, this.height);
            animeList.forEach(anime->anime.animeDrawAfter(initTime));
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glPopMatrix();
        }
    }
}
