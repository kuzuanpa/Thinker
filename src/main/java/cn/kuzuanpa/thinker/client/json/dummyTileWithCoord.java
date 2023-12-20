package cn.kuzuanpa.thinker.client.json;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldTileEntity;

public class dummyTileWithCoord {
    public BlockPosition pos;
    public dummyWorldTileEntity tile;
    public dummyTileWithCoord(BlockPosition pos,dummyWorldTileEntity tile){
        this.pos=pos;
        this.tile=tile;
    }
}
