package cn.kuzuanpa.thinker.client.render.gui.anime;

import cn.kuzuanpa.thinker.client.render.gui.button.ThinkerButton;
import org.lwjgl.opengl.GL11;

public class animeRotate implements IGuiAnime {
    public animeRotate(int startTime,int endTime,int rotateAngle){
        this.startTime=startTime;
        this.rotateAngle=rotateAngle;
        this.endTime=endTime;
    }
    public int startTime, endTime, rotateAngle;
    @Override
    public void animeDraw(long initTime) {
        long timer = System.currentTimeMillis()-initTime;

        if(timer<startTime) return;
        if(timer<endTime)GL11.glRotatef(((float)(timer - startTime)/(float)(endTime-startTime))*rotateAngle,0,0,1);
        else GL11.glRotatef(rotateAngle,0,0,1);
    }

    @Override
    public void animeDrawPre(long initTime) {

    }

    @Override
    public void animeDrawAfter(long initTime) {

    }
    @Override
    public void updateButton(long initTime, ThinkerButton button) {

    }
    @Override
    public String jsonName() {
        return "Gui.Rotate";
    }
}
