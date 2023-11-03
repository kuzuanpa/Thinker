package cn.kuzuanpa.thinker.client.render.gui.anime;

import org.lwjgl.opengl.GL11;

public class animeRotateSteadily implements IAnime{
    public animeRotateSteadily(float speed){
        this.speed=speed;
    }
    public float speed=1.0F;
    @Override
    public void animeDraw(long initTime, long currentTime) {
        GL11.glRotated(((currentTime%360000)*speed)%360,0,0,1);
    }

    @Override
    public void animeDrawPre(long initTime, long currentTime) {

    }

    @Override
    public void animeDrawAfter(long currentTime, long initTime) {

    }
}
