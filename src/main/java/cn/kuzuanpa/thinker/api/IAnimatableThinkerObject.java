package cn.kuzuanpa.thinker.api;

import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnimes;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;

import java.util.ArrayList;
import java.util.List;

public interface IAnimatableThinkerObject extends IThinkerObject{
    ArrayList<IGuiAnime> GuiAnimeList = new ArrayList<>();
    default ArrayList<IGuiAnime> getGuiAnimeList(){return GuiAnimeList;}

    ArrayList<IDummyWorldAnimes> WorldAnimeList = new ArrayList<>();
    default ArrayList<IDummyWorldAnimes> getWorldAnimeList(){return WorldAnimeList;}
    default IAnimatableThinkerObject addAnimes(List<IGuiAnime> guiAnimes, List<IDummyWorldAnimes> dummyWorldAnimes){
        this.getGuiAnimeList().addAll(guiAnimes);
        this.getWorldAnimeList().addAll(dummyWorldAnimes);
        return this;
    }
}
