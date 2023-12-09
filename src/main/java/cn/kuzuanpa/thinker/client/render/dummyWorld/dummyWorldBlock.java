package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyBlockAnime;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Collections;

public class dummyWorldBlock {
    public Block block;
    public ArrayList<IDummyBlockAnime> animeList = new ArrayList<>();
    public dummyWorldBlock(Block block,IDummyBlockAnime... animes){
        this.block=block;
        Collections.addAll(animeList,animes);
    }
    public dummyWorldBlock(Block block) {
        this( block, new IDummyBlockAnime[0]);
    }

}
