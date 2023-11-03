package blockrenderer6343.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import blockrenderer6343.api.utils.PositionedRect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

/**
 * Created with IntelliJ IDEA.
 * 
 * @Author: KilaBash, backported by Quarri6343
 * @Date: 2021/8/24
 * @Description: Real-time rendering renderer. If you need to render scene as a texture, use the FBO
 *               {@link FBOWorldSceneRenderer}.
 */
@SideOnly(Side.CLIENT)
public class ImmediateWorldSceneRenderer extends WorldSceneRenderer {

    public ImmediateWorldSceneRenderer(World world) {
        super(world);
    }

    @Override
    protected PositionedRect getPositionedRect(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        // compute window size from scaled width & height
        int windowWidth = (int) (width / (resolution.getScaledWidth() * 1.0) * mc.displayWidth);
        int windowHeight = (int) (height / (resolution.getScaledHeight() * 1.0) * mc.displayHeight);
        // translate gui coordinates to window's ones (y is inverted)
        int windowX = (int) (x / (resolution.getScaledWidth() * 1.0) * mc.displayWidth);
        int windowY = mc.displayHeight - (int) (y / (resolution.getScaledHeight() * 1.0) * mc.displayHeight)
                - windowHeight;

        return super.getPositionedRect(windowX, windowY, windowWidth, windowHeight);
    }
}
