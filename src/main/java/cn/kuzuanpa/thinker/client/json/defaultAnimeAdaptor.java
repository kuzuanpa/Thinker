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
        if (((String) values.get("type")).startsWith("gui")) switch (((String) values.get("type"))) {
            
            case "gui.MoveLinear":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.moveLinear)");
            case "gui.moveLinear":
                return cn.kuzuanpa.thinker.client.objects.gui.anime.animeMoveLinear.isMapHaveValidContents(values);
            case "gui.Color":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.color)");
            case "gui.RGBA":
            case "gui.color":
                return cn.kuzuanpa.thinker.client.objects.gui.anime.animeRGBA.isMapHaveValidContents(values);
            case "gui.Rotate":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.rotate)");
            case "gui.rotate":
                return false;//dummyWorldBlock.isMapHaveValidContents(values);
            case "gui.RotateSteadily":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.rotateSteadily)");
            case "gui.rotateSteadily":
                return false;//dummyWorldTile.isMapHaveValidContents(values);
            case "gui.Scale":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.scale)");
            case "gui.scale":
                return cn.kuzuanpa.thinker.client.objects.gui.anime.animeScale.isMapHaveValidContents(values);
            case "gui.Transparency":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.transparency)");
            case "gui.transparency":
                return cn.kuzuanpa.thinker.client.objects.gui.anime.animeTransparency.isMapHaveValidContents(values);
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
            case "world.graphic.Hid":
            case "world.graphic.hid":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.Hid.isMapHaveValidContents(values);
            case "world.graphic.OutlineGlowth":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.outlineGlowth)");
            case "world.graphic.outlineGlowth":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.OutlineGlowth.isMapHaveValidContents(values);
            case "world.graphic.RotateSteadily":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.rotateSteadily)");
            case "world.graphic.rotateSteadily":
                return false;//TODO
            case "world.graphic.Rotate":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.rotate)");
            case "world.graphic.rotate":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.Rotate.isMapHaveValidContents(values);
            case "world.graphic.Swing":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.swing)");
            case "world.graphic.swing":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.Swing.isMapHaveValidContents(values);

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
        if (((String) values.get("type")).startsWith("gui")) switch (((String) values.get("type"))) {
            case "gui.MoveLinear":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.moveLinear)");
            case "gui.moveLinear":
                return null;
            case "gui.Color":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.color)");
            case "gui.RGBA":
            case "gui.color":
                return cn.kuzuanpa.thinker.client.objects.gui.anime.animeRGBA.create(values);
            case "gui.Rotate":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.rotate)");
            case "gui.rotate":
                return null;
            case "gui.RotateSteadily":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.rotateSteadily)");
            case "gui.rotateSteadily":
                return null;
            case "gui.Scale":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.scale)");
            case "gui.scale":
                return cn.kuzuanpa.thinker.client.objects.gui.anime.animeScale.create(values);
            case "gui.Transparency":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.transparency)");
            case "gui.transparency":
                return cn.kuzuanpa.thinker.client.objects.gui.anime.animeTransparency.create(values);
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
            case "world.graphic.Hid":
            case "world.graphic.hid":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.Hid.create(values);
            case "world.graphic.OutlineGlowth":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.outlineGlowth)");
            case "world.graphic.outlineGlowth":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.OutlineGlowth.create(values);
            case "world.graphic.RotateSteadily":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.rotateSteadily)");
            case "world.graphic.rotateSteadily":
                return null;
            case "world.graphic.Rotate":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.rotate)");
            case "world.graphic.rotate":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.Rotate.create(values);
            case "world.graphic.Swing":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.swing)");
            case "world.graphic.swing":
                return cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.Swing.create(values);
                
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
