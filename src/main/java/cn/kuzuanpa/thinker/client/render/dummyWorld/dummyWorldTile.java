package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import blockrenderer6343.world.DummyWorld;
import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.api.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.*;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static blockrenderer6343.client.WorldSceneRenderer.setDefaultPassRenderState;
import static cn.kuzuanpa.thinker.Thinker.getInt;

public class dummyWorldTile implements IdummyWorldThinkerObject, IAnimatableThinkerObject {
    public BlockPosition pos;
    public dummyWorldTileEntityContainer tile;
    public dummyWorldTile(BlockPosition pos, dummyWorldTileEntityContainer tile){
        this.pos=pos;
        this.tile=tile;
    }
    public static boolean doesMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("posX")&&
                values.containsKey("posY")&&
                values.containsKey("posZ")&&
                dummyWorldTileEntityContainer.doesMapHaveValidContents(values);
        if(!result) Thinker.err("Not Enough contents for dummyWorldTileEntity: posX, posY, posZ, tileEntityNBT");
        return result;
    }

    public static dummyWorldTile create(Map<String, Object> values) {
        return new dummyWorldTile(new BlockPosition(getInt(values.get("posX")),getInt(values.get("posY")),getInt(values.get("posZ"))),
                dummyWorldTileEntityContainer.create(values));
    }
    @Override
    public ArrayList<IGuiAnime> getGuiAnimeList() {
        return null;
    }

    @Override
    public ArrayList<IDummyWorldAnimes> getWorldAnimeList() {
        return tile.getWorldAnimeList();
    }

    @Override
    public void render(DummyWorld world, long initTime, boolean isMousePointed) {
        for (int pass = 0; pass < 2; pass++) {
            ForgeHooksClient.setRenderPass(pass);
            if (pos == null || tile.tile == null) return;
            GL11.glPushMatrix();
            setDefaultPassRenderState(pass);
            if (tile.tile.shouldRenderInPass(pass)) {
                GL11.glTranslatef(pos.x, pos.y, pos.z);
                List<IDummyWorldAnimes> anime = tile.getWorldAnimeList();
                if (anime != null && !anime.isEmpty()) anime.forEach(a -> {
                    if (a instanceof IDummyBlockAnimeDrawAdditionalQuads)
                        ((IDummyBlockAnimeDrawAdditionalQuads) a).drawAdditionalQuads(initTime);
                    if (a instanceof IDummyWorldGraphicAnime) {
                        GL11.glTranslatef(pos.x, pos.y, pos.z);
                        ((IDummyWorldGraphicAnime) a).animeDraw(initTime);
                        GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
                    }
                    if (a instanceof IDummyWorldTilePropertiesAnime) {
                        ((IDummyWorldTilePropertiesAnime) a).doAnime(tile.tile);
                    }
                });
                GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
                int i = world.getLightBrightnessForSkyBlocks(pos.x, pos.y, pos.z, 0);
                float j = i % 65536;
                float k = i / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                TileEntityRendererDispatcher.instance.renderTileEntityAt(tile.tile, pos.x, pos.y, pos.z, 0);
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
