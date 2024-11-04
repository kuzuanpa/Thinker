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
package cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getInt;

public class OutlineGlowth implements IDummyBlockAnimeDrawAdditionalQuads{
    public OutlineGlowth(int startTime, int endTime, BlockPosition pos, int color, float thickness){
        this.startTime=startTime;
        this.endTime=endTime;
        this.pos=pos;
        this.color=color;
        this.thickness=thickness;
    }
    public BlockPosition pos;
    public int startTime,endTime, color;
    public float thickness;
    public static void renderBlockOutlineAt(BlockPosition pos, int color, float thickness) {
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
        renderBlockOutline(axis);

        GL11.glPopMatrix();
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
    /**Copied from Botania**/
    private static void renderBlockOutline(AxisAlignedBB aabb) {
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

    @Override
    public void drawAdditionalQuads(long time) {
        long timer = System.currentTimeMillis()- time;
        if(startTime<timer&&timer<endTime)renderBlockOutlineAt(pos,color,thickness);

    }

    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("startTime")&&
                values.containsKey("endTime")&&
                values.containsKey("posX")&&
                values.containsKey("posY")&&
                values.containsKey("posZ");
        if(!result)
            thinkerJsonReader.requestLogError("Not Enough contents for world.graphic.outlineGlowth: startTime, endTime, posX, posY, posZ");
        return result;
    }

    public static OutlineGlowth create(Map<String, Object> values) {
        int thickness = values.containsKey("thickness")?getInt(values.get("thickness")):1;
        int color = values.containsKey("color")?getInt(values.get("color")):0xffffffff;
        return new OutlineGlowth(getInt(values.get("startTime")),getInt(values.get("endTime")),new BlockPosition(getInt(values.get("dX")),getInt(values.get("dY")),getInt(values.get("dZ"))),color,thickness);
    }
}
