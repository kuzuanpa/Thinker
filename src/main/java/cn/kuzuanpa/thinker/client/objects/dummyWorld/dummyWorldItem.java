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
import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import cn.kuzuanpa.thinker.client.objects.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.IDummyWorldAnimes;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.IDummyBlockAnimeDrawAdditionalQuads;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.IDummyWorldGraphicAnime;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.OutlineGlowth;
import cn.kuzuanpa.thinker.client.objects.gui.anime.IGuiAnime;
import cn.kuzuanpa.thinker.util.DummyEntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
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

public class dummyWorldItem implements IdummyWorldThinkerObject, IAnimatableThinkerObject {
    protected static RenderItem itemRender = new RenderItem();
    public final ArrayList<IDummyWorldAnimes> WorldAnimeList = new ArrayList<>();

    public float posX,posY,posZ;
    public ItemStack itemStack;
    public float pitch =0, yaw =0,roll=0;
    public boolean rendering=false;
    public long joinTime,leaveTime;

    public dummyWorldItem(long joinTime,long leaveTime, float posX,float posY,float posZ, ItemStack itemStack, float roll, float pitch, float yaw, IDummyWorldAnimes... animes){
        this.joinTime=joinTime;
        this.leaveTime=leaveTime;
        this.posX=posX;
        this.posY=posY;
        this.posZ=posZ;
        this.roll=roll;
        this.pitch=pitch;
        this.yaw=yaw;
        this.itemStack =itemStack;
        if(animes!=null)Collections.addAll(WorldAnimeList,animes);
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
    public BlockPosition getPos(){
        return new BlockPosition((int) posX, (int) posY, (int) posZ);
    }

    @Override
    public boolean shouldInWorld(long timer) {
        return joinTime<=timer && timer<leaveTime;
    }

    @Override
    public boolean alreadyInWorld() {
        return rendering;
    }

    @Override
    public void render(DummyWorld world, long timer, BlockPosition mousePointingPos) {
        GL11.glPushMatrix();

        RenderHelper.disableStandardItemLighting();
        List<IDummyWorldAnimes> anime = getWorldAnimeList();
        if(anime!=null&&!anime.isEmpty())if(anime.stream().anyMatch(a->{
            boolean result=false;
            if(a instanceof IDummyBlockAnimeDrawAdditionalQuads) ((IDummyBlockAnimeDrawAdditionalQuads) a).drawAdditionalQuads(timer);
            if(a instanceof IDummyWorldGraphicAnime){
                GL11.glTranslatef(posX, posY, posZ);
                result = ((IDummyWorldGraphicAnime)a).animeDraw(timer);
                GL11.glTranslatef(-posX, -posY, -posZ);
            }
            return result;
        })){
            GL11.glPopMatrix();
            return;
        }

       // Tessellator.instance.startDrawingQuads();
        try {
            Minecraft mc = Minecraft.getMinecraft();
            itemRender.setRenderManager(RenderManager.instance);

            EntityItem renderItem = new EntityItem(world); //tileEntityRenderer.worldObj
            renderItem.setAngles(0, 90);
            renderItem.hoverStart = 0f;
            renderItem.setEntityItemStack(itemStack);

            boolean b =RenderManager.instance.options.fancyGraphics;
            RenderManager.instance.options.fancyGraphics = true;
            GL11.glTranslatef(posX, posY, posZ);
            GL11.glRotatef(roll,0,0,1);
            GL11.glRotatef(yaw,0,1,0);
            GL11.glRotatef(pitch,1,0,0);
            itemRender.doRender(renderItem, 0, 0, 0, 0, 0);
            RenderManager.instance.options.fancyGraphics = b;

        } finally {
           // Tessellator.instance.draw();
            if(getPos().equals(mousePointingPos)) OutlineGlowth.renderBlockOutlineAt(getPos(), 0xCCCCCC, 2F);
            GL11.glPopMatrix();
        }

    }

    @Override
    public List<IdummyWorldThinkerObject> addToWorld(DummyWorld world) {
        rendering=true;
        return new ArrayList<>();
    }

    @Override
    public List<IdummyWorldThinkerObject> removeFromWorld(DummyWorld world) {
        rendering=false;
        return Collections.emptyList();
    }

    //JsonReader
    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result= values.containsKey("joinTime")&& values.containsKey("leaveTime")&&
                values.containsKey("posX")&& values.containsKey("posY")&& values.containsKey("posZ")&&
                values.containsKey("item");
        if(!result) thinkerJsonReader.requestLogError("Not Enough contents for dummyWorldBlock: posX, posY, posZ, item");
        return result;
    }
    public static dummyWorldItem create(Map<String, Object> values) {
        float pitch=0,yaw=0,roll=0;

        if(values.containsKey("pitch")) pitch = getFloat(values.get("pitch"));
        if(values.containsKey("yaw")) yaw = getFloat(values.get("yaw"));
        if(values.containsKey("roll")) roll = getFloat(values.get("roll"));

        if(values.containsKey("item"))return new dummyWorldItem(getLong(values.get("joinTime")),getLong(values.get("leaveTime")), getFloat(values.get("posX")),getFloat(values.get("posY")),getFloat(values.get("posZ")) , getItemStack((String)values.get("item")),roll,pitch,yaw);
        return null;
    }
}
