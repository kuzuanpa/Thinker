package cn.kuzuanpa.thinker.client.render.dummyWorld.anime;

import blockrenderer6343.client.WorldSceneRenderer;

public interface IDummyBlockAnime {
    void animeDraw(long initTime, WorldSceneRenderer renderer);
    /**Some Anime changed Position or Scale of buttons. update them in there to make things perform correctly when clicked on these button**/
    void updateButton(long initTime, WorldSceneRenderer renderer);

    String jsonName();
}
