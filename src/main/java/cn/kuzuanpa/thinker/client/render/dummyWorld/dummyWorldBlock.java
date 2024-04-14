package cn.kuzuanpa.thinker.client.render.dummyWorld;

import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyBlockAnime;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

public class dummyWorldBlock {

    public Block block;
    public ItemStack itemStack;
    public boolean renderAllFaces;
    public int meta;
    public ArrayList<IDummyBlockAnime> animeList = new ArrayList<>();

    public dummyWorldBlock(Block block,ArrayList<IDummyBlockAnime> animes){
        this(block,0,animes);
    }

    public dummyWorldBlock(Block block,IDummyBlockAnime... animes){
        this(block,0,animes);
    }
    public dummyWorldBlock(Block block) {
        this( block,0);
    }
    public dummyWorldBlock(ItemStack itemStack,IDummyBlockAnime... animes){
        this.itemStack =itemStack;
        Collections.addAll(animeList,animes);
    }
    public dummyWorldBlock(ItemStack itemStack, ArrayList<IDummyBlockAnime> animes){
        this.itemStack =itemStack;
        this.animeList=animes;
    }
    public dummyWorldBlock(Block block,int meta,IDummyBlockAnime... animes){
        this.block=block;
        this.meta=meta;
        Collections.addAll(animeList,animes);
    }
    public dummyWorldBlock(Block block, int meta, ArrayList<IDummyBlockAnime> animes){
        this.block=block;
        this.meta=meta;
        this.animeList=animes;
    }

}
