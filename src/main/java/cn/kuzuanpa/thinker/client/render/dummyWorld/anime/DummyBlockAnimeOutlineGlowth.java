package cn.kuzuanpa.thinker.client.render.dummyWorld.anime;

import blockrenderer6343.api.utils.BlockPosition;
import blockrenderer6343.client.WorldSceneRenderer;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class DummyBlockAnimeOutlineGlowth implements IDummyBlockAnime{
    public DummyBlockAnimeOutlineGlowth(int startTime,int endTime,BlockPosition pos,int color,float thickness){
        this.startTime=startTime;
        this.endTime=endTime;
        this.pos=pos;
        this.color=color;
        this.thickness=thickness;
    }
    public BlockPosition pos;
    public int startTime,endTime, color;
    public float thickness;
    @Override
    public void animeDraw(long initTime, WorldSceneRenderer renderer) {
        long timer = System.currentTimeMillis()-initTime;
        if(startTime<timer&&timer<endTime)renderBlockOutlineAt(pos,color,thickness);
    }

    @Override
    public void updateButton(long initTime, WorldSceneRenderer renderer) {
    }
    @Override
    public String jsonName() {
        return "Block.OutlineGlowth";
    }
    public void renderBlockOutlineAt(BlockPosition pos, int color, float thickness) {
        GL11.glPushMatrix();
        GL11.glPushAttrib(2896);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glPushMatrix();
        Color colorRGB = new Color(color);
        GL11.glColor4ub((byte)colorRGB.getRed(), (byte)colorRGB.getGreen(), (byte)colorRGB.getBlue(), (byte)-1);
        AxisAlignedBB axis= AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1);
        GL11.glLineWidth(thickness);
        GL11.glColor4ub((byte)colorRGB.getRed(), (byte)colorRGB.getGreen(), (byte)colorRGB.getBlue(), (byte)255);
        this.renderBlockOutline(axis);

        GL11.glPopMatrix();
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
    /**Copied from Botania**/
    private void renderBlockOutline(AxisAlignedBB aabb) {
        Tessellator tessellator = Tessellator.instance;
        double ix = aabb.minX;
        double iy = aabb.minY;
        double iz = aabb.minZ;
        double ax = aabb.maxX;
        double ay = aabb.maxY;
        double az = aabb.maxZ;
        tessellator.startDrawing(1);
        tessellator.addVertex(ix, iy, iz);
        tessellator.addVertex(ix, ay, iz);
        tessellator.addVertex(ix, ay, iz);
        tessellator.addVertex(ax, ay, iz);
        tessellator.addVertex(ax, ay, iz);
        tessellator.addVertex(ax, iy, iz);
        tessellator.addVertex(ax, iy, iz);
        tessellator.addVertex(ix, iy, iz);
        tessellator.addVertex(ix, iy, az);
        tessellator.addVertex(ix, ay, az);
        tessellator.addVertex(ix, iy, az);
        tessellator.addVertex(ax, iy, az);
        tessellator.addVertex(ax, iy, az);
        tessellator.addVertex(ax, ay, az);
        tessellator.addVertex(ix, ay, az);
        tessellator.addVertex(ax, ay, az);
        tessellator.addVertex(ix, iy, iz);
        tessellator.addVertex(ix, iy, az);
        tessellator.addVertex(ix, ay, iz);
        tessellator.addVertex(ix, ay, az);
        tessellator.addVertex(ax, iy, iz);
        tessellator.addVertex(ax, iy, az);
        tessellator.addVertex(ax, ay, iz);
        tessellator.addVertex(ax, ay, az);
        tessellator.draw();
    }
}
