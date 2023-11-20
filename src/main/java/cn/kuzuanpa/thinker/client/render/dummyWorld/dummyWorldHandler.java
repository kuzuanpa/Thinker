package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnime;
import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.List;

public class dummyWorldHandler {
    public static HashMap<BlockPosition, IDummyWorldAnime> dummyWorldAnimeHashMap=new HashMap<>();
    public static HashMap<BlockPosition,Block> dummyWorldBlocksHashMap=new HashMap<>();
    public static void addAnime(BlockPosition pos,IDummyWorldAnime anime){
        dummyWorldAnimeHashMap.putIfAbsent(pos,anime);
    }
    public static void removeAnime(BlockPosition pos){
        dummyWorldAnimeHashMap.remove(pos);
    }
    public static void replaceAnime(BlockPosition pos,IDummyWorldAnime anime){
        dummyWorldAnimeHashMap.replace(pos,anime);
    }
}
