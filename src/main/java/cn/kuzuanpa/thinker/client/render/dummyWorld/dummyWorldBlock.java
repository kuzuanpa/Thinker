package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import blockrenderer6343.world.DummyWorld;
import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.api.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.client.dummyWorldHandler;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.*;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.kuzuanpa.thinker.Thinker.getInt;

public class dummyWorldBlock implements IdummyWorldThinkerObject, IAnimatableThinkerObject {
    public BlockPosition pos;
    public dummyWorldBlockContainer block;
    public dummyWorldBlock(BlockPosition pos, dummyWorldBlockContainer block){
        this.pos=pos;
        this.block=block;
    }

    public static boolean doesMapHaveValidContents(Map<String,Object> values) {
        boolean result= values.containsKey("posX")&&
                values.containsKey("posY")&&
                values.containsKey("posZ")&&
                dummyWorldBlockContainer.doesMapHaveValidContents(values);
        if(!result) Thinker.err("Not Enough contents for dummyWorldBlock: posX, posY, posZ, (block, meta) or (fromItem) ");
        return result;
    }

    public static dummyWorldBlock create(Map<String, Object> values) {
        return new dummyWorldBlock(new BlockPosition(getInt(values.get("posX")),getInt(values.get("posY")),getInt(values.get("posZ"))),
                dummyWorldBlockContainer.create(values));
    }

    @Override
    public ArrayList<IGuiAnime> getGuiAnimeList() {
        return null;
    }

    @Override
    public ArrayList<IDummyWorldAnimes> getWorldAnimeList() {
        return block.getWorldAnimeList();
    }

    @Override
    public BlockPosition getPos(){
        return pos;
    }
    @Override
    public void render(DummyWorld world, long initTime, boolean isMousePointed) {
        GL11.glPushMatrix();
        List<IDummyWorldAnimes> anime = block.getWorldAnimeList();
        if(anime!=null&&!anime.isEmpty())anime.forEach(a->{
            if(a instanceof IDummyBlockAnimeDrawAdditionalQuads) ((IDummyBlockAnimeDrawAdditionalQuads) a).drawAdditionalQuads(initTime);
            if(a instanceof IDummyWorldGraphicAnime){
                GL11.glTranslatef(pos.x, pos.y, pos.z);
                ((IDummyWorldGraphicAnime)a).animeDraw(initTime);
                GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
            }
            if(a instanceof IDummyWorldTilePropertiesAnime){
                dummyWorldHandler.dummyWorldObjects.stream().filter(obj -> obj instanceof dummyWorldTile&&obj.getPos()==pos).forEach(tile->((IDummyWorldTilePropertiesAnime) a).doAnime(((dummyWorldTile)tile).tile.tile));
            }
        });
        Tessellator.instance.startDrawingQuads();
        try {
            Tessellator.instance.setBrightness(15 << 20 | 15 << 4);
            if (block.block.equals(Blocks.air)) return;
            RenderBlocks bufferBuilder = new RenderBlocks();
            bufferBuilder.blockAccess = world;
            bufferBuilder.setRenderBounds(0, 0, 0, 1, 1, 1);
            bufferBuilder.renderAllFaces = block.renderAllFaces;
            bufferBuilder.renderBlockByRenderType(block.block, pos.x, pos.y, pos.z);
        } finally {
            Tessellator.instance.draw();
            Tessellator.instance.setTranslation(0, 0, 0);
            if(isMousePointed) DummyWorldGraphicAnimeOutlineGlowth.renderBlockOutlineAt(pos, 0xCCCCCC, 2F);
            GL11.glPopMatrix();
        }

    }
}
