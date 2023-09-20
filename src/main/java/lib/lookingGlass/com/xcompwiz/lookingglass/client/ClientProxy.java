package lib.lookingGlass.com.xcompwiz.lookingglass.client;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;

import lib.lookingGlass.com.xcompwiz.lookingglass.client.render.RenderPortal;
import lib.lookingGlass.com.xcompwiz.lookingglass.core.CommonProxy;
import lib.lookingGlass.com.xcompwiz.lookingglass.entity.EntityPortal;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Our faithful proxy class.  Allows for running code differently dependent on whether we are client- or server-side.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	/**
	 * Run during mod init.
	 */
	@Override
	public void init() {
		// We register the portal renderer here
		Render render;
		render = new RenderPortal();
		render.setRenderManager(RenderManager.instance);
		RenderingRegistry.registerEntityRenderingHandler(EntityPortal.class, render);
	}
}
