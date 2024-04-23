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
import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.api.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.*;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import cn.kuzuanpa.thinker.util.Nbt;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static blockrenderer6343.client.WorldSceneRenderer.setDefaultPassRenderState;
import static cn.kuzuanpa.thinker.Thinker.getInt;

public class dummyWorldTile implements IdummyWorldThinkerObject, IAnimatableThinkerObject {
    public BlockPosition pos;
    public final ArrayList<IDummyWorldAnimes> WorldAnimeList = new ArrayList<>();
    public TileEntity tile;

    public dummyWorldTile(BlockPosition pos, TileEntity tile, ArrayList<IDummyWorldAnimes> animes){
        this.pos=pos;
        this.tile=tile;
        WorldAnimeList.addAll(animes);
    }
    public dummyWorldTile(BlockPosition pos, TileEntity tile, IDummyWorldAnimes... animes) {
        this.pos = pos;
        this.tile = tile;
        Collections.addAll(WorldAnimeList, animes);
    }
    public dummyWorldTile(BlockPosition pos, TileEntity tile) {
        this.pos = pos;
        this.tile = tile;
    }
    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("posX")&&
                values.containsKey("posY")&&
                values.containsKey("posZ")&&
                values.containsKey("tileEntityNBT");
        if(!result) thinkerJsonReader.requestLogError("Not Enough contents for dummyWorldTileEntity: posX, posY, posZ, tileEntityNBT");
        return result;
    }

    public static dummyWorldTile create(Map<String, Object> values) {

        NBTTagCompound tileEntityNBT= (NBTTagCompound) Nbt.stringToNBT((String) values.get("tileEntityNBT"));
        //replace pos
        tileEntityNBT.setInteger("x",getInt(values.get("posX")));
        tileEntityNBT.setInteger("y",getInt(values.get("posY")));
        tileEntityNBT.setInteger("z",getInt(values.get("posZ")));
        TileEntity tile = TileEntity.createAndLoadEntity(tileEntityNBT);

        return new dummyWorldTile(new BlockPosition(getInt(values.get("posX")),getInt(values.get("posY")),getInt(values.get("posZ"))),tile);
    }
    @Override
    public ArrayList<IGuiAnime> getGuiAnimeList() {
        return null;
    }

    @Override
    public ArrayList<IDummyWorldAnimes> getWorldAnimeList() {
        return WorldAnimeList;
    }

    @Override
    public void render(DummyWorld world, long initTime, boolean isMousePointed) {
        for (int pass = 0; pass < 2; pass++) {
            ForgeHooksClient.setRenderPass(pass);
            if (pos == null || tile == null) return;
            GL11.glPushMatrix();
            setDefaultPassRenderState(pass);
            if (tile.shouldRenderInPass(pass)) {
                GL11.glTranslatef(pos.x, pos.y, pos.z);
                List<IDummyWorldAnimes> anime = getWorldAnimeList();
                if (anime != null && !anime.isEmpty()) anime.forEach(a -> {
                    if (a instanceof IDummyBlockAnimeDrawAdditionalQuads)
                        ((IDummyBlockAnimeDrawAdditionalQuads) a).drawAdditionalQuads(initTime);
                    if (a instanceof IDummyWorldGraphicAnime) {
                        GL11.glTranslatef(pos.x, pos.y, pos.z);
                        ((IDummyWorldGraphicAnime) a).animeDraw(initTime);
                        GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
                    }
                    if (a instanceof IDummyWorldTilePropertiesAnime) {
                        ((IDummyWorldTilePropertiesAnime) a).doAnime(tile);
                    }
                });
                GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
                int i = world.getLightBrightnessForSkyBlocks(pos.x, pos.y, pos.z, 0);
                float j = i % 65536;
                float k = i / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                TileEntityRendererDispatcher.instance.renderTileEntityAt(tile, pos.x, pos.y, pos.z, 0);
                if (isMousePointed) DummyWorldGraphicAnimeOutlineGlowth.renderBlockOutlineAt(pos, 0xCCCCCC, 2F);
            }
            GL11.glPopMatrix();
        }
    }

    @Override
    public BlockPosition getPos() {
        return pos;
    }
}
