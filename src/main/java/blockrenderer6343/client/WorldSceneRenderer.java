package blockrenderer6343.client;

import static cn.kuzuanpa.thinker.client.dummyWorldHandler.dummyWorldObjects;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import blockrenderer6343.world.DummyWorld;
import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.client.render.dummyWorld.*;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.*;
import cn.kuzuanpa.thinker.client.dummyWorldHandler;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;


import blockrenderer6343.api.utils.BlockPosition;
import blockrenderer6343.api.utils.Position;
import blockrenderer6343.api.utils.PositionedRect;
import blockrenderer6343.api.utils.Size;
import blockrenderer6343.api.utils.ProjectionUtils;
import blockrenderer6343.world.TrackedDummyWorld;
import codechicken.lib.vec.Vector3;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: KilaBash, backported by Quarri6343
 * @Date: 2021/08/23
 * @Description: Abstract class, and extend a lot of features compared with the original one.
 */
public abstract class WorldSceneRenderer {
    public BlockPosition pointedBlock;

    public final DummyWorld world;
    private Consumer<WorldSceneRenderer> beforeRender;
    private Consumer<WorldSceneRenderer> onRender;
    private Consumer<MovingObjectPosition> onLookingAt;
    private int clearColor;
    private MovingObjectPosition lastTraceResult;
    private Vector3f eyePos = new Vector3f(0, 0, 0);
    private Vector3f lookAt = new Vector3f(0, 0, 0);
    private Vector3f worldUp = new Vector3f(0, 1, 0);
    public long initTime=0;

    public WorldSceneRenderer(DummyWorld world) {
        this.world = world;
    }

    public WorldSceneRenderer setBeforeWorldRender(Consumer<WorldSceneRenderer> callback) {
        this.beforeRender = callback;
        return this;
    }

    public WorldSceneRenderer setOnWorldRender(Consumer<WorldSceneRenderer> callback) {
        this.onRender = callback;
        return this;
    }

    public WorldSceneRenderer setOnLookingAt(Consumer<MovingObjectPosition> onLookingAt) {
        this.onLookingAt = onLookingAt;
        return this;
    }


    public void setClearColor(int clearColor) {
        this.clearColor = clearColor;
    }

    public MovingObjectPosition getLastTraceResult() {
        return lastTraceResult;
    }


    /**
     * Renders scene on given coordinates with given width and height, and RGB background color Note that this will
     * ignore any transformations applied currently to projection/view matrix, so specified coordinates are scaled MC
     * gui coordinates. It will return matrices of projection and view in previous state after rendering
     */
    public void render(int x, int y, int width, int height, int mouseX, int mouseY, List<IGuiAnime> animeList) {
        PositionedRect positionedRect = getPositionedRect(x, y, width, height);
        PositionedRect mouse = getPositionedRect(mouseX, mouseY, 0, 0);
        mouseX = mouse.position.x;
        mouseY = mouse.position.y;

        GL11.glPushMatrix();
        // setupCamera
        setupCamera(positionedRect);

        // render TrackedDummyWorld
        animeList.forEach(anime -> anime.animeDraw(initTime));
        drawWorld();
        // check lookingAt
        this.lastTraceResult = null;
        if (onLookingAt != null) {
            int mouseX1=mouseX;
            int mouseY1=mouseY;
            dummyWorldHandler.dummyWorldObjects.forEach((blockDummy)->{
                GL11.glPushMatrix();
                if(!blockDummy.getWorldAnimeList().isEmpty())blockDummy.getWorldAnimeList().forEach(gAnime-> {
                    if(!(gAnime instanceof IDummyWorldGraphicAnime))return;
                    BlockPosition pos = blockDummy.getPos();
                    GL11.glTranslatef(pos.x,pos.y,pos.z);
                    ((IDummyWorldGraphicAnime)gAnime).animeDraw(initTime);
                    GL11.glTranslatef(-pos.x,-pos.y,-pos.z);
                });
                Vector3f hitPos = ProjectionUtils.unProject(mouseX1, mouseY1);
                GL11.glPopMatrix();
                MovingObjectPosition result = rayTrace(hitPos,blockDummy.getPos());
                if (result != null) {
                    this.lastTraceResult = result;
                    onLookingAt.accept(result);
                }
            });
        }

        // resetcamera
        resetCamera();
        GL11.glPopMatrix();
    }

