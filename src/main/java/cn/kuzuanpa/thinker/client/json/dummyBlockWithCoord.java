package cn.kuzuanpa.thinker.client.json;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldBlock;

public class dummyBlockWithCoord {
    public BlockPosition pos;
    public dummyWorldBlock block;
    public dummyBlockWithCoord(BlockPosition pos, dummyWorldBlock block){
        this.pos=pos;
        this.block=block;
    }
}
