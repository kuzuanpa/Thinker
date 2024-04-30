package blockrenderer6343.world;

import java.util.*;

import blockrenderer6343.api.utils.BlockPosition;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.chunk.Chunk;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TrackedDummyWorld extends DummyWorld {

    public final Set<BlockPosition> placedBlocks = new HashSet<>();

    private Vector3f minPos = new Vector3f(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    private Vector3f maxPos = new Vector3f(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    private boolean isClearingBlocks =false;
    @Override
    public boolean setBlock(int x, int y, int z, Block block, int meta, int flags) {
        if(!isClearingBlocks)if (block == Blocks.air) placedBlocks.remove(new BlockPosition(x, y, z));
                             else placedBlocks.add(new BlockPosition(x, y, z));

        minPos.x = Math.min(minPos.x, x);
        minPos.y = Math.min(minPos.y, y);
        minPos.z = Math.min(minPos.z, z);
        maxPos.x = Math.max(maxPos.x, x);
        maxPos.y = Math.max(maxPos.y, y);
        maxPos.z = Math.max(maxPos.z, z);

        // copy base method to avoid fastcraft ASM
        if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000) {
            if (y < 0) {
                return false;
            } else if (y >= 256) {
                return false;
            } else {
                Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
                Block block1 = null;
                net.minecraftforge.common.util.BlockSnapshot blockSnapshot = null;

                if ((flags & 1) != 0) {
                    block1 = chunk.getBlock(x & 15, y, z & 15);
                }

                if (this.captureBlockSnapshots && !this.isRemote) {
                    blockSnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(this, x, y, z, flags);
                    this.capturedBlockSnapshots.add(blockSnapshot);
                }

                boolean flag = chunk.func_150807_a(x & 15, y, z & 15, block, meta);

                if (!flag && blockSnapshot != null) {
                    this.capturedBlockSnapshots.remove(blockSnapshot);
                    blockSnapshot = null;
                }

                this.theProfiler.startSection("checkLight");
                this.func_147451_t(x, y, z);
                this.theProfiler.endSection();

                if (flag && blockSnapshot == null) // Don't notify clients or update physics while capturing blockstates
                {
                    // Modularize client and physic updates
                    this.markAndNotifyBlock(x, y, z, chunk, block1, block, flags);
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return super.getBlock(x, y, z);
    }

    /**
     * Enable fullbright rendering
     */
    @SideOnly(Side.CLIENT)
    @Override
    public int getLightBrightnessForSkyBlocks(int p_72802_1_, int p_72802_2_, int p_72802_3_, int p_72802_4_) {
        return 15 << 20 | 15 << 4;
    }

    public Vector3f getSize() {
        Vector3f result = new Vector3f();
        result.x = maxPos.x - minPos.x + 1;
        result.y = maxPos.y - minPos.y + 1;
        result.z = maxPos.z - minPos.z + 1;
        return result;
    }

    public void onProfileChanged(){
        int minX=Integer.MAX_VALUE,minY=Integer.MAX_VALUE,minZ=Integer.MAX_VALUE;
        int maxX=Integer.MIN_VALUE,maxY=Integer.MIN_VALUE,maxZ=Integer.MIN_VALUE;
        for (BlockPosition placedBlock : this.placedBlocks) {
            minX=Math.min(minX,placedBlock.x);
            minY=Math.min(minY,placedBlock.y);
            minZ=Math.min(minZ,placedBlock.z);
            maxX=Math.max(maxX,placedBlock.x);
            maxY=Math.max(maxY,placedBlock.y);
            maxZ=Math.max(maxZ,placedBlock.z);
        }
        minPos=new Vector3f(minX-10,minY-10,minZ-10);
        maxPos=new Vector3f(maxX+10,maxY+10,maxZ+10);
    }
    public Vector3f getMinPos() {
        return minPos;
    }

    public Vector3f getMaxPos() {
        return maxPos;
    }

    public boolean isPosInWorld(double x,double y,double z){return x>minPos.x&&y>minPos.y&&z>minPos.z&&x<maxPos.x&&y<maxPos.y&&z<maxPos.z;}
    public MovingObjectPosition rayTraceBlockswithTargetMap(Vec3 start, Vec3 end,Set<BlockPosition> allBlocks, BlockPosition targetedBlocks) {
        return rayTraceBlockswithTargetMap(start, end,allBlocks, targetedBlocks, false, false, false);
    }

    public MovingObjectPosition rayTraceBlockswithTargetMap(Vec3 start, Vec3 end,Set<BlockPosition> allBlocks, BlockPosition targetedBlock,
            boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        if (!Double.isNaN(start.xCoord) && !Double.isNaN(start.yCoord) && !Double.isNaN(start.zCoord)) {
            if (!Double.isNaN(end.xCoord) && !Double.isNaN(end.yCoord) && !Double.isNaN(end.zCoord)) {
                if(!isPosInWorld(end.xCoord,end.yCoord,end.zCoord))return null;
                int i = MathHelper.floor_double(end.xCoord);
                int j = MathHelper.floor_double(end.yCoord);
                int k = MathHelper.floor_double(end.zCoord);
                int l = MathHelper.floor_double(start.xCoord);
                int i1 = MathHelper.floor_double(start.yCoord);
                int j1 = MathHelper.floor_double(start.zCoord);
                Block block = this.getBlock(l, i1, j1);
                int k1 = this.getBlockMetadata(l, i1, j1);

                if ((!ignoreBlockWithoutBoundingBox || block.getCollisionBoundingBoxFromPool(this, l, i1, j1) != null)
                        && block.canCollideCheck(k1, stopOnLiquid)) {
                    MovingObjectPosition movingobjectposition = block.collisionRayTrace(this, l, i1, j1, start, end);

                    if (movingobjectposition != null && isBlockTargeted(movingobjectposition, targetedBlock)) {
                        return movingobjectposition;
                    }
                }
                MovingObjectPosition movingobjectposition2 = null;
                k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(start.xCoord) || Double.isNaN(start.yCoord) || Double.isNaN(start.zCoord)) {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k) {
                        return returnLastUncollidableBlock ? movingobjectposition2 : null;
                    }

                    boolean flag6 = true;
                    boolean flag3 = true;
                    boolean flag4 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l) {
                        d0 = (double) l + 1.0D;
                    } else if (i < l) {
                        d0 = (double) l + 0.0D;
                    } else {
                        flag6 = false;
                    }

                    if (j > i1) {
                        d1 = (double) i1 + 1.0D;
                    } else if (j < i1) {
                        d1 = (double) i1 + 0.0D;
                    } else {
                        flag3 = false;
                    }

                    if (k > j1) {
                        d2 = (double) j1 + 1.0D;
                    } else if (k < j1) {
                        d2 = (double) j1 + 0.0D;
                    } else {
                        flag4 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = end.xCoord - start.xCoord;
                    double d7 = end.yCoord - start.yCoord;
                    double d8 = end.zCoord - start.zCoord;

                    if (flag6) {
                        d3 = (d0 - start.xCoord) / d6;
                    }

                    if (flag3) {
                        d4 = (d1 - start.yCoord) / d7;
                    }

                    if (flag4) {
                        d5 = (d2 - start.zCoord) / d8;
                    }

                    boolean flag5 = false;
                    byte b0;

                    if (d3 < d4 && d3 < d5) {
                        if (i > l) {
                            b0 = 4;
                        } else {
                            b0 = 5;
                        }

                        start.xCoord = d0;
                        start.yCoord += d7 * d3;
                        start.zCoord += d8 * d3;
                    } else if (d4 < d5) {
                        if (j > i1) {
                            b0 = 0;
                        } else {
                            b0 = 1;
                        }

                        start.xCoord += d6 * d4;
                        start.yCoord = d1;
                        start.zCoord += d8 * d4;
                    } else {
                        if (k > j1) {
                            b0 = 2;
                        } else {
                            b0 = 3;
                        }

                        start.xCoord += d6 * d5;
                        start.yCoord += d7 * d5;
                        start.zCoord = d2;
                    }

                    Vec3 vec32 = Vec3.createVectorHelper(start.xCoord, start.yCoord, start.zCoord);
                    l = (int) (vec32.xCoord = MathHelper.floor_double(start.xCoord));

                    if (b0 == 5) {
                        --l;
                        ++vec32.xCoord;
                    }

                    i1 = (int) (vec32.yCoord = MathHelper.floor_double(start.yCoord));

                    if (b0 == 1) {
                        --i1;
                        ++vec32.yCoord;
                    }

                    j1 = (int) (vec32.zCoord = MathHelper.floor_double(start.zCoord));

                    if (b0 == 3) {
                        --j1;
                        ++vec32.zCoord;
                    }

                    Block block1 = this.getBlock(l, i1, j1);
                    int l1 = this.getBlockMetadata(l, i1, j1);

                    if (!ignoreBlockWithoutBoundingBox || block1.getCollisionBoundingBoxFromPool(this, l, i1, j1) != null) {
                        if (block1.canCollideCheck(l1, stopOnLiquid)) {
                            MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(this, l, i1, j1, start, end);
                            if (movingobjectposition1 != null && isBlockTargeted2(movingobjectposition1, allBlocks)) {
                                if(isBlockTargeted(movingobjectposition1,targetedBlock))return movingobjectposition1;
                                else break;
                            }
                        } else {
                            movingobjectposition2 = new MovingObjectPosition(l, i1, j1, b0, start, false);
                        }
                    }
                }

                return returnLastUncollidableBlock ? movingobjectposition2 : null;
            }
            return null;
        } else {
            return null;
        }
    }

    private boolean isBlockTargeted(MovingObjectPosition result, BlockPosition pos) {
        return pos.equals(new BlockPosition(result.blockX, result.blockY, result.blockZ));
    }
    private boolean isBlockTargeted2(MovingObjectPosition result, Set<BlockPosition> pos) {
        return pos.contains(new BlockPosition(result.blockX, result.blockY, result.blockZ));
    }
    public void clearBlocks(){
        this.isClearingBlocks=true;
        this.placedBlocks.forEach(pos->this.setBlockToAir(pos.x,pos.y,pos.z));
        this.isClearingBlocks=false;
        this.placedBlocks.clear();
    }
}
