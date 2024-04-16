package cn.kuzuanpa.thinker.client;

import blockrenderer6343.api.utils.BlockPosition;
import static cn.kuzuanpa.thinker.Thinker.isGeckoLibLoaded;

import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldBlock;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldGeckoModel;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldGeckoModelContainer;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldTileEntity;

import java.util.HashMap;

public class dummyWorldHandler {
    public static HashMap<BlockPosition, dummyWorldBlock> dummyWorldBlocksHashMap=new HashMap<>();
    public static HashMap<BlockPosition, dummyWorldTileEntity> dummyWorldTileEntityHashMap=new HashMap<>();
    public static HashMap<BlockPosition, dummyWorldGeckoModel> dummyWorldGeckoModelHashMap=new HashMap<>();
    public static void onProfileChanged(String profileID){
        dummyWorldBlocksHashMap.clear();
        dummyWorldTileEntityHashMap.clear();
        if(isGeckoLibLoaded) dummyWorldGeckoModelHashMap.clear();
        if(profileHandler.getProfile(profileID).dummyWorldBlocks.isEmpty()&&profileHandler.getProfile(profileID).dummyWorldTileEntities.isEmpty()&&(!isGeckoLibLoaded||profileHandler.getProfile(profileID).dummyWorldGeckoModels.isEmpty()))return;
        dummyWorldBlocksHashMap.putAll(profileHandler.getProfile(profileID).dummyWorldBlocks);
        dummyWorldTileEntityHashMap.putAll(profileHandler.getProfile(profileID).dummyWorldTileEntities);
        if(isGeckoLibLoaded) dummyWorldGeckoModelHashMap.putAll(profileHandler.getProfile(profileID).dummyWorldGeckoModels);
        if(isGeckoLibLoaded) dummyWorldGeckoModelHashMap.put(new BlockPosition(0,4,3),new dummyWorldGeckoModel("","",""));
    }

}
