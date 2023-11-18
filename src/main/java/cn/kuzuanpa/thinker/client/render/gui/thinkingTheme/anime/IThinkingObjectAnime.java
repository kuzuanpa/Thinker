package cn.kuzuanpa.thinker.client.render.gui.thinkingTheme.anime;

public interface IThinkingObjectAnime {
    void animeDraw(long initTime);
    void animeDrawPre(long initTime);
    void animeDrawAfter(long initTime);
    /**Some Anime changed Position or Scale of buttons. update them in there to make things perform correctly when clicked on these button**/
    void updateButton(long initTime);
}
