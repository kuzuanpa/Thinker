/*
 * This class was created by <kuzuanpa>. It is distributed as
 * part of the kTFRUAddon Mod. Get the Source Code in github:
 * https://github.com/kuzuanpa/kTFRUAddon
 * 
 * kTFRUAddon is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 */
package cn.kuzuanpa.thinker;

import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

public class commonProxy implements IGuiHandler {
    public commonProxy() {
    }
    public void init(FMLInitializationEvent aEvent) {
        NetworkRegistry.INSTANCE.registerGuiHandler(MOD_ID,this);

    }
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch(id) {
            case 0:
                return null;
        }
        return null;
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}

