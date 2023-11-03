package cn.kuzuanpa.thinker.client.render.gui.anime;

import cn.kuzuanpa.thinker.Thinker;
import org.lwjgl.opengl.GL11;

public class animeRGBA implements IAnime{
    public animeRGBA(int startTime, int endTime,int startR,int startG,int startB,int startA,int dR,int dG,int dB,int dA){
        this.startTime=startTime;
        this.endTime=endTime;
        this.startR=startR;
        this.startG=startG;
        this.startB=startB;
        this.startA=startA;
        this.dR=dR;
        this.dG=dG;
        this.dB=dB;
        this.dA=dA;
    }
    public int startTime, endTime,startR, startG, startB, startA,dR,dG,dB,dA;
    @Override
    public void animeDraw(long initTime, long currentTime) {
        long timer = currentTime-initTime;
        if((startR+dR)>255||(startG+dG)>255||(startB+dB)>255||(startA+dA)>255) Thinker.error(new IllegalArgumentException("RGBA value is too big: dR:"+dR+",dG:"+dG+",dB:"+dB+",dA:"+dA));
        if(timer<startTime) return;
        if(timer<endTime){
            float f1=((float)(timer - startTime)/(float)(endTime-startTime));
            GL11.glColor4ub((byte) (startR+(f1*dR)), (byte) (startG+(f1*dG)), (byte) (startB+(f1*dB)), (byte) (startA+(f1*dA)));
        } else GL11.glColor4ub((byte) (startR+dR), (byte) (startG+dG), (byte) (startB+dB), (byte) (startA+dA));
    }

    @Override
    public void animeDrawPre(long initTime, long currentTime) {

    }

    @Override
    public void animeDrawAfter(long currentTime, long initTime) {

    }
}
