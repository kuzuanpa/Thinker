/*
 * This class was created by <kuzuanpa>. It is distributed as
 * part of the kTFRUAddon Mod. Get the Source Code in github:
 * https://github.com/kuzuanpa/kTFRUAddon
 *
 * kTFRUAddon is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 */

package cn.kuzuanpa.thinker.client.render.gui.button;

import blockrenderer6343.api.utils.BlockPosition;
import blockrenderer6343.client.ImmediateWorldSceneRenderer;
import blockrenderer6343.client.WorldSceneRenderer;
import blockrenderer6343.world.DummyWorld;
import blockrenderer6343.world.TrackedDummyWorld;
import cn.kuzuanpa.thinker.client.render.gui.ThinkingGui;
import cn.kuzuanpa.thinker.clientProxy;
import codechicken.lib.gui.GuiDraw;
import codechicken.lib.math.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import org.lwjgl.util.vector.Vector3f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class CommonModel extends CommonGuiButton{
    protected static ImmediateWorldSceneRenderer renderer;
    protected static Vector3f center;
    protected static BlockPosition selectedBlock;
    protected static float rotationYaw;
    protected static float rotationPitch;
    protected static float zoom;
    protected static final float DEFAULT_RANGE_MULTIPLIER = 3.5f;
    protected int lastGuiMouseX,lastGuiMouseY;
    public CommonModel(int id, int xPos, int yPos, int width, int height){
        super(id, xPos, yPos,width,height,"");
        try {
            initializeSceneRenderer(true);
        }catch (Throwable t){
            t.printStackTrace();
        }
        lastGuiMouseX=0;lastGuiMouseY=0;

    }
    protected void initializeSceneRenderer(boolean resetCamera) {
        Vector3f eyePos = new Vector3f();
        Vector3f lookAt = new Vector3f();
        Vector3f worldUp = new Vector3f();
        if (!resetCamera) {
            try {
                eyePos = renderer.getEyePos();
                lookAt = renderer.getLookAt();
                worldUp = renderer.getWorldUp();
            } catch (NullPointerException e) {
            }
        }

        renderer = new ImmediateWorldSceneRenderer(new TrackedDummyWorld());
        ((DummyWorld) renderer.world).updateEntitiesForNEI();
        renderer.setClearColor(0xC6C6C6);
        renderer.world.setBlock(0,5,0,Blocks.diamond_block);
        //placeMultiblock();

        Vector3f size = ((TrackedDummyWorld) renderer.world).getSize();
        Vector3f minPos = ((TrackedDummyWorld) renderer.world).getMinPos();
        center = new Vector3f(minPos.x + size.x / 2, minPos.y + size.y / 2, minPos.z + size.z / 2);

        renderer.renderedBlocks.clear();
        renderer.addRenderedBlocks(((TrackedDummyWorld) renderer.world).placedBlocks);
        renderer.setOnLookingAt(ray -> {});

        renderer.setOnWorldRender(this::onRendererRender);
        // world.setRenderFilter(pos -> worldSceneRenderer.renderedBlocksMap.keySet().stream().anyMatch(c ->
        // c.contains(pos)));

        selectedBlock = null;
        if (resetCamera) {
            float max = Math.max(Math.max(Math.max(size.x, size.y), size.z), 1);
            // Compact Series multiblocks compat
            if (size.x >= 30 || size.y >= 30 || size.z >= 30) {
                zoom = (float) (DEFAULT_RANGE_MULTIPLIER * 4 * Math.sqrt(max));
            }
            // Mega Series multiblocks compat
            if (size.x >= 15 && size.y >= 15 && size.z >= 11) {
                zoom = (float) (DEFAULT_RANGE_MULTIPLIER * 2 * Math.sqrt(max));
            } else {
                zoom = (float) (DEFAULT_RANGE_MULTIPLIER * Math.sqrt(max));
            }
            rotationYaw = 20.0f;
            rotationPitch = 50f;
            if (renderer != null) {
                resetCenter();
            }
        } else {
            renderer.setCameraLookAt(eyePos, lookAt, worldUp);
        }
    }
    public void onRendererRender(WorldSceneRenderer renderer) {
        BlockPosition look = renderer.getLastTraceResult() == null ? null
                : new BlockPosition(
                renderer.getLastTraceResult().blockX,
                renderer.getLastTraceResult().blockY,
                renderer.getLastTraceResult().blockZ);
        if (look != null && look.equals(selectedBlock)) {
            renderBlockOverLay(selectedBlock, Blocks.glass.getIcon(0, 6));
            return;
        }
        renderBlockOverLay(look, Blocks.stained_glass.getIcon(0, 7));
        renderBlockOverLay(selectedBlock, Blocks.stained_glass.getIcon(0, 14));
    }
    private void resetCenter() {
        TrackedDummyWorld world = (TrackedDummyWorld) renderer.world;
        Vector3f size = world.getSize();
        Vector3f minPos = world.getMinPos();
        center = new Vector3f(minPos.x + size.x / 2, minPos.y + size.y / 2, minPos.z + size.z / 2);
        renderer.setCameraLookAt(center, zoom, Math.toRadians(rotationPitch), Math.toRadians(rotationYaw));
    }
    private void renderBlockOverLay(BlockPosition pos, IIcon icon) {
        if (pos == null) return;

        RenderBlocks bufferBuilder = new RenderBlocks();
        bufferBuilder.blockAccess = renderer.world;
        bufferBuilder.setRenderBounds(0, 0, 0, 1, 1, 1);
        bufferBuilder.renderAllFaces = true;
        Block block = renderer.world.getBlock(pos.x, pos.y, pos.z);
        bufferBuilder.renderBlockUsingTexture(block, pos.x, pos.y, pos.z, icon);
    }

    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
        if (this.visible)
        {
            try {
                int RECIPE_LAYOUT_X = 8;
                int RECIPE_LAYOUT_Y = 50;
                int RECIPE_WIDTH = 160;
                int sceneHeight = RECIPE_WIDTH - 10;
                int MOUSE_OFFSET_X = 5;
                int MOUSE_OFFSET_Y = 43;

                int guiMouseX = GuiDraw.getMousePosition().x;
                int guiMouseY = GuiDraw.getMousePosition().y;
                ItemStack tooltipBlockStack=null;
                final Map<GuiButton, Runnable> buttons = new HashMap<>();

                // NEI guiLeft
                int k = 23;
                // NEI guiTop
                int l = 8;
                renderer.render(
                        RECIPE_LAYOUT_X + k,
                        RECIPE_LAYOUT_Y + l,
                        RECIPE_WIDTH,
                        sceneHeight,
                        lastGuiMouseX,
                        lastGuiMouseY);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                MovingObjectPosition rayTraceResult = renderer.getLastTraceResult();
                boolean insideView = guiMouseX >= k + RECIPE_LAYOUT_X && guiMouseY >= l + RECIPE_LAYOUT_Y
                        && guiMouseX < k + RECIPE_LAYOUT_X + RECIPE_WIDTH
                        && guiMouseY < l + RECIPE_LAYOUT_Y + sceneHeight;
                boolean leftClickHeld = Mouse.isButtonDown(0);
                boolean rightClickHeld = Mouse.isButtonDown(1);
                if (insideView) {
                    if (leftClickHeld) {
                        rotationPitch += guiMouseX - lastGuiMouseX + 360;
                        rotationPitch = rotationPitch % 360;
                        rotationYaw = (float) MathHelper.clip(rotationYaw + (guiMouseY - lastGuiMouseY), -89.9, 89.9);
                    } else if (rightClickHeld) {
                        int mouseDeltaY = guiMouseY - lastGuiMouseY;
                        if (Math.abs(mouseDeltaY) > 1) {
                            zoom = (float) MathHelper.clip(zoom + (mouseDeltaY > 0 ? 0.5 : -0.5), 3, 999);
                        }
                    }
                    renderer.setCameraLookAt(center, zoom, Math.toRadians(rotationPitch), Math.toRadians(rotationYaw));
                }

                // draw buttons
                for (GuiButton button : buttons.keySet()) {
                    button.drawButton(Minecraft.getMinecraft(), guiMouseX - k - MOUSE_OFFSET_X, guiMouseY - l - MOUSE_OFFSET_Y);
                }

                if (!(leftClickHeld || rightClickHeld) && rayTraceResult != null
                        && !renderer.world.isAirBlock(rayTraceResult.blockX, rayTraceResult.blockY, rayTraceResult.blockZ)) {
                    Block block = renderer.world.getBlock(rayTraceResult.blockX, rayTraceResult.blockY, rayTraceResult.blockZ);
                    tooltipBlockStack = block.getPickBlock(
                            rayTraceResult,
                            renderer.world,
                            rayTraceResult.blockX,
                            rayTraceResult.blockY,
                            rayTraceResult.blockZ,
                            Minecraft.getMinecraft().thePlayer);
                }

                lastGuiMouseX = guiMouseX;
                lastGuiMouseY = guiMouseY;
            }catch (Throwable t){
                t.printStackTrace();
            }
        }
    }

}
