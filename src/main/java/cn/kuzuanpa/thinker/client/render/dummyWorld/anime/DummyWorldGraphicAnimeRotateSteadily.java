package cn.kuzuanpa.thinker.client.render.dummyWorld.anime;

import org.lwjgl.opengl.GL11;

public class DummyWorldGraphicAnimeRotateSteadily implements IDummyWorldGraphicAnime {
    @Override
    public void animeDraw(long initTime) {
        GL11.glTranslatef(0.5F,0.5F,0);
        GL11.glRotated((System.currentTimeMillis()-initTime) /10F,0,0,1);
        GL11.glTranslatef(-0.5F,-0.5F,0);

    }

    @Override
    public void updateButton(long initTime) {

    }
    @Override
    public String jsonName() {
        return "Block.RotateSteadily";
    }
}
