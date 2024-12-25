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

/*
 * This class was created by <kuzuanpa>. It is distributed as
 * part of the Thinker Mod. Get the Source Code in github:
 * https://github.com/kuzuanpa/Thinker
 *
 * Thinker is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 */

package cn.kuzuanpa.thinker.client.objects.gui;

import blockrenderer6343.api.utils.BlockPosition;
import blockrenderer6343.client.ImmediateWorldSceneRenderer;
import blockrenderer6343.client.WorldSceneRenderer;
import blockrenderer6343.world.TrackedDummyWorld;
import cn.kuzuanpa.thinker.client.anim.gui.setCamera;
import cn.kuzuanpa.thinker.client.handler.dummyWorldHandler;
import cn.kuzuanpa.thinker.client.handler.profileHandler;
import codechicken.lib.gui.GuiDraw;
import codechicken.lib.math.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import org.lwjgl.util.vector.Vector3f;


public class DummyWorldButton extends ThinkerButtonBase{
    protected static ImmediateWorldSceneRenderer renderer;
    protected static Vector3f center;
    protected static BlockPosition selectedBlock;
    protected static float rotationYaw;
    protected static float rotationPitch;
    protected static float zoom;
    protected static final float DEFAULT_RANGE_MULTIPLIER = 3.5f;
    protected int lastGuiMouseX,lastGuiMouseY;
    public boolean clickOnOtherButton=false,worldSynced=false;


    public DummyWorldButton(int id, int xPos, int yPos, int width, int height){
        super(id, xPos, yPos,width,height,"");
        try {
            initializeSceneRenderer(true);
        }catch (Throwable t){
            t.printStackTrace();
        }
        lastGuiMouseX=0;lastGuiMouseY=0;
    }
    public void onProfileChanged(){
        if(renderer==null)return;
        worldSynced=false;
        resetCenter();
    }
    protected void initializeSceneRenderer(boolean resetCamera) {
        Vector3f eyePos = new Vector3f();
        Vector3f lookAt = new Vector3f();
        Vector3f worldUp = new Vector3f();
        if (!resetCamera&&renderer!=null) {
            eyePos = renderer.getEyePos();
            lookAt = renderer.getLookAt();
            worldUp = renderer.getWorldUp();
        }
        dummyWorldHandler.getDummyWorldObjects("DummyWorldButton.initializeSceneRenderer:84").clear();
        renderer = new ImmediateWorldSceneRenderer(new TrackedDummyWorld());
        renderer.setClearColor(0xC6C6C6);

        Vector3f size = ((TrackedDummyWorld) renderer.world).getSize();
        Vector3f minPos = ((TrackedDummyWorld) renderer.world).getMinPos();
        center = new Vector3f((minPos.x + size.x / 2), minPos.y + size.y / 2, minPos.z + size.z / 2);

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
    }
    public void resizeToScreen(int width,int height){
        this.width=width;
        this.height=height;
    }
    private void resetCenter() {
        TrackedDummyWorld world = (TrackedDummyWorld) renderer.world;
        Vector3f size = world.getSize();
        Vector3f minPos = world.getMinPos();
        center = new Vector3f((minPos.x + size.x / 2), minPos.y + size.y / 2, minPos.z + size.z / 2);
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

    public void updateTimer(long timer){
        super.updateTimer(timer);
        renderer.setWorldTimer(timer);
    }

    public void setCameraPitch(float value)
    {
        rotationPitch = (value) % 360;
    }
    public void setCameraYaw(double value)
    {
        rotationYaw = (float) MathHelper.clip(value, -89.9, 89.9);
    }
    public void setCameraZoom(double value)
    {
        zoom = (float) MathHelper.clip(value, 3, 999);
    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!this.visible|| profileHandler.selectedProfile==null || profileHandler.selectedProfile.disableDummyWorldRend)return;
            try {
                if(!worldSynced){
                    worldSynced=renderer.sync();
                    resetCenter();
                }
                if(!worldSynced)return;
                renderer.onTick();
                updateHoverState(mouseX,mouseY);
                profileHandler.selectedProfile.dummyWorldAnime.forEach(anime -> anime.animeDrawPre(timer));

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
                        profileHandler.selectedProfile.dummyWorldAnime);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                profileHandler.selectedProfile.dummyWorldAnime.stream().filter(anime-> anime instanceof setCamera).map(anime-> ((setCamera) anime)).forEach(setter-> {
                    if(setter.time >timer || setter.hasSet)return;
                    if(setter.pitch != Float.MIN_VALUE) setCameraPitch(setter.pitch);
                    if(setter.yaw   != Float.MIN_VALUE) setCameraYaw  (setter.yaw  );
                    if(setter.zoom  != Float.MIN_VALUE) setCameraZoom (setter.zoom );
                    if(setter.x     != Float.MIN_VALUE) center.x      = setter.x    ;
                    if(setter.y     != Float.MIN_VALUE) center.y      = setter.y    ;
                    if(setter.z     != Float.MIN_VALUE) center.z      = setter.z    ;
                    setter.hasSet=true;
                });
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
                        setCameraPitch(rotationPitch + guiMouseX - lastGuiMouseX + 360);
                        setCameraYaw(rotationYaw + (guiMouseY - lastGuiMouseY));
                    } else if (rightClickHeld) {
                        int mouseDeltaY = guiMouseY - lastGuiMouseY;
                        if (Math.abs(mouseDeltaY) > 0.1) {
                            setCameraZoom(zoom + (mouseDeltaY > 0 ? 0.2 : -0.2));
                        }
                    }else if(middleClickHeld){
                        int mouseDeltaX = guiMouseX - lastGuiMouseX;
                        int mouseDeltaY = guiMouseY - lastGuiMouseY;
                        double rYaw=3.1415*rotationYaw/180;
                        double rPitch=3.1415*rotationPitch/180;
                        center.x-=(Math.sin(rPitch)*mouseDeltaX+Math.sin(rYaw)*Math.cos(rPitch)*mouseDeltaY)/(180/zoom);
                        center.z+=(Math.cos(rPitch)*mouseDeltaX-Math.sin(rYaw)*Math.sin(rPitch)*mouseDeltaY)/(180/zoom);
                        center.y+=Math.cos(rYaw)*mouseDeltaY/20F;
                    }
                    renderer.setCameraLookAt(center, zoom, Math.toRadians(rotationPitch), Math.toRadians(rotationYaw));
                    if(rayTraceResult!=null) {
                        renderer.pointedBlock = new BlockPosition(rayTraceResult.blockX, rayTraceResult.blockY, rayTraceResult.blockZ);
                    }else {
                        renderer.pointedBlock = null;
                    }

                }

                lastGuiMouseX = guiMouseX;
                lastGuiMouseY = guiMouseY;
                profileHandler.selectedProfile.dummyWorldAnime.forEach(anime -> anime.animeDrawAfter(timer));
            }catch (Throwable t){
                t.printStackTrace();
            }
    }

}
