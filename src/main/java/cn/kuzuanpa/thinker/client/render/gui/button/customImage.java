package cn.kuzuanpa.thinker.client.render.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

public class customImage extends CommonGuiButton {
    public customImage(int id,int x,int y,int u,int v,int width,int height,String path) {
        super(id, x, y,width,height,"");
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
    public int id,x,y,rotateAngle,scaleX,scaleY, u, v, width, height;
    public long initTime=0,currentTime;
    public String path;
    public byte[] combineRGB;
    public void drawButton(Minecraft mc, int p_146112_2_, int p_146112_3_) {
        if (this.visible) {
            GL11.glPushMatrix();
            animeList.forEach(anime->anime.animeDrawPre(initTime, currentTime));
            mc.getTextureManager().bindTexture(new ResourceLocation(MOD_ID,path));

            //GL11.glColor3b(combineRGB[0],combineRGB[1],combineRGB[2]);
            GL11.glTranslatef(x + (height / 2F), y + (width / 2F),0);
            //GL11.glScaled(scaleX,scaleY,1);
            animeList.forEach(anime->anime.animeDraw(initTime, currentTime));
            GL11.glTranslatef(-(x + (height / 2F)), -(y + (width / 2F)),0);
            this.drawTexturedModalRect(x, y, 0, 0, this.width, this.height);
            animeList.forEach(anime->anime.animeDrawAfter(initTime,currentTime));
            GL11.glPopMatrix();
        }
    }
    @Override
    public void doAnime(long initTime,long currentTime){
        this.initTime=initTime;
        this.currentTime=currentTime;
    }

}
