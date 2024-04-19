package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.api.IThinkerObject;

import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getInt;

public class dummyWorldTile implements IThinkerObject {
    public BlockPosition pos;
    public dummyWorldTileEntityContainer tile;
    public dummyWorldTile(BlockPosition pos, dummyWorldTileEntityContainer tile){
        this.pos=pos;
        this.tile=tile;
    }
    public static boolean doesMapHaveValidContents(Map<String,Object> values) {
        return values.containsKey("posX")&&
                values.containsKey("posY")&&
                values.containsKey("posZ")&&
                dummyWorldTileEntityContainer.doesMapHaveValidContents(values);
    }

    public static dummyWorldTile create(Map<String, Object> values) {
        return new dummyWorldTile(new BlockPosition(getInt(values.get("posX")),getInt(values.get("posY")),getInt(values.get("posZ"))),
                dummyWorldTileEntityContainer.create(values));
    }
}
