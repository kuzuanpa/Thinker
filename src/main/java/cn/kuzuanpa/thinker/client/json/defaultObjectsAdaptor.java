/*
 * This class was created by <kuzuanpa>. It is a part of Thinker.
 * Get the Source Code in github:
 * https://github.com/kuzuanpa/Thinker
 *
 * Thinker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * Thinker is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 */
package cn.kuzuanpa.thinker.client.json;

import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.api.IThinkerObject;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldBlock;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldTile;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldGeckoModel;
import cn.kuzuanpa.thinker.client.render.gui.button.custom.customImage;
import cn.kuzuanpa.thinker.client.render.gui.button.custom.customText;

import java.util.Map;

public class defaultObjectsAdaptor implements IThinkerObjectsAdaptor {

    public defaultObjectsAdaptor(){}
    public boolean isMapHaveValidContents(Map<String,Object> values){
        if(!values.containsKey("type"))return false;
        switch ((String) values.get("type")){
            case "Image": Thinker.log("syntax improvable: First Letter should be lower case.(type=image)");
            case "image": return customImage.isMapHaveValidContents(values);
            case "Text": Thinker.log("syntax improvable: First Letter should be lower case.(type=text)");
            case "text": return customText.isMapHaveValidContents(values);
            case "Block": Thinker.log("syntax improvable: First Letter should be lower case.(type=block)");
            case "block": return dummyWorldBlock.isMapHaveValidContents(values);
            case "Tile":
            case "TileEntity":Thinker.log("syntax improvable: First Letter should be lower case.(type=tileEntity)");
            case "tile":
            case "tileEntity": return dummyWorldTile.isMapHaveValidContents(values);
            case "GeoModel":
            case "GeckoModel":
            case "Gecko": Thinker.log("syntax improvable: First Letter should be lower case.(type=geckoModel)");
            case "geoModel":
            case "geckoModel":
            case "gecko": return dummyWorldGeckoModel.isMapHaveValidContents(values);
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
