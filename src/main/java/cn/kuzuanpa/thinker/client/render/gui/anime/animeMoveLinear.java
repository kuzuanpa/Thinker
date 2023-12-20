package cn.kuzuanpa.thinker.client.render.gui.anime;

import cn.kuzuanpa.thinker.client.render.gui.button.ThinkerButton;
import org.lwjgl.opengl.GL11;

public class animeMoveLinear implements IGuiAnime {
    public animeMoveLinear(int startTime, int endTime, int dX, int dY){
        this.startTime=startTime;
        this.dX =dX;
        this.dY =dY;
        this.endTime=endTime;
    }
    public int startTime, endTime, dX, dY;
    @Override
    public void animeDraw(long initTime) {
        long timer = System.currentTimeMillis()-initTime;
        if(timer<startTime) return;
        if (timer < endTime) GL11.glTranslatef(((float)(timer - startTime)/(float)(endTime-startTime))* dX,((float)(timer - startTime)/(float)(endTime-startTime))* dY, 0);
        else GL11.glTranslatef(dX, dY, 0);
    }

    @Override
    public void animeDrawPre(long initTime) {

    }

    @Override
    public void animeDrawAfter(long initTime) {

    }

    @Override
    public void updateButton(long initTime, ThinkerButton button) {
        long timer = System.currentTimeMillis()-initTime;
        if(timer<startTime) return;
        if (timer < endTime) {
            float f1=((float)(timer - startTime)/(float)(endTime-startTime));
            button.animeXModify+= (int) (dX*f1);
            button.animeYModify+= (int) (dY*f1);
        }
        else {
            button.animeXModify +=dX ;
            button.animeYModify +=dY ;
        }
    }
    @Override
    public String jsonName() {
        return "Gui.MoveLinear";
    }
}
