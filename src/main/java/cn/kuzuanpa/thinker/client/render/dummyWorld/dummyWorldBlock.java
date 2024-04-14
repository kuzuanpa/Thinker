package cn.kuzuanpa.thinker.client.render.dummyWorld;

import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnimes;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldGraphicAnime;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

public class dummyWorldBlock {

    public Block block;
    public ItemStack itemStack;
    public boolean renderAllFaces;
    public int meta;
    public ArrayList<IDummyWorldAnimes> animeList = new ArrayList<>();

    public dummyWorldBlock setRenderAllFace(boolean renderAllFace){this.renderAllFaces=renderAllFace;return this;}
    public dummyWorldBlock(Block block,ArrayList<IDummyWorldAnimes> animes){
        this(block,0,animes);
    }

    public dummyWorldBlock(Block block, IDummyWorldAnimes... animes){
        this(block,0,animes);
    }
    public dummyWorldBlock(Block block) {
        this( block,0);
    }
    public dummyWorldBlock(ItemStack itemStack, IDummyWorldAnimes... animes){
        this.itemStack =itemStack;
        Collections.addAll(animeList,animes);
    }
    public dummyWorldBlock(ItemStack itemStack, ArrayList<IDummyWorldAnimes> animes){
        this.itemStack =itemStack;
        this.animeList=animes;
    }
    public dummyWorldBlock(Block block, int meta, IDummyWorldAnimes... animes){
        this.block=block;
        this.meta=meta;
        Collections.addAll(animeList,animes);
    }
    public dummyWorldBlock(Block block, int meta, ArrayList<IDummyWorldAnimes> animes){
        this.block=block;
        this.meta=meta;
        this.animeList=animes;
    }

}
