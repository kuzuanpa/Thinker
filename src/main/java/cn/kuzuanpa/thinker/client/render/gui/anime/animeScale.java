package cn.kuzuanpa.thinker.client.render.gui.anime;

import cn.kuzuanpa.thinker.client.render.gui.button.CommonGuiButton;
import org.lwjgl.opengl.GL11;

public class animeScale implements IAnime{
    public animeScale(int startTime,int endTime,float scaleRate,float scaleX,float scaleY){
        this.startTime=startTime;
        this.endTime=endTime;
        this.scaleRate=scaleRate;
        this.scaleX=scaleX;
        this.scaleY=scaleY;
    }
    public int startTime, endTime;
    public float scaleRate,scaleX,scaleY;
    @Override
    public void animeDraw(long initTime) {
        long timer = System.currentTimeMillis()-initTime;

        if(timer<startTime) return;
        if(timer<endTime) GL11.glScalef(((float)(timer - startTime)/(float)(endTime-startTime))*scaleX*scaleRate,((float)(timer - startTime)/(float)(endTime-startTime))*scaleY*scaleRate,1);
        else GL11.glScalef(scaleX*scaleRate,scaleY*scaleRate,1);
    }

    @Override
    public void animeDrawPre(long initTime) {}

    @Override
    public void animeDrawAfter(long initTime) {}
    @Override
    public void updateButton(long initTime, CommonGuiButton button) {
        //TODO
    }
}
