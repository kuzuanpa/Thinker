package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyBlockAnime;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.Collections;

public class dummyWorldTileEntity {
    public TileEntity tile;
    public ArrayList<IDummyBlockAnime> animeList = new ArrayList<>();
    public dummyWorldTileEntity(TileEntity tile, ArrayList<IDummyBlockAnime> animes){
        this.tile=tile;
        this.animeList=animes;
    }
    public dummyWorldTileEntity(TileEntity tile, IDummyBlockAnime... animes){
        this.tile=tile;
        Collections.addAll(animeList,animes);
    }
    public dummyWorldTileEntity(TileEntity tile){
        this(tile,new IDummyBlockAnime[0]);
    }
}
