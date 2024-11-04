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
import cn.kuzuanpa.thinker.client.anim.gui.*;
import cn.kuzuanpa.thinker.client.objects.IThinkerAnime;

import java.util.Map;

public class defaultAnimeAdaptor implements IThinkerAnimeAdaptor{
    @Override
    public boolean isMapHaveValidContents(Map<String, Object> values) {

        if (!values.containsKey("type")) return false;
        if (((String) values.get("type")).startsWith("gui")) switch (((String) values.get("type"))) {
            
            case "gui.MoveLinear":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.moveLinear)");
            case "gui.moveLinear":
                return animeMoveLinear.isMapHaveValidContents(values);
            case "gui.Color":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.color)");
            case "gui.RGBA":
            case "gui.color":
                return animeRGBA.isMapHaveValidContents(values);
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
                return animeScale.isMapHaveValidContents(values);
            case "gui.Transparency":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.transparency)");
            case "gui.transparency":
                return animeTransparency.isMapHaveValidContents(values);
            case "gui.setCamera":
                return setCamera.isMapHaveValidContents(values);
            default:
                return false;
        }
        if (((String) values.get("type")).startsWith("world")) switch (((String) values.get("type"))) {
            case "world.graphic.MoveLinear":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.moveLinear)");
            case "world.graphic.moveLinear":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.MoveLinear.isMapHaveValidContents(values);
            case "world.graphic.RGBA":
            case "world.graphic.changeColor":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.RGBA.isMapHaveValidContents(values);
            case "world.graphic.Hid":
            case "world.graphic.hid":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.Hid.isMapHaveValidContents(values);
            case "world.graphic.OutlineGlowth":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.outlineGlowth)");
            case "world.graphic.outlineGlowth":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.OutlineGlowth.isMapHaveValidContents(values);
            case "world.graphic.RotateSteadily":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.rotateSteadily)");
            case "world.graphic.rotateSteadily":
                return false;//TODO
            case "world.graphic.Rotate":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.rotate)");
            case "world.graphic.rotate":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.Rotate.isMapHaveValidContents(values);
            case "world.graphic.Swing":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.swing)");
            case "world.graphic.swing":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.Swing.isMapHaveValidContents(values);

            case "world.tick.SetTileNBT":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.tick.setTileNBT)");
            case "world.tick.setTileNBT":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.tick.SetTileNBT.isMapHaveValidContents(values);
            case "world.tick.Skip":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.tick.skip)");
            case "world.tick.skip":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.tick.SkipTick.isMapHaveValidContents(values);
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
                return animeRGBA.create(values);
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
                return animeScale.create(values);
            case "gui.Transparency":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=gui.transparency)");
            case "gui.transparency":
                return animeTransparency.create(values);
            case "gui.setCamera":
                return setCamera.create(values);
            default:
                return null;
        }
        if (((String) values.get("type")).startsWith("world")) switch (((String) values.get("type"))) {
            case "world.graphic.MoveLinear":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.moveLinear)");
            case "world.graphic.moveLinear":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.MoveLinear.create(values);
            case "world.graphic.RGBA":
            case "world.graphic.changeColor":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.RGBA.create(values);
            case "world.graphic.Hid":
            case "world.graphic.hid":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.Hid.create(values);
            case "world.graphic.OutlineGlowth":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.outlineGlowth)");
            case "world.graphic.outlineGlowth":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.OutlineGlowth.create(values);
            case "world.graphic.RotateSteadily":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.rotateSteadily)");
            case "world.graphic.rotateSteadily":
                return null;
            case "world.graphic.Rotate":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.rotate)");
            case "world.graphic.rotate":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.Rotate.create(values);
            case "world.graphic.Swing":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.graphic.swing)");
            case "world.graphic.swing":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.Swing.create(values);
                
            case "world.tick.SetTileNBT":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.tick.setTileNBT)");
            case "world.tick.setTileNBT":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.tick.SetTileNBT.create(values);
            case "world.tick.Skip":
                Thinker.log("syntax improvable: First Letter should be lower case.(type=world.tick.skip)");
            case "world.tick.skip":
                return cn.kuzuanpa.thinker.client.anim.dummyWorld.tick.SkipTick.create(values);
            default: return null;
        }
        return null;
    }
}