    public Vector3f getEyePos() {
        return eyePos;
    }

    public Vector3f getLookAt() {
        return lookAt;
    }

    public Vector3f getWorldUp() {
        return worldUp;
    }

    public void setCameraLookAt(Vector3f eyePos, Vector3f lookAt, Vector3f worldUp) {
        this.eyePos = eyePos;
        this.lookAt = lookAt;
        this.worldUp = worldUp;
    }

    public void sync(){
        while(world.lock) {try{wait(0,1000);}catch (Exception ignored){}}
        List<IdummyWorldThinkerObject> tmp = new ArrayList<>();
        dummyWorldHandler.dummyWorldObjects.forEach((obj) -> {
            BlockPosition pos = obj.getPos();
            if(obj instanceof dummyWorldBlock) {
                dummyWorldBlock block = ((dummyWorldBlock) obj);
                if (block.itemStack != null) {
                    block.itemStack.tryPlaceItemIntoWorld((EntityPlayer) Minecraft.getMinecraft().thePlayer, world, pos.x, pos.y, pos.z, 0, 0, 0, 0);
                    block.block = world.getBlock(pos.x, pos.y, pos.z);
                    if (block.block == null || block.block == Blocks.air) {
                        Thinker.err("Invalid Block Created From Item!" + block.itemStack.getDisplayName());
                        block.block = Blocks.air;
                    }
                    if (world.getTileEntity(pos.x, pos.y, pos.z) != null)
                        tmp.add(new dummyWorldTile(pos,world.getTileEntity(pos.x, pos.y, pos.z), block.WorldAnimeList));
                } else world.setBlock(pos.x, pos.y, pos.z, block.block);
                if (!block.block.hasTileEntity(block.meta)) return;
                TileEntity tileEntity = block.block.createTileEntity(world, block.meta);
                if (tileEntity != null)
                    tmp.add( new dummyWorldTile(pos,world.getTileEntity(pos.x, pos.y, pos.z), block.WorldAnimeList));
            }else if(obj instanceof dummyWorldTile) {
                dummyWorldTile tile = ((dummyWorldTile) obj);
                world.setTileEntity(pos.x, pos.y, pos.z, tile.tile);
                if (tile.tile.blockType != null) world.setBlock(pos.x, pos.y, pos.z, tile.tile.blockType);
            }
        });
        dummyWorldObjects.addAll(tmp);
        if(world instanceof TrackedDummyWorld)((TrackedDummyWorld) world).onProfileChanged();
    }
    public void setCameraLookAt(Vector3f lookAt, double radius, double rotationPitch, double rotationYaw) {
        this.lookAt = lookAt;
        Vector3 vecX = new Vector3(Math.cos(rotationPitch), 0, Math.sin(rotationPitch));
        Vector3 vecY = new Vector3(0, Math.tan(rotationYaw) * vecX.mag(), 0);
        Vector3 pos = vecX.copy().add(vecY).normalize().multiply(radius);
        this.eyePos = pos.add(lookAt.x, lookAt.y, lookAt.z).vector3f();
    }

    protected PositionedRect getPositionedRect(int x, int y, int width, int height) {
        return new PositionedRect(new Position(x, y), new Size(width, height));
    }

    public void setupCamera(PositionedRect positionedRect) {
        int x = positionedRect.getPosition().x;
        int y = positionedRect.getPosition().y;
        int width = positionedRect.getSize().width;
        int height = positionedRect.getSize().height;

        Minecraft mc = Minecraft.getMinecraft();
        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glPushClientAttrib(GL_ALL_CLIENT_ATTRIB_BITS);
        mc.entityRenderer.disableLightmap(0);
        glDisable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);


        // setup viewport and clear GL buffers
        glViewport(x, y, width, height);

        glClear(GL_DEPTH_BUFFER_BIT);

        // setup projection matrix to perspective
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();

        float aspectRatio = width / (height * 1.0f);
        GLU.gluPerspective(60.0f, aspectRatio, 0.1f, 10000.0f);

        // setup modelview matrix
        glMatrixMode(GL_MODELVIEW);

