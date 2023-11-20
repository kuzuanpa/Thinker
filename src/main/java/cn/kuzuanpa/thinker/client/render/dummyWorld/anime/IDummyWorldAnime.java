package cn.kuzuanpa.thinker.client.render.dummyWorld.anime;

public interface IDummyWorldAnime {
    void animeDraw(long initTime);
    void animeDrawPre(long initTime);
    void animeDrawAfter(long initTime);
    /**Some Anime changed Position or Scale of buttons. update them in there to make things perform correctly when clicked on these button**/
    void updateButton(long initTime);
}
