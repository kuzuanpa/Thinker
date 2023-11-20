package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnime;

import java.util.HashMap;

public class dummyWorldAnimeHandler {
    public static HashMap<BlockPosition, IDummyWorldAnime> dummyWorldAnimeHashMap=new HashMap<>();
    public static void add(BlockPosition pos,IDummyWorldAnime anime){
        dummyWorldAnimeHashMap.putIfAbsent(pos,anime);
    }
    public static void remove(BlockPosition pos){
        dummyWorldAnimeHashMap.remove(pos);
    }
    public static void replace(BlockPosition pos,IDummyWorldAnime anime){
        dummyWorldAnimeHashMap.replace(pos,anime);
    }
}
