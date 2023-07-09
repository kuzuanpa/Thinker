package cn.kuzuanpa.thinker;

import cpw.mods.fml.common.SidedProxy;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = Thinker.MOD_ID, version = Thinker.VERSION)
public class Thinker
{
    public static final String MOD_ID = "thinker";
    public static final String MOD_NAME = "Thinker";
    public static final String VERSION = "0.0.1";
    @SidedProxy(clientSide = "cn.kuzuanpa.thinker.clientProxy",
            serverSide = "cn.kuzuanpa.thinker.commonProxy")
    public static commonProxy PROXY;
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
PROXY.init(event);
    }
}
