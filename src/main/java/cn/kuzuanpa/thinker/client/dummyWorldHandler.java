package cn.kuzuanpa.thinker.client;

import blockrenderer6343.api.utils.BlockPosition;
import static cn.kuzuanpa.thinker.Thinker.isGeckoLibLoaded;

import cn.kuzuanpa.thinker.client.render.dummyWorld.IdummyWorldThinkerObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class dummyWorldHandler {
    public static List<IdummyWorldThinkerObject> dummyWorldObjects =new ArrayList<>();
    public static void onProfileChanged(String profileID){
        dummyWorldObjects.clear();
        if(profileHandler.getProfile(profileID).dummyWorldThinkerObjects.isEmpty())return;
        dummyWorldObjects.addAll(profileHandler.getProfile(profileID).dummyWorldThinkerObjects);
    }

}
