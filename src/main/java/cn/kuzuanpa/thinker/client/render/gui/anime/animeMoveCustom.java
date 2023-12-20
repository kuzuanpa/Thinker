package cn.kuzuanpa.thinker.client.render.gui.anime;

import cn.kuzuanpa.thinker.client.render.gui.button.ThinkerButton;
import org.lwjgl.opengl.GL11;

public class animeMoveCustom implements IGuiAnime {
    public animeMoveCustom(int startTime, int endTime, String XFormula,String YFormula){
        this.startTime=startTime;
        this.endTime=endTime;
        this.XFormula=XFormula;
        this.YFormula=YFormula;
    }
    public int startTime, endTime;
    public String XFormula,YFormula;
    @Override
    public void animeDraw(long initTime) {
        long timer = System.currentTimeMillis()-initTime;
        if(timer<startTime) return;
        if (timer < endTime) GL11.glTranslatef(Integer.getInteger(XFormula, (int) timer),Integer.getInteger(YFormula, (int) timer),0);
        else GL11.glTranslatef(Integer.getInteger(XFormula,endTime),Integer.getInteger(YFormula,  endTime),0);
    }

    @Override
    public void animeDrawPre(long initTime){}

    @Override
    public void animeDrawAfter(long initTime) {}
    @Override
    public void updateButton(long initTime, ThinkerButton button) {
    }
    @Override
    public String jsonName() {
        return "Gui.MoveCustom";
    }
}
