package cn.kuzuanpa.thinker.client.render.gui.anime;

public interface IAnime {
    public void animeDraw(long initTime, long currentTime);
    public void animeDrawPre(long initTime, long currentTime);
    public void animeDrawAfter(long initTime,long currentTime);
}

