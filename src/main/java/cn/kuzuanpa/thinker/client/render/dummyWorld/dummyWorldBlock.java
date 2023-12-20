package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyBlockAnime;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Collections;

public class dummyWorldBlock {
    public Block block;
    public int meta;
    public ArrayList<IDummyBlockAnime> animeList = new ArrayList<>();
    public dummyWorldBlock(Block block,int meta,ArrayList<IDummyBlockAnime> animes){
        this.block=block;
        this.meta=meta;
        this.animeList=animes;
    }
    public dummyWorldBlock(Block block,ArrayList<IDummyBlockAnime> animes){
        this.block=block;
        this.meta=0;
        this.animeList=animes;
    }
    public dummyWorldBlock(Block block,int meta,IDummyBlockAnime... animes){
        this.block=block;
        this.meta=meta;
        Collections.addAll(animeList,animes);
    }
    public dummyWorldBlock(Block block,IDummyBlockAnime... animes){
        this.block=block;
        this.meta=0;
        Collections.addAll(animeList,animes);
    }
    public dummyWorldBlock(Block block) {
        this( block,0);
    }

}
