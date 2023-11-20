package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnime;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class dummyWorldHandler {
    public static HashMap<BlockPosition, List<IDummyWorldAnime>> dummyWorldAnimeHashMap=new HashMap<>();
    public static HashMap<BlockPosition,Block> dummyWorldBlocksHashMap=new HashMap<>();
    public static void newBlockAnime(BlockPosition pos,IDummyWorldAnime anime){
        ArrayList<IDummyWorldAnime> tmpList=new ArrayList<>();
        tmpList.add(anime);
        dummyWorldAnimeHashMap.putIfAbsent(pos, tmpList);
    }
    public static void addAnime(BlockPosition pos,IDummyWorldAnime anime){
        List<IDummyWorldAnime> animeList=dummyWorldAnimeHashMap.get(pos);
        if(animeList==null)newBlockAnime(pos,anime);
        else animeList.add(anime);
    }
    public static void removeAllAnime(BlockPosition pos){
        dummyWorldAnimeHashMap.remove(pos);
    }
    public static void removeAnime(BlockPosition pos,IDummyWorldAnime anime){
        List<IDummyWorldAnime> animeList=dummyWorldAnimeHashMap.get(pos);
        if(animeList==null||animeList.isEmpty())return;
        animeList.remove(anime);
        if(animeList.isEmpty())dummyWorldAnimeHashMap.remove(pos);
    }
}
