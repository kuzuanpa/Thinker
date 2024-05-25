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
import cn.kuzuanpa.thinker.client.objects.IThinkerAnime;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.dummyWorldBlock;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.dummyWorldTile;

import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.isGeckoLibLoaded;

public class defaultAnimeAdaptor implements IThinkerAnimeAdaptor{
    @Override
    public boolean isMapHaveValidContents(Map<String, Object> values) {

        if (!values.containsKey("type")) return false;
        if (((String) values.get("type")).startsWith("gui")) switch (((String) values.get("type")).replaceFirst("gui", "")) {
            //TODO:
            //case "MoveLinear":
            //    Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.moveLinear)");
            //case "moveLinear":
            //    return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.MoveLinear.isMapHaveValidContents(values);
            //case "Color":
            //    Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.color)");
            //case "RGBA":
            //case "color":
            //    return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.MoveLinear.isMapHaveValidContents(values);
            //case "Rotate":
            //    Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.rotate)");
            //case "rotate":
            //    return dummyWorldBlock.isMapHaveValidContents(values);
            //case "RotateSteadily":
            //    Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.rotateSteadily)");
            //case "rotateSteadily":
            //    return dummyWorldTile.isMapHaveValidContents(values);
            //case "Scale":
            //    Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.scale)");
            //case "scale":
            //    return dummyWorldTile.isMapHaveValidContents(values);
            //case "Transparency":
            //    Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.transparency)");
            //case "transparency":
            //    return dummyWorldTile.isMapHaveValidContents(values);
            default:
                return false;
        }
        if (((String) values.get("type")).startsWith("world")) switch (((String) values.get("type"))) {
            case "world.graphic.MoveLinear":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.moveLinear)");
            case "world.graphic.moveLinear":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.MoveLinear.isMapHaveValidContents(values);
            case "world.graphic.RGBA":
            case "world.graphic.changeColor":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.RGBA.isMapHaveValidContents(values);
            case "world.graphic.OutlineGlowth":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.outlineGlowth)");
            case "world.graphic.outlineGlowth":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.OutlineGlowth.isMapHaveValidContents(values);
            case "world.graphic.RotateSteadily":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.rotateSteadily)");
            case "world.graphic.rotateSteadily":
                return false;//TODO
            case "world.tick.SetTileNBT":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.tick.setTileNBT)");
            case "world.tick.setTileNBT":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.tick.SetTileNBT.isMapHaveValidContents(values);
            case "world.tick.Skip":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.tick.skip)");
            case "world.tick.skip":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.tick.SkipTick.isMapHaveValidContents(values);
            default:
                return false;
        }
        return false;
    }

    @Override
    public IThinkerAnime create(Map<String, Object> values) {


        if (!values.containsKey("type")) return null;
        if (((String) values.get("type")).startsWith("gui")) switch (((String) values.get("type")).replaceFirst("gui", "")) {
            case "MoveLinear":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.moveLinear)");
            case "moveLinear":
                return null;
            case "Color":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.color)");
            case "RGBA":
            case "color":
                return null;
            case "Rotate":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.rotate)");
            case "rotate":
                return null;
            case "RotateSteadily":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.rotateSteadily)");
            case "rotateSteadily":
                return null;
            case "Scale":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.scale)");
            case "scale":
                return null;
            case "Transparency":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.transparency)");
            case "transparency":
                return null;
            default:
                return null;
        }
        if (((String) values.get("type")).startsWith("world")) switch (((String) values.get("type"))) {
            case "world.graphic.MoveLinear":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.moveLinear)");
            case "world.graphic.moveLinear":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.MoveLinear.create(values);
            case "world.graphic.RGBA":
            case "world.graphic.changeColor":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.RGBA.create(values);
            case "world.graphic.OutlineGlowth":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.outlineGlowth)");
            case "world.graphic.outlineGlowth":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.OutlineGlowth.create(values);
            case "world.graphic.RotateSteadily":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.rotateSteadily)");
            case "world.graphic.rotateSteadily":
                return null;
            case "world.tick.SetTileNBT":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.tick.setTileNBT)");
            case "world.tick.setTileNBT":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.tick.SetTileNBT.create(values);
            case "world.tick.Skip":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.tick.skip)");
            case "world.tick.skip":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.tick.SkipTick.create(values);
            default: return null;
        }
        return null;
    }
}
