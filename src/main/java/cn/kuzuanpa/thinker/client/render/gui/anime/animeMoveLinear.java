package cn.kuzuanpa.thinker.client.render.gui.anime;

import org.lwjgl.opengl.GL11;

public class animeMoveLinear implements IAnime{
    public animeMoveLinear(int startTime, int endTime, int dX, int dY){
        this.startTime=startTime;
        this.dX =dX;
        this.dY =dY;
        this.endTime=endTime;
    }
    public int startTime, endTime, dX, dY;
    @Override
    public void animeDraw(long initTime, long currentTime) {
        long timer = currentTime-initTime;
        if(timer<startTime) return;
        if (timer < endTime) GL11.glTranslatef((float)(timer - startTime)/(float)(endTime-startTime)* dX,(float)(timer - startTime)/(float)(endTime-startTime)* dY, 0);
        else GL11.glTranslatef(dX, dY, 0);
    }

    @Override
    public void animeDrawPre(long initTime, long currentTime) {

    }

    @Override
    public void animeDrawAfter(long currentTime, long initTime) {

    }
}
