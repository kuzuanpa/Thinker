package cn.kuzuanpa.thinker.client.render.gui.anime;

import org.lwjgl.opengl.GL11;

public class animeRotate implements IAnime{
    public animeRotate(int startTime,int endTime,int rotateAngle){
        this.startTime=startTime;
        this.rotateAngle=rotateAngle;
        this.endTime=endTime;
    }
    public int startTime, endTime, rotateAngle;
    @Override
    public void animeDraw(long initTime, long currentTime) {
        long timer = currentTime-initTime;
        if(timer<startTime) return;
        if(timer<endTime)GL11.glRotatef(((float)(timer - startTime)/(float)(endTime-startTime))*rotateAngle,0,0,1);
        else GL11.glRotatef(rotateAngle,0,0,1);
    }

    @Override
    public void animeDrawPre(long initTime, long currentTime) {

    }

    @Override
    public void animeDrawAfter(long currentTime, long initTime) {

    }
}
