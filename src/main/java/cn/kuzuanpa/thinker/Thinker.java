package cn.kuzuanpa.thinker;

import cn.kuzuanpa.thinker.client.json.jsonReader;
import cn.kuzuanpa.thinker.client.render.gui.ThinkingGuiWelcome;
import cn.kuzuanpa.thinker.command.CommandGetTileNBT;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = Thinker.MOD_ID, version = Thinker.VERSION, dependencies = "required-after:CodeChickenCore@[1.0.7,);")
public class Thinker
{
    public static final String MOD_ID = "thinker";
    public static final String MOD_NAME = "Thinker";
    public static final String VERSION = "0.0.1";
    public static int delay = 5;
    public static boolean welcomed=false;
    public Configuration config;
    @SidedProxy(clientSide = "cn.kuzuanpa.thinker.clientProxy",
            serverSide = "cn.kuzuanpa.thinker.commonProxy")
    public static commonProxy PROXY;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        welcomed = !config.getBoolean("welcome", "main", true, "Will thinker show the welcome screen");
    }
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        PROXY.init(event);
        jsonReader.readAllProfiles();

    }
    @EventHandler
    public void registerCommands(FMLServerStartingEvent e){
        e.registerServerCommand(new CommandGetTileNBT());
    }
    public static void error(Throwable err){
        err.printStackTrace();
    }
    public static void error(String err){
        System.err.println(err);
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(welcomed)return;
        if (delay > 0 && event.phase == TickEvent.Phase.END) {
            --delay;
            if (delay == 0) {
                FMLClientHandler.instance().getClient().displayGuiScreen(new ThinkingGuiWelcome());
                welcomed=true;
                config.get("main", "welcome", true, "Will thinker show the welcome screen").set(false);
                config.save();
            }
        }
    }
}
