package cn.kuzuanpa.thinker.client;

import blockrenderer6343.api.utils.BlockPosition;
import static cn.kuzuanpa.thinker.Thinker.isGeckoLibLoaded;

import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldBlockContainer;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldGeckoModel;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldTileEntityContainer;

import java.util.HashMap;

public class dummyWorldHandler {
    public static HashMap<BlockPosition, dummyWorldBlockContainer> dummyWorldBlocksHashMap=new HashMap<>();
    public static HashMap<BlockPosition, dummyWorldTileEntityContainer> dummyWorldTileEntityHashMap=new HashMap<>();
    public static HashMap<BlockPosition, dummyWorldGeckoModel> dummyWorldGeckoModelHashMap=new HashMap<>();
    public static void onProfileChanged(String profileID){
        dummyWorldBlocksHashMap.clear();
        dummyWorldTileEntityHashMap.clear();
        if(isGeckoLibLoaded) dummyWorldGeckoModelHashMap.clear();
        if(profileHandler.getProfile(profileID).dummyWorldBlocks.isEmpty()&&profileHandler.getProfile(profileID).dummyWorldTileEntities.isEmpty()&&(!isGeckoLibLoaded||profileHandler.getProfile(profileID).dummyWorldGeckoModels.isEmpty()))return;
        dummyWorldBlocksHashMap.putAll(profileHandler.getProfile(profileID).dummyWorldBlocks);
        dummyWorldTileEntityHashMap.putAll(profileHandler.getProfile(profileID).dummyWorldTileEntities);
        if(isGeckoLibLoaded) dummyWorldGeckoModelHashMap.putAll(profileHandler.getProfile(profileID).dummyWorldGeckoModels);
    }

}
