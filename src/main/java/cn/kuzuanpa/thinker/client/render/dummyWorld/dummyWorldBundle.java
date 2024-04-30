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

package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import blockrenderer6343.world.DummyWorld;
import cn.kuzuanpa.thinker.api.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.api.IThinkerObject;
import cn.kuzuanpa.thinker.client.dummyWorldHandler;
import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.*;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import cn.kuzuanpa.thinker.client.render.gui.button.ThinkerButton;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getInt;
import static cn.kuzuanpa.thinker.client.json.thinkerJsonReader.objectsAdaptors;

public class dummyWorldBundle implements IdummyWorldThinkerObject, IAnimatableThinkerObject {
    public dummyWorldBundle(){}
    public final ArrayList<IdummyWorldThinkerObject> subObjects=new ArrayList<>();
    public final ArrayList<IDummyWorldAnimes> WorldAnimeList = new ArrayList<>();
    public BlockPosition pos;

    @Override
    public ArrayList<IGuiAnime> getGuiAnimeList() {
        return null;
    }

    @Override
    public ArrayList<IDummyWorldAnimes> getWorldAnimeList() {
        return WorldAnimeList;
    }

    @Override
    public void render(DummyWorld world, long initTime, BlockPosition mousePointingPos) {
        GL11.glPushMatrix();

        List<IDummyWorldAnimes> anime = getWorldAnimeList();
        if(anime!=null&&!anime.isEmpty())anime.forEach(a->{
            if(a instanceof IDummyBlockAnimeDrawAdditionalQuads) ((IDummyBlockAnimeDrawAdditionalQuads) a).drawAdditionalQuads(initTime);
            if(a instanceof IDummyWorldGraphicAnime){
                GL11.glTranslatef(pos.x, pos.y, pos.z);
                ((IDummyWorldGraphicAnime)a).animeDraw(initTime);
                GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
            }
            if(a instanceof IDummyWorldTilePropertiesAnime){
                dummyWorldHandler.dummyWorldObjects.stream().filter(obj -> obj instanceof dummyWorldTile&&obj.getPos()==pos).forEach(tile->((IDummyWorldTilePropertiesAnime) a).doAnime(((dummyWorldTile)tile).tile));
            }
        });
        subObjects.forEach(obj->obj.render(world, initTime, mousePointingPos));

        GL11.glPopMatrix();
    }

    @Override
    public List<IdummyWorldThinkerObject> syncWithWorld(DummyWorld world) {
        List<IdummyWorldThinkerObject> tmp = new ArrayList<>();
        subObjects.forEach(obj->tmp.addAll(obj.syncWithWorld(world)));
        return tmp;
    }

    @Override
    public BlockPosition getPos() {
        return pos;
    }
    public dummyWorldBundle addSubObject(IdummyWorldThinkerObject object){
        subObjects.add(object);
        return this;
    }
    //JsonReader
    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result= values.containsKey("posX")&& values.containsKey("posY")&& values.containsKey("posZ")&&
                values.containsKey("subObjects")&&values.get("subObjects") instanceof Map && objectsAdaptors.stream().anyMatch(adaptor-> ((Map<String, Object>) values.get("subObjects")).values().stream().allMatch(obj -> adaptor.isMapHaveValidContents((Map<String, Object>) obj)));
        if(!result) thinkerJsonReader.requestLogError("Not Enough contents for bundle: ");
        return result;
    }
    public static dummyWorldBundle create(Map<String, Object> values) {
        dummyWorldBundle bundle = new dummyWorldBundle();

        bundle.pos = new BlockPosition(getInt(values.get("posX")),getInt(values.get("posY")),getInt(values.get("posZ")));

        ArrayList<IThinkerObject> list = new ArrayList<>();
        objectsAdaptors.forEach(adaptor-> ((Map<String,Object>) values.get("subObjects")).values().forEach(obj->list.add(adaptor.create((Map<String, Object>) obj))));
        StringBuilder err = new StringBuilder("");
        list.stream().filter(obj->obj instanceof ThinkerButton).forEach(obj-> err.append(obj.toString()).append(", "));
        if(!err.toString().equals(""))thinkerJsonReader.requestLogError("Unsupported object in bundle"+err);
        list.stream().filter(obj->obj instanceof IdummyWorldThinkerObject).forEach(obj-> bundle.subObjects.add((IdummyWorldThinkerObject) obj));
        return bundle;
    }
}
