package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.api.IThinkerObject;

import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getInt;

public class dummyWorldBlock implements IThinkerObject {
    public BlockPosition pos;
    public dummyWorldBlockContainer block;
    public dummyWorldBlock(BlockPosition pos, dummyWorldBlockContainer block){
        this.pos=pos;
        this.block=block;
    }

    public static boolean doesMapHaveValidContents(Map<String,Object> values) {
        return values.containsKey("posX")&&
                values.containsKey("posY")&&
                values.containsKey("posZ")&&
                dummyWorldBlockContainer.doesMapHaveValidContents(values);
    }

    public static dummyWorldBlock create(Map<String, Object> values) {
        return new dummyWorldBlock(new BlockPosition(getInt(values.get("posX")),getInt(values.get("posY")),getInt(values.get("posZ"))),
                dummyWorldBlockContainer.create(values));
    }
}
