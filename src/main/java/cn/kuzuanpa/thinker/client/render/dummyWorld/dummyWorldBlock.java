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
import cn.kuzuanpa.thinker.util.DummyEntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.*;
import static cn.kuzuanpa.thinker.client.json.thinkerJsonReader.getItemStack;

public class dummyWorldBlock implements IdummyWorldThinkerObject, IAnimatableThinkerObject {
    public final ArrayList<IDummyWorldAnimes> WorldAnimeList = new ArrayList<>();
    public boolean renderAllFaces=false;
    public BlockPosition pos;
    public Block block;
    public ItemStack itemStack;
    public int dummyPlayerX =0, dummyPlayerY =0, dummyPlayerZ =0;
    public float dummyPlayerCarmeaPitch =0, dummyPlayerCarmeaYaw =0;
    public int meta;
    public dummyWorldBlock(BlockPosition pos, Block block, IDummyWorldAnimes... animes){
        this(pos,block,0,animes);
    }
    public dummyWorldBlock(BlockPosition pos, Block block) {
        this(pos,block,0);
    }
    public dummyWorldBlock(BlockPosition pos, ItemStack itemStack, int dummyPlayerX, int dummyPlayerY, int dummyPlayerZ,float dummyPlayerCarmeaPitch,float dummyPlayerCarmeaYaw, IDummyWorldAnimes... animes){
        this.pos=pos;
        this.itemStack =itemStack;
        this.dummyPlayerX =dummyPlayerX;
        this.dummyPlayerY =dummyPlayerY;
        this.dummyPlayerZ =dummyPlayerZ;
        this.dummyPlayerCarmeaPitch=dummyPlayerCarmeaPitch;
        this.dummyPlayerCarmeaYaw=dummyPlayerCarmeaYaw;
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
            block.itemStack.copy().tryPlaceItemIntoWorld(new DummyEntityPlayer(world).setPos(dummyPlayerX,dummyPlayerY,dummyPlayerZ).setFacing(dummyPlayerCarmeaPitch,dummyPlayerCarmeaYaw), world, pos.x, pos.y, pos.z, 0, 0, 0, 0);
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
        int dummyPlayerX=0,dummyPlayerY=0,dummyPlayerZ=0;
        float dummyPlayerPitch=0,dummyPlayerYaw=0;
        if(values.containsKey("renderAllFaces"))renderAllFaces = getBoolean(values.get("renderAllFaces"));

        if(values.containsKey("dummyPlayerX")) dummyPlayerX = getInt(values.get("dummyPlayerX"));
        if(values.containsKey("dummyPlayerY")) dummyPlayerY = getInt(values.get("dummyPlayerY"));
        if(values.containsKey("dummyPlayerZ")) dummyPlayerZ = getInt(values.get("dummyPlayerZ"));
        if(values.containsKey("dummyPlayerPitch")) {
            dummyPlayerPitch = getFloat(values.get("dummyPlayerPitch"));
            if(dummyPlayerPitch>90){
                Thinker.log("Too large dummyPlayerPitch, setting to 90");
                dummyPlayerPitch=90;
            }else if(dummyPlayerPitch<-90){
                Thinker.log("Too small dummyPlayerPitch, setting to -90");
                dummyPlayerPitch=-90;
            }
        }
        if(values.containsKey("dummyPlayerYaw")) {
            dummyPlayerYaw = getFloat(values.get("dummyPlayerYaw"));
            if(dummyPlayerYaw>180){
                Thinker.log("Too large dummyPlayerYaw, setting to 180");
                dummyPlayerYaw=180;
            }else if(dummyPlayerYaw<-180){
                Thinker.log("Too small dummyPlayerYaw, setting to -180");
                dummyPlayerYaw=-180;
            }
        }

        BlockPosition pos = new BlockPosition(getInt(values.get("posX")),getInt(values.get("posY")),getInt(values.get("posZ")));

        if(values.containsKey("fromItem"))return new dummyWorldBlock(pos,getItemStack((String)values.get("fromItem")),dummyPlayerX,dummyPlayerY,dummyPlayerZ,dummyPlayerPitch,dummyPlayerYaw).setRenderAllFace(renderAllFaces);
        if(values.containsKey("block")&&values.containsKey("meta"))return new dummyWorldBlock(pos,Block.getBlockFromName((String) values.get("block")),getInt(values.get("meta"))).setRenderAllFace(renderAllFaces);
        return null;
    }
}
