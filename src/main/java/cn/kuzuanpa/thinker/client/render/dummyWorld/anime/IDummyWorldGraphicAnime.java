package cn.kuzuanpa.thinker.client.render.dummyWorld.anime;

public interface IDummyWorldGraphicAnime extends IDummyWorldAnimes {
    void animeDraw(long initTime);
    /**Some Anime changed Position or Scale of buttons. update them in there to make things perform correctly when clicked on these button**/
    void updateButton(long initTime);
}
