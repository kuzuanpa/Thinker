/*
 * This class was created by <kuzuanpa>. It is distributed as
 * part of the kTFRUAddon Mod. Get the Source Code in github:
 * https://github.com/kuzuanpa/kTFRUAddon
 * 
 * kTFRUAddon is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 */
package cn.kuzuanpa.thinker;
import cn.kuzuanpa.thinker.client.world.DummyChunkProvider;
import cn.kuzuanpa.thinker.client.world.DummyWorld;
import lib.lookingGlass.com.xcompwiz.lookingglass.api.view.IWorldView;
import lib.lookingGlass.com.xcompwiz.lookingglass.client.proxyworld.ProxyWorldManager;
import lib.lookingGlass.com.xcompwiz.lookingglass.client.proxyworld.WorldView;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import cn.kuzuanpa.thinker.client.render.gui.ThinkingGui;
import cn.kuzuanpa.thinker.client.keyBindHandler;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;

public class clientProxy extends commonProxy {
    public static WorldView mainView;

    public void init(FMLInitializationEvent event) {
        super.init(event);
        ClientRegistry.registerKeyBinding(keyBindHandler.keyThink);
        FMLCommonHandler.instance().bus().register(new keyBindHandler());
    }

    public static WorldView getOrCreateDummyWorldView(@Nullable ChunkCoordinates coord, int width, int height){
        if(mainView==null) mainView = ProxyWorldManager.createWorldView(0,coord, width, height);
        return mainView;
    }
    @SideOnly(Side.CLIENT)
    public void registerRenderers(){
    }
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch(id) {
            case 0:
                return new ThinkingGui();
        }
        return null;
    }

}