package cn.kuzuanpa.thinker.client.render.dummyWorld;

import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnimes;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldGraphicAnime;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.Collections;

public class dummyWorldTileEntity {
    public TileEntity tile;
    public ArrayList<IDummyWorldAnimes> animeList = new ArrayList<>();
    public dummyWorldTileEntity(TileEntity tile, ArrayList<IDummyWorldAnimes> animes){
        this.tile=tile;
        this.animeList=animes;
    }
    public dummyWorldTileEntity(TileEntity tile, IDummyWorldAnimes... animes){
        this.tile=tile;
        Collections.addAll(animeList,animes);
    }
    public dummyWorldTileEntity(TileEntity tile){
        this(tile,new IDummyWorldAnimes[0]);
    }
}
