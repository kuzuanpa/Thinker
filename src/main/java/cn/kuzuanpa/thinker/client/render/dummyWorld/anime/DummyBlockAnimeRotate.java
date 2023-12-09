package cn.kuzuanpa.thinker.client.render.dummyWorld.anime;

import org.lwjgl.opengl.GL11;

public class DummyBlockAnimeRotate implements IDummyBlockAnime {
    @Override
    public void animeDraw(long initTime) {
        GL11.glRotated((System.currentTimeMillis()-initTime) /10F,0,0,1);
    }

    @Override
    public void animeDrawPre(long initTime) {

    }

    @Override
    public void animeDrawAfter(long initTime) {

    }

    @Override
    public void updateButton(long initTime) {

    }
}
