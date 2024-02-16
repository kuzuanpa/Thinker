package cn.kuzuanpa.thinker;

import cn.kuzuanpa.thinker.client.json.jsonReader;
import cn.kuzuanpa.thinker.client.render.gui.ThinkingGuiMain;
import cn.kuzuanpa.thinker.client.render.gui.ThinkingGuiStart;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

@Mod(modid = Thinker.MOD_ID, version = Thinker.VERSION)
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
    public static void error(Throwable err){
        err.printStackTrace();
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!welcomed && delay > 0 && event.phase == TickEvent.Phase.END) {
            --delay;
            if (delay == 0) {
                FMLClientHandler.instance().getClient().displayGuiScreen(new ThinkingGuiStart());
                welcomed=true;
                config.get("main", "welcome", true, "Will thinker show the welcome screen").set(false);
            }
        }
        config.save();
    }
}
