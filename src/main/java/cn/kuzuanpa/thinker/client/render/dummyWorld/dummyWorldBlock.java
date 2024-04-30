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
import cn.kuzuanpa.thinker.client.dummyWorldHandler;
import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.*;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getBoolean;
import static cn.kuzuanpa.thinker.Thinker.getInt;
import static cn.kuzuanpa.thinker.client.json.thinkerJsonReader.getItemStack;

public class dummyWorldBlock implements IdummyWorldThinkerObject, IAnimatableThinkerObject {
    public final ArrayList<IDummyWorldAnimes> WorldAnimeList = new ArrayList<>();
    public boolean renderAllFaces=false;
    public BlockPosition pos;
    public Block block;
    public ItemStack itemStack;
    public int meta;
    public dummyWorldBlock(BlockPosition pos, Block block, IDummyWorldAnimes... animes){
        this(pos,block,0,animes);
    }
    public dummyWorldBlock(BlockPosition pos, Block block) {
        this(pos,block,0);
    }
    public dummyWorldBlock(BlockPosition pos, ItemStack itemStack, IDummyWorldAnimes... animes){
        this.pos=pos;
        this.itemStack =itemStack;
        Collections.addAll(WorldAnimeList,animes);
    }
    public dummyWorldBlock(BlockPosition pos, Block block, int meta, IDummyWorldAnimes... animes){
        this.pos=pos;
        this.block=block;
        this.meta=meta;
        Collections.addAll(WorldAnimeList,animes);
    }
    public dummyWorldBlock setRenderAllFace(boolean renderAllFace){this.renderAllFaces=renderAllFace;return this;}


    @Override
    public ArrayList<IGuiAnime> getGuiAnimeList() {
        return null;
    }
    @Override
    public ArrayList<IDummyWorldAnimes> getWorldAnimeList() {
        return WorldAnimeList;
    }
    @Override
    public BlockPosition getPos(){
        return pos;
    }
    @Override
    public void render(DummyWorld world, long initTime, BlockPosition mousePointingPos) {
        GL11.glPushMatrix();
        RenderHelper.disableStandardItemLighting();
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
        Tessellator.instance.startDrawingQuads();
        try {
            Tessellator.instance.setBrightness(15 << 20 | 15 << 4);
            if (block.equals(Blocks.air)) return;
            RenderBlocks bufferBuilder = new RenderBlocks();
            bufferBuilder.blockAccess = world;
            bufferBuilder.setRenderBounds(0, 0, 0, 1, 1, 1);
            bufferBuilder.renderAllFaces = renderAllFaces;
            bufferBuilder.renderBlockByRenderType(block, pos.x, pos.y, pos.z);
        } finally {
            Tessellator.instance.draw();
            Tessellator.instance.setTranslation(0, 0, 0);
            if(pos.equals(mousePointingPos)) DummyWorldGraphicAnimeOutlineGlowth.renderBlockOutlineAt(pos, 0xCCCCCC, 2F);
            GL11.glPopMatrix();
        }

    }

    @Override
    public List<IdummyWorldThinkerObject> syncWithWorld(DummyWorld world) {
        dummyWorldBlock block = ((dummyWorldBlock) this);
        List<IdummyWorldThinkerObject> tmp = new ArrayList<>();
        if (block.itemStack != null) {
            block.itemStack.copy().tryPlaceItemIntoWorld((EntityPlayer) Minecraft.getMinecraft().thePlayer, world, pos.x, pos.y, pos.z, 0, 0, 0, 0);
            block.block = world.getBlock(pos.x, pos.y, pos.z);
            if (block.block == null || block.block == Blocks.air) {
                Thinker.err("Invalid Block Created From Item!" + block.itemStack.getDisplayName());
                block.block = Blocks.air;
            }
            if (world.getTileEntity(pos.x, pos.y, pos.z) != null)
                tmp.add(new dummyWorldTile(pos,world.getTileEntity(pos.x, pos.y, pos.z), block.WorldAnimeList));
        } else world.setBlock(pos.x, pos.y, pos.z, block.block);
        if (!block.block.hasTileEntity(block.meta)) return tmp;
        TileEntity tileEntity = block.block.createTileEntity(world, block.meta);
        if (tileEntity != null)
            tmp.add( new dummyWorldTile(pos,world.getTileEntity(pos.x, pos.y, pos.z), block.WorldAnimeList));
        return tmp;
    }

    //JsonReader
    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result= values.containsKey("posX")&& values.containsKey("posY")&& values.containsKey("posZ")&&
                ((values.containsKey("block") && values.containsKey("meta")) || values.containsKey("fromItem"));//block+meta or fromItem
        if(!result) thinkerJsonReader.requestLogError("Not Enough contents for dummyWorldBlock: posX, posY, posZ, (block, meta) or (fromItem) ");
        return result;
    }
    public static dummyWorldBlock create(Map<String, Object> values) {
        boolean renderAllFaces = false;
        if(values.containsKey("renderAllFaces"))renderAllFaces = getBoolean(values.get("renderAllFaces"));

        BlockPosition pos = new BlockPosition(getInt(values.get("posX")),getInt(values.get("posY")),getInt(values.get("posZ")));

        if(values.containsKey("fromItem"))return new dummyWorldBlock(pos,getItemStack((String)values.get("fromItem"))).setRenderAllFace(renderAllFaces);
        if(values.containsKey("block")&&values.containsKey("meta"))return new dummyWorldBlock(pos,Block.getBlockFromName((String) values.get("block")),getInt(values.get("meta"))).setRenderAllFace(renderAllFaces);
        return null;
    }
}
