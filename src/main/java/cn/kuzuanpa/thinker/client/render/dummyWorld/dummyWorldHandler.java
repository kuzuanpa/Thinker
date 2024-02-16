package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.profileHandler;

import java.util.HashMap;

public class dummyWorldHandler {
    public static HashMap<BlockPosition,dummyWorldBlock> dummyWorldBlocksHashMap=new HashMap<>();
    public static HashMap<BlockPosition,dummyWorldTileEntity> dummyWorldTileEntityHashMap=new HashMap<>();
    public static void onProfileChanged(String profileID){
        dummyWorldBlocksHashMap.clear();
        dummyWorldTileEntityHashMap.clear();
        dummyWorldBlocksHashMap.putAll(profileHandler.getProfile(profileID).dummyWorldBlocks);
        dummyWorldTileEntityHashMap.putAll(profileHandler.getProfile(profileID).dummyWorldTileEntities);
    }

}