        glPushMatrix();
        glLoadIdentity();
        GLU.gluLookAt(eyePos.x, eyePos.y, eyePos.z, lookAt.x, lookAt.y, lookAt.z, worldUp.x, worldUp.y, worldUp.z);
    }

    public static void resetCamera() {
        // reset viewport
        Minecraft minecraft = Minecraft.getMinecraft();
        glViewport(0, 0, minecraft.displayWidth, minecraft.displayHeight);

        // reset modelview matrix
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();

        // reset projection matrix
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();

        glMatrixMode(GL_MODELVIEW);

        // reset attributes
        glPopClientAttrib();
        glPopAttrib();
    }


    protected void drawWorld() {
        if (beforeRender != null) {
            beforeRender.accept(this);
        }
        Minecraft mc = Minecraft.getMinecraft();
        glEnable(GL_CULL_FACE);
        glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        mc.entityRenderer.disableLightmap(0);
        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        glDisable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_ALPHA_TEST);
        glEnable(GL_DEPTH_TEST);

        final int savedAo = mc.gameSettings.ambientOcclusion;
        RenderHelper.enableStandardItemLighting();
        glEnable(GL_LIGHTING);
        mc.gameSettings.ambientOcclusion = 0;
        dummyWorldObjects.forEach((obj)->{
            try{
                GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
                obj.render(world, initTime,false);
                GL11.glPopAttrib();
            }catch (Exception e){Thinker.err(e);}
        });

        mc.gameSettings.ambientOcclusion = savedAo;
        ForgeHooksClient.setRenderPass(-1);
        glDisable(GL_BLEND);
        glDepthMask(true);
    }

    public static void setDefaultPassRenderState(int pass) {
        glColor4f(1, 1, 1, 1);
        if (pass == 0) { // SOLID
            glEnable(GL_DEPTH_TEST);
            glDisable(GL_BLEND);
            glDepthMask(true);
        } else { // TRANSLUCENT
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glDepthMask(false);
        }
    }

    public MovingObjectPosition rayTrace(Vector3f hitPos,BlockPosition pos) {
        Vec3 startPos = Vec3.createVectorHelper(this.eyePos.x, this.eyePos.y, this.eyePos.z);
        hitPos.scale(2); // Double view range to ensure pos can be seen.
        Vec3 endPos = Vec3.createVectorHelper(
                (hitPos.x - startPos.xCoord),
                (hitPos.y - startPos.yCoord),
                (hitPos.z - startPos.zCoord));

        return ((TrackedDummyWorld) this.world).rayTraceBlockswithTargetMap(startPos, endPos,dummyWorldHandler.dummyWorldObjects.stream().map(IdummyWorldThinkerObject::getPos).collect(Collectors.toSet()), pos);
    }

    /***
     * For better performance, You'd better handle the event setOnLookingAt(Consumer) or getLastTraceResult()
     *
     * @param mouseX xPos in Texture
     * @param mouseY yPos in Texture
     * @return RayTraceResult Hit
     */
    protected MovingObjectPosition screenPos2BlockPosFace(int mouseX, int mouseY, int x, int y, int width, int height) {
        // render a frame
        glEnable(GL_DEPTH_TEST);
        setupCamera(getPositionedRect(x, y, width, height));

        drawWorld();

        AtomicReference<MovingObjectPosition> result = new AtomicReference<>();
        dummyWorldHandler.dummyWorldObjects.forEach((blockDummy)->{
            BlockPosition pos = blockDummy.getPos();
            GL11.glPushMatrix();
            if(!blockDummy.getWorldAnimeList().isEmpty())blockDummy.getWorldAnimeList().forEach(gAnime->{
                if(!(gAnime instanceof IDummyWorldGraphicAnime))return;
                GL11.glTranslatef(pos.x, pos.y, pos.z);
                ((IDummyWorldGraphicAnime)gAnime).animeDraw(initTime);
                GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
            });
            Vector3f hitPos = ProjectionUtils.unProject(mouseX, mouseY);
            result.set(rayTrace(hitPos, pos));

            GL11.glPopMatrix();
        });

        resetCamera();
        return result.get();
    }

    /***
     * For better performance, You'd better do project in setOnWorldRender(Consumer)
     *
     * @param pos   BlockPos
     * @param depth should pass Depth Test
     * @return x, y, z
     */
    protected Vector3f blockPos2ScreenPos(BlockPosition pos, boolean depth, int x, int y, int width, int height) {
        // render a frame
        glEnable(GL_DEPTH_TEST);
        setupCamera(getPositionedRect(x, y, width, height));

        drawWorld();
        Vector3f winPos = ProjectionUtils.project(pos);

        resetCamera();

        return winPos;
    }
}
