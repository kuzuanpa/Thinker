package cn.kuzuanpa.thinker.client.render.dummyWorld.anime;

import blockrenderer6343.client.WorldSceneRenderer;
import org.lwjgl.opengl.GL11;

public class DummyBlockAnimeRotateSteadily implements IDummyBlockAnime {
    @Override
    public void animeDraw(long initTime, WorldSceneRenderer renderer) {
        GL11.glTranslatef(0.5F,0.5F,0);
        GL11.glRotated((System.currentTimeMillis()-initTime) /10F,0,0,1);
        GL11.glTranslatef(-0.5F,-0.5F,0);

    }

    @Override
    public void updateButton(long initTime, WorldSceneRenderer renderer) {

    }
    @Override
    public String jsonName() {
        return "Block.RotateSteadily";
    }
}
