package blockrenderer6343.client;

import static org.lwjgl.opengl.GL11.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnime;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldHandler;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
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

    // you have to place blocks in the world before use
    public final World world;
    // the Blocks which this renderer needs to render
    public final Set<BlockPosition> renderedBlocks = new HashSet<>();
    private Consumer<WorldSceneRenderer> beforeRender;
    private Consumer<WorldSceneRenderer> onRender;
    private Consumer<MovingObjectPosition> onLookingAt;
    private int clearColor;
    private MovingObjectPosition lastTraceResult;
    private Vector3f eyePos = new Vector3f(0, 0, -10f);
    private Vector3f lookAt = new Vector3f(0, 0, 0);
    private Vector3f worldUp = new Vector3f(0, 1, 0);
    private boolean renderAllFaces = false;
    public long initTime=0;

    public WorldSceneRenderer(World world) {
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

    public WorldSceneRenderer addRenderedBlocks(Collection<BlockPosition> blocks) {
        if (blocks != null) {
            this.renderedBlocks.addAll(blocks);
        }
        return this;
    }

    public WorldSceneRenderer setOnLookingAt(Consumer<MovingObjectPosition> onLookingAt) {
        this.onLookingAt = onLookingAt;
        return this;
    }

    public void setRenderAllFaces(boolean renderAllFaces) {
        this.renderAllFaces = renderAllFaces;
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
        if (onLookingAt != null && mouseX > positionedRect.position.x
                && mouseX < positionedRect.position.x + positionedRect.size.width
                && mouseY > positionedRect.position.y
                && mouseY < positionedRect.position.y + positionedRect.size.height) {
            Vector3f hitPos = ProjectionUtils.unProject(mouseX, mouseY);
            MovingObjectPosition result = rayTrace(hitPos);
            if (result != null) {
                this.lastTraceResult = result;
                onLookingAt.accept(result);
            }
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

        final int savedAo = mc.gameSettings.ambientOcclusion;
        mc.gameSettings.ambientOcclusion = 0;
        for (BlockPosition pos : renderedBlocks) {
            GL11.glPushMatrix();
            Tessellator.instance.startDrawingQuads();
            List<IDummyWorldAnime> anime = dummyWorldHandler.dummyWorldAnimeHashMap.get(pos);
            if(anime!=null&&!anime.isEmpty())anime.forEach(a->a.animeDraw(initTime));
            try {
                Tessellator.instance.setBrightness(15 << 20 | 15 << 4);
                Block block = world.getBlock(pos.x, pos.y, pos.z);
                if (block.equals(Blocks.air)) continue;
                RenderBlocks bufferBuilder = new RenderBlocks();
                bufferBuilder.blockAccess = world;
                bufferBuilder.setRenderBounds(0, 0, 0, 1, 1, 1);
                bufferBuilder.renderAllFaces = renderAllFaces;
                bufferBuilder.renderBlockByRenderType(block, pos.x, pos.y, pos.z);
                if (onRender != null) onRender.accept(this);
            } finally {
                Tessellator.instance.draw();
                Tessellator.instance.setTranslation(0, 0, 0);
                GL11.glPopMatrix();
            }
        }
        mc.gameSettings.ambientOcclusion = savedAo;
        RenderHelper.enableStandardItemLighting();
        glEnable(GL_LIGHTING);

        // render TESR
        for (int pass = 0; pass < 2; pass++) {
            ForgeHooksClient.setRenderPass(pass);
            int finalPass = pass;
            //renderedBlocks.forEach(blockPosition -> {
            //    setDefaultPassRenderState(finalPass);
            //    TileEntity tile = world.getTileEntity(blockPosition.x, blockPosition.y, blockPosition.z);
            //    if (tile != null) {
            //        if (tile.shouldRenderInPass(finalPass)) {
            //        }
            //    }
            //});
            world.loadedTileEntityList.forEach(t->{
                setDefaultPassRenderState(finalPass);
                if(!(t instanceof TileEntity))return;
                TileEntity tile=(TileEntity) (t);
                if(tile.shouldRenderInPass(finalPass)){
                    List<IDummyWorldAnime> anime = dummyWorldHandler.dummyWorldAnimeHashMap.get(new BlockPosition(tile.xCoord,tile.yCoord,tile.zCoord));
                    if(anime!=null&&!anime.isEmpty())anime.forEach(a->a.animeDraw(initTime));
                    int i = world.getLightBrightnessForSkyBlocks(tile.xCoord, tile.yCoord, tile.zCoord, 0);
                    float j = i % 65536;
                    float k = i / 65536;
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,  j,  k);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    TileEntityRendererDispatcher.instance.renderTileEntityAt(tile, (double)tile.xCoord, (double)tile.yCoord, (double)tile.zCoord, 0);
                }
            });
        }
        ForgeHooksClient.setRenderPass(-1);
        glEnable(GL_DEPTH_TEST);
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

    public MovingObjectPosition rayTrace(Vector3f hitPos) {
        Vec3 startPos = Vec3.createVectorHelper(this.eyePos.x, this.eyePos.y, this.eyePos.z);
        hitPos.scale(2); // Double view range to ensure pos can be seen.
        Vec3 endPos = Vec3.createVectorHelper(
                (hitPos.x - startPos.xCoord),
                (hitPos.y - startPos.yCoord),
                (hitPos.z - startPos.zCoord));
        return ((TrackedDummyWorld) this.world).rayTraceBlockswithTargetMap(startPos, endPos, renderedBlocks);
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

        Vector3f hitPos = ProjectionUtils.unProject(mouseX, mouseY);
        MovingObjectPosition result = rayTrace(hitPos);

        resetCamera();

        return result;
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
