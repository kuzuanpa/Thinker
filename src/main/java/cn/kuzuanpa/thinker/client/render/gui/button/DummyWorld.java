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
import blockrenderer6343.world.TrackedDummyWorld;
import cn.kuzuanpa.thinker.client.profileHandler;
import codechicken.lib.gui.GuiDraw;
import codechicken.lib.math.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import org.lwjgl.util.vector.Vector3f;

public class DummyWorld extends ThinkerButton {
    protected static ImmediateWorldSceneRenderer renderer;
    protected static Vector3f center;
    protected static BlockPosition selectedBlock;
    protected static float rotationYaw;
    protected static float rotationPitch;
    protected static float zoom;
    protected static final float DEFAULT_RANGE_MULTIPLIER = 3.5f;
    protected int lastGuiMouseX,lastGuiMouseY;
    public boolean clickOnOtherButton=false;

    public DummyWorld(int id, int xPos, int yPos, int width, int height){
        super(id, xPos, yPos,width,height,"");
        try {
            initializeSceneRenderer(true);
        }catch (Throwable t){
            t.printStackTrace();
        }
        lastGuiMouseX=0;lastGuiMouseY=0;
    }
    public void onProfileChanged(long initTime){
        if(renderer==null)return;
        renderer.initTime=initTime;
        renderer.sync();
        resetCenter();
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
            } catch (NullPointerException ignored) {
            }
        }

        renderer = new ImmediateWorldSceneRenderer(new TrackedDummyWorld());
        renderer.initTime=System.currentTimeMillis();
        ((blockrenderer6343.world.DummyWorld) renderer.world).updateEntitiesForNEI();
        renderer.setClearColor(0xC6C6C6);

        lookAt.setX((float) (100));

        Vector3f size = ((TrackedDummyWorld) renderer.world).getSize();
        Vector3f minPos = ((TrackedDummyWorld) renderer.world).getMinPos();
        center = new Vector3f((minPos.x + size.x / 2)+100, minPos.y + size.y / 2, minPos.z + size.z / 2);

        renderer.setOnLookingAt(ray -> {});

        renderer.setOnWorldRender(this::onRendererRender);


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
    }
    private void resetCenter() {
        TrackedDummyWorld world = (TrackedDummyWorld) renderer.world;
        Vector3f size = world.getSize();
        Vector3f minPos = world.getMinPos();
        center = new Vector3f((minPos.x + size.x / 2)+100, minPos.y + size.y / 2, minPos.z + size.z / 2);
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

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible&&(profileHandler.selectedProfile==null||!profileHandler.selectedProfile.disableDummyWorldRend))
        {
            try {
                updateHoverState(mouseX,mouseY);
                animeList.forEach(anime -> anime.animeDrawPre(initTime));

                int RECIPE_LAYOUT_X = xPosition;
                int RECIPE_LAYOUT_Y = yPosition;
                int RECIPE_WIDTH = width;
                int sceneHeight = height;

                int guiMouseX = GuiDraw.getMousePosition().x;
                int guiMouseY = GuiDraw.getMousePosition().y;
                renderer.render(
                        RECIPE_LAYOUT_X,
                        RECIPE_LAYOUT_Y,
                        RECIPE_WIDTH,
                        sceneHeight,
                        lastGuiMouseX,
                        lastGuiMouseY,
                        animeList);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                boolean insideView = !clickOnOtherButton
                        && guiMouseX >=  RECIPE_LAYOUT_X && guiMouseY >=  RECIPE_LAYOUT_Y
                        && guiMouseX <  RECIPE_LAYOUT_X + RECIPE_WIDTH
                        && guiMouseY <  RECIPE_LAYOUT_Y + sceneHeight
                        && Mouse.isInsideWindow();
                boolean leftClickHeld = Mouse.isButtonDown(0);
                boolean rightClickHeld = Mouse.isButtonDown(1);
                boolean middleClickHeld = Mouse.isButtonDown(2);
                if (insideView) {
                    MovingObjectPosition rayTraceResult = renderer.getLastTraceResult();

                    if (leftClickHeld) {
                        rotationPitch += guiMouseX - lastGuiMouseX + 360;
                        rotationPitch = rotationPitch % 360;
                        rotationYaw = (float) MathHelper.clip(rotationYaw + (guiMouseY - lastGuiMouseY), -89.9, 89.9);
                    } else if (rightClickHeld) {
                        int mouseDeltaY = guiMouseY - lastGuiMouseY;
                        if (Math.abs(mouseDeltaY) > 1) {
                            zoom = (float) MathHelper.clip(zoom + (mouseDeltaY > 0 ? 0.5 : -0.5), 3, 999);
                        }
                    }else if(middleClickHeld){
                        int mouseDeltaX = guiMouseX - lastGuiMouseX;
                        int mouseDeltaY = guiMouseY - lastGuiMouseY;

                        double rYaw=3.14*rotationYaw/180;
                        double rPitch=3.14*rotationPitch/180;
                        center.x-=(Math.sin(rPitch)*mouseDeltaX+Math.sin(rYaw)*Math.cos(rPitch)*mouseDeltaY)/20F;
                        center.z+=(Math.cos(rPitch)*mouseDeltaX-Math.sin(rYaw)*Math.sin(rPitch)*mouseDeltaY)/20F;
                        center.y+=Math.cos(rYaw)*mouseDeltaY/20F;


                    }
                    renderer.setCameraLookAt(center, zoom, Math.toRadians(rotationPitch), Math.toRadians(rotationYaw));
                    if (!(leftClickHeld || rightClickHeld) && rayTraceResult != null
                            && !renderer.world.isAirBlock(rayTraceResult.blockX, rayTraceResult.blockY, rayTraceResult.blockZ)) {
                        Block block = renderer.world.getBlock(rayTraceResult.blockX, rayTraceResult.blockY, rayTraceResult.blockZ);
                    }
                    if(rayTraceResult!=null)System.out.println(rayTraceResult.blockX+"/"+rayTraceResult.blockY+ rayTraceResult.blockZ);

                }

                lastGuiMouseX = guiMouseX;
                lastGuiMouseY = guiMouseY;
                animeList.forEach(anime -> anime.animeDrawAfter(initTime));
            }catch (Throwable t){
                t.printStackTrace();
            }
        }
    }

}
