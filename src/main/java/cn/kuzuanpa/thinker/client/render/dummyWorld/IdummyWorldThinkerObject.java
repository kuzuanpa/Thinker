package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import blockrenderer6343.world.DummyWorld;
import cn.kuzuanpa.thinker.api.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.api.IThinkerObject;

public interface IdummyWorldThinkerObject extends IThinkerObject, IAnimatableThinkerObject {
    void render(DummyWorld world, long initTime, boolean isMousePointed);

    BlockPosition getPos();
}
