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

package cn.kuzuanpa.thinker.client.objects.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import blockrenderer6343.world.DummyWorld;
import cn.kuzuanpa.thinker.client.anim.dummyWorld.IDummyWorldAnimes;
import cn.kuzuanpa.thinker.client.objects.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.client.objects.IThinkerObject;
import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.IDummyBlockAnimeDrawAdditionalQuads;
import cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic.IDummyWorldGraphicAnime;
import cn.kuzuanpa.thinker.client.anim.gui.IGuiAnime;
import cn.kuzuanpa.thinker.client.objects.gui.ThinkerButtonBase;
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
    public boolean inWorld =false;
    @Override
    public ArrayList<IGuiAnime> getGuiAnimeList() {
        return null;
    }

    @Override
    public ArrayList<IDummyWorldAnimes> getWorldAnimeList() {
        return WorldAnimeList;
    }

    @Override
    public boolean shouldInWorld(long timer) {
        return subObjects.stream().anyMatch(idummyWorldThinkerObject -> idummyWorldThinkerObject.shouldInWorld(timer));
    }

    @Override
    public boolean alreadyInWorld() {
        return inWorld;
    }

    @Override
    public void render(DummyWorld world, long timer, BlockPosition mousePointingPos) {
        GL11.glPushMatrix();

        List<IDummyWorldAnimes> anime = getWorldAnimeList();
        if(anime.stream().noneMatch(a->{
            boolean result=false;
            if(a instanceof IDummyBlockAnimeDrawAdditionalQuads) ((IDummyBlockAnimeDrawAdditionalQuads) a).drawAdditionalQuads(timer);
            if(a instanceof IDummyWorldGraphicAnime){
                GL11.glTranslatef(pos.x, pos.y, pos.z);
                result = ((IDummyWorldGraphicAnime)a).animeDraw(timer);
                GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
            }
            return result;
        }))subObjects.forEach(obj->obj.render(world, timer, mousePointingPos));

        GL11.glPopMatrix();
    }

    @Override
    public List<IdummyWorldThinkerObject> addToWorld(DummyWorld world) {
        inWorld=true;
        List<IdummyWorldThinkerObject> tmp = new ArrayList<>();
        subObjects.forEach(obj->tmp.addAll(obj.addToWorld(world)));
        return tmp;
    }

    @Override
    public List<IdummyWorldThinkerObject> removeFromWorld(DummyWorld world) {
        inWorld=false;
        List<IdummyWorldThinkerObject> tmp = new ArrayList<>();
        subObjects.forEach(obj->tmp.addAll(obj.removeFromWorld(world)));
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
        boolean result= values.containsKey("subObjects")&&values.get("subObjects") instanceof Map && objectsAdaptors.stream().anyMatch(adaptor-> ((Map<String, Object>) values.get("subObjects")).values().stream().allMatch(obj -> adaptor.isMapHaveValidContents((Map<String, Object>) obj)));
        if(!result) thinkerJsonReader.requestLogError("Not Enough contents for bundle: ");
        return result;
    }
    public static dummyWorldBundle create(Map<String, Object> values) {
        dummyWorldBundle bundle = new dummyWorldBundle();

        bundle.pos = new BlockPosition(getInt(values.get("posX")),getInt(values.get("posY")),getInt(values.get("posZ")));

        ArrayList<IThinkerObject> list = new ArrayList<>();
        objectsAdaptors.forEach(adaptor-> ((Map<String,Object>) values.get("subObjects")).values().forEach(obj->list.add(adaptor.create((Map<String, Object>) obj))));
        StringBuilder err = new StringBuilder("");
        list.stream().filter(obj->obj instanceof ThinkerButtonBase).forEach(obj-> err.append(obj.toString()).append(", "));
        if(!err.toString().equals(""))thinkerJsonReader.requestLogError("Unsupported object in bundle"+err);
        list.stream().filter(obj->obj instanceof IdummyWorldThinkerObject).forEach(obj-> bundle.subObjects.add((IdummyWorldThinkerObject) obj));
        return bundle;
    }
}
