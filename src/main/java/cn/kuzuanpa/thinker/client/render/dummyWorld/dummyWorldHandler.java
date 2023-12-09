package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.profileHandler;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.DummyBlockAnimeMoveLinear;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyBlockAnime;
import cn.kuzuanpa.thinker.client.render.gui.button.DummyWorld;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class dummyWorldHandler {
    public static HashMap<BlockPosition,dummyWorldBlock> dummyWorldBlocksHashMap=new HashMap<>();
    public static HashMap<BlockPosition,dummyWorldTileEntity> dummyWorldTileEntityHashMap=new HashMap<>();
    public static void onProfileChanged(int profileID){
        dummyWorldBlocksHashMap.clear();
        dummyWorldTileEntityHashMap.clear();
        dummyWorldBlocksHashMap.putAll(profileHandler.profileList.get(profileID).dummyWorldBlocks);
        dummyWorldTileEntityHashMap.putAll(profileHandler.profileList.get(profileID).dummyWorldTileEntities);
    }
}
