package cn.kuzuanpa.thinker.client.render.gui.anime;

import org.lwjgl.opengl.GL11;

public class animeMove implements IAnime{
    public animeMove(int startTime,int endTime,int endPosX,int endPosY){
        this.startTime=startTime;
        this.endPosX=endPosX;
        this.endPosY=endPosY;
        this.endTime=endTime;
    }
    public int startTime, endTime, endPosX, endPosY;
    @Override
    public void animeDraw(long initTime, long currentTime) {
        long timer = currentTime-initTime;
        if(timer<startTime) return;
        if (timer < endTime) GL11.glTranslatef((float)(timer - startTime)/(float)(endTime-startTime)*endPosX,(float)(timer - startTime)/(float)(endTime-startTime)*endPosY , 0);
        else GL11.glTranslatef(endPosX, endPosY, 0);
    }

    @Override
    public void animeDrawPre(long initTime, long currentTime) {

    }

    @Override
    public void animeDrawAfter(long currentTime, long initTime) {

    }
}
