package cn.kuzuanpa.thinker.client.render.dummyWorld.anime;

import blockrenderer6343.client.WorldSceneRenderer;
import org.lwjgl.opengl.GL11;

public class DummyBlockAnimeRotateSteadily implements IDummyBlockAnime {
    @Override
    public void animeDraw(long initTime, WorldSceneRenderer renderer) {
        GL11.glRotated((System.currentTimeMillis()-initTime) /10F,0,0,1);
    }

    @Override
    public void updateButton(long initTime, WorldSceneRenderer renderer) {

    }
    @Override
    public String jsonName() {
        return "Block.RotateSteadily";
    }
}
