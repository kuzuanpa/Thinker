package cn.kuzuanpa.thinker.client.json;

import cn.kuzuanpa.thinker.api.IThinkerObject;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldBlock;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldTile;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldGeckoModel;
import cn.kuzuanpa.thinker.client.render.gui.button.custom.customImage;
import cn.kuzuanpa.thinker.client.render.gui.button.custom.customText;

import java.util.Map;

public class ThinkerObjectsAdaptor {

    public boolean doesMapHaveValidContents(Map<String,Object> values){
        if(!values.containsKey("type"))return false;
        switch ((String) values.get("type")){
            case "image":
            case "Image": return customImage.doesMapHaveValidContents(values);
            case "text":
            case "Text": return customText.doesMapHaveValidContents(values);
            case "block":
            case "Block": return dummyWorldBlock.doesMapHaveValidContents(values);
            case "tile":
            case "Tile":
            case "tileEntity":
            case "TileEntity": return dummyWorldTile.doesMapHaveValidContents(values);
            case "geoModel":
            case "GeoModel":
            case "GeckoModel":
            case "geckoModel":
            case "Gecko":
            case "gecko": return dummyWorldGeckoModel.doesMapHaveValidContents(values);
            default: return false;
        }
    }
    public IThinkerObject create(Map<String,Object> values){
        switch ((String) values.get("type")){
            case "image":
            case "Image": return customImage.create(values);
            case "text":
            case "Text": return customText.create(values);
            case "block":
            case "Block": return dummyWorldBlock.create(values);
            case "tile":
            case "Tile":
            case "tileEntity":
            case "TileEntity": return dummyWorldTile.create(values);
            case "geoModel":
            case "GeoModel":
            case "GeckoModel":
            case "geckoModel":
            case "Gecko":
            case "gecko": return dummyWorldGeckoModel.create(values);
            default: return null;
        }
    }
}
