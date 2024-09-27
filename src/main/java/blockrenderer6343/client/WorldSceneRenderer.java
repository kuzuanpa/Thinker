package blockrenderer6343.client;

import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import blockrenderer6343.world.DummyWorld;
import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.*;
import cn.kuzuanpa.thinker.client.handler.dummyWorldHandler;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic.IDummyWorldGraphicAnime;
import cn.kuzuanpa.thinker.client.objects.gui.anime.IGuiAnime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
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
        animeList.forEach(anime -> anime.animeDraw(world.timer));
        drawWorld();
        // check lookingAt
        this.lastTraceResult = null;

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

    public boolean sync(){
        if(world.lock) return false;
        if(world instanceof TrackedDummyWorld)((TrackedDummyWorld) world).clearBlocks();
        if(world instanceof TrackedDummyWorld)((TrackedDummyWorld) world).onProfileChanged();
        return true;
    }

    public void onTick(){
        world.lock=true;
        List<IdummyWorldThinkerObject> tmp = new ArrayList<>();
        List<IdummyWorldThinkerObject> tmp1 = new ArrayList<>();
        dummyWorldHandler.getDummyWorldObjects("WorldSceneRenderer.onTick:147").forEach((obj) -> {
            if(obj.shouldInWorld(world.timer)){if(!obj.alreadyInWorld())tmp.addAll(obj.addToWorld(world));
            }else if(obj.alreadyInWorld())tmp1.addAll(obj.removeFromWorld(world));
        });
        dummyWorldHandler.getDummyWorldObjects("WorldSceneRenderer.onTick:151").addAll( tmp);
        dummyWorldHandler.getDummyWorldObjects("WorldSceneRenderer.onTick:152").removeAll( tmp1);
        world.lock=false;
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

    public void onMouseMoved(int x,int y){

        glSelectBuffer(selectBuffer);
        glEnable(GL_DEPTH_TEST);
        glRenderMode(GL_SELECT);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
        int[] viewportArray = new int[4];
        viewport.get(viewportArray);


        GL11.glMatrixMode(GL_PROJECTION);
        Minecraft mc =Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        // compute window size from scaled width & height
        int scaleMultiplier = (int) (mc.displayWidth / (resolution.getScaledWidth() * 1.0));
        int scale1=1024/ (scaleMultiplier);
        glScalef(scale1,scale1,1);
        glTranslatef(-(Mouse.getX()- (mc.displayWidth/2F)) / (float)scale1,-(Mouse.getY()- (mc.displayHeight/2F))/(float)scale1,0);

        glMatrixMode(GL_MODELVIEW);

        // 然后重绘场景，这次是在选择模式下

        renderSceneForPicking();
    }

    void renderSceneForPicking() {

        GL11.glInitNames(); // 初始化名称堆栈


        int i=0;
        try {
            Minecraft mc = Minecraft.getMinecraft();
            glEnable(GL_CULL_FACE);
            glEnable(GL12.GL_RESCALE_NORMAL);
            mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_ALPHA_TEST);
            glInitNames();
            glPushName(-1);

            for (IdummyWorldThinkerObject obj : dummyWorldHandler.getDummyWorldObjects("WorldSceneRenderer.renderSceneForPicking:266")) {
                try {

                    glEnable(GL_DEPTH_TEST);
                    glClear(GL_DEPTH_BUFFER_BIT);

                    glLoadName(i++);
                    glPushAttrib(GL_ALL_ATTRIB_BITS);
                    obj.render(world, world.timer, null);
                    glPopAttrib();

                } catch (Exception e) {
                    Thinker.err(e);
                }
            }
            glPopName();
            ForgeHooksClient.setRenderPass(-1);
            glDisable(GL_BLEND);
            glDepthMask(true);
            glFlush();
        }catch (Exception e){e.printStackTrace();}

        {

            int hits = GL11.glRenderMode(GL11.GL_RENDER); // 返回到正常渲染模式，并获取命中数

            if (hits > 0) {
                System.out.println("Selected object num: " + hits);

                int offset = 0;

                while (offset < hits * 4) { // 每个命中有4个值：名字堆栈深度、最接近的、最远的、对象ID

                    int name = selectBuffer.get(offset + 3); // 最后一个值是对象ID

                    //System.out.println("Selected object ID: " + name);

                    offset += 4;

                }

            } else {

                System.out.println("No object selected.");

            }

        }

    }

    public void setWorldTimer(long timer){
        world.timer=timer;
    }
    protected void drawWorld() {
        if (beforeRender != null) {
            beforeRender.accept(this);
        }

        try {
            Minecraft mc = Minecraft.getMinecraft();
            glEnable(GL_CULL_FACE);
            glEnable(GL12.GL_RESCALE_NORMAL);
            mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_ALPHA_TEST);
            glEnable(GL_DEPTH_TEST);
            world.lock=true;
            dummyWorldHandler.getDummyWorldObjects("WorldSceneRenderer.drawWorld:333").forEach((obj) -> {
                try {
                    if(obj.shouldInWorld(world.timer))obj.render(world, world.timer, null);
                } catch (Exception e) {
                    Thinker.err(e);
                }
            });
            world.lock=false;

            //onMouseMoved(Mouse.getX(),Mouse.getY());

            ForgeHooksClient.setRenderPass(-1);
            glDisable(GL_BLEND);
            glDepthMask(true);

        }catch (Exception e){e.printStackTrace();}
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

        return ((TrackedDummyWorld) this.world).rayTraceBlockswithTargetMap(startPos, endPos,dummyWorldHandler.getDummyWorldObjects("WorldSceneRenderer.rayTrace:371").stream().map(IdummyWorldThinkerObject::getPos).collect(Collectors.toSet()), pos);
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
        dummyWorldHandler.getDummyWorldObjects("WorldSceneRenderer.screenPos2BlockPosFace:389").forEach((blockDummy)->{
            BlockPosition pos = blockDummy.getPos();
            GL11.glPushMatrix();
            if(!blockDummy.getWorldAnimeList().isEmpty())blockDummy.getWorldAnimeList().forEach(gAnime->{
                if(!(gAnime instanceof IDummyWorldGraphicAnime))return;
                GL11.glTranslatef(pos.x, pos.y, pos.z);
                ((IDummyWorldGraphicAnime)gAnime).animeDraw(world.timer);
                GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
            });
            Vector3f hitPos = ProjectionUtils.unProject(mouseX, mouseY);
            result.set(rayTrace(hitPos, pos));

            GL11.glPopMatrix();
        });

        resetCamera();
        return result.get();
    }
    private static final IntBuffer selectBuffer = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder())
            .asIntBuffer();
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
