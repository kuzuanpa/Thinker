package cn.kuzuanpa.thinker.client.render.dummyWorld.anime;

import blockrenderer6343.client.WorldSceneRenderer;
import org.lwjgl.opengl.GL11;

public class DummyBlockAnimeMoveLinear implements IDummyBlockAnime {
    public DummyBlockAnimeMoveLinear(int startTime, int endTime, float dX, float dY,float dZ){
        this.startTime=startTime;
        this.endTime=endTime;
        this.dX =dX;
        this.dY =dY;
        this.dZ= dZ;
    }
    public int startTime, endTime;
    public float dX, dY, dZ;
    @Override
    public void animeDraw(long initTime, WorldSceneRenderer renderer) {
        long timer = System.currentTimeMillis()-initTime;
        if(timer<startTime) return;
        float progress=(float)(timer - startTime)/(float)(endTime-startTime);
        if (timer < endTime) GL11.glTranslatef(progress * dX,progress * dY, progress*dZ);
        else GL11.glTranslatef(dX, dY, dZ);
    }

    @Override
    public void updateButton(long initTime, WorldSceneRenderer renderer) {

    }

    @Override
    public String jsonName() {
        return "Block.MoveLinear";
    }
}
