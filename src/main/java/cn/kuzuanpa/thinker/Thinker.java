/*
 * This class was created by <kuzuanpa>. It is a part of Thinker.
 * Get the Source Code in github:
 * https://github.com/kuzuanpa/Thinker
 *
 * Thinker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * Thinker is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 */
package cn.kuzuanpa.thinker;

import blockrenderer6343.world.DummyWorldTickThread;
import cn.kuzuanpa.thinker.client.handler.configHandler;
import cn.kuzuanpa.thinker.client.json.*;
import cn.kuzuanpa.thinker.command.CommandGetTileNBT;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.MixinEnvironment;

import static cn.kuzuanpa.thinker.client.json.thinkerJsonReader.animesAdaptors;
import static cn.kuzuanpa.thinker.client.json.thinkerJsonReader.objectsAdaptors;

@Mod(modid = Thinker.MOD_ID, version = Thinker.VERSION, dependencies = "required-after:CodeChickenCore@[1.0.7,)")
public class Thinker
{
    public static final String MOD_ID = "thinker";
    public static final String MOD_NAME = "Thinker";
    public static final String VERSION = "0.0.1";
    public static final DummyWorldTickThread dummyWorldTickThread=new DummyWorldTickThread();
    public static int delay = 5;
    public static boolean isGeckoLibLoaded=false,isServerSide=false;
    @SidedProxy(clientSide = "cn.kuzuanpa.thinker.clientProxy",
            serverSide = "cn.kuzuanpa.thinker.commonProxy")
    public static commonProxy PROXY;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        isServerSide=MinecraftServer.getServer() instanceof DedicatedServer;
        isGeckoLibLoaded= Loader.isModLoaded("geckolib3");
        if(isServerSide){
            FMLLog.log(Level.ERROR,"Thinker is an CLIENT only mod, and will disable entirely in Server!");
            return;
        }
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        configHandler.preInit(event);
    }
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if(isServerSide)return;
        PROXY.init(event);

        dummyWorldTickThread.start();
        configHandler.saveAll();
        registerObjectAdaptor(new defaultObjectsAdaptor());
        registerAnimeAdaptor(new defaultAnimeAdaptor());try {
        thinkerJsonReader.readAllProfiles("ideas");
    }catch (Exception ignored){}
    }
    @EventHandler
    public void registerCommands(FMLServerStartingEvent e){
        e.registerServerCommand(new CommandGetTileNBT());
    }
    public static void err(Throwable err){
        err.printStackTrace();
    }
    public static void err(String err){
        System.err.println(err);
    }
    public static void log(String log){
        System.out.println(log);
    }
    public static int getInt(Object str){
        return Integer.parseInt((String) str);
    }
    public static long getLong(Object str){
        return Long.parseLong((String) str);
    }
    public static double getDouble(Object str){
        return Double.parseDouble((String) str);
    }
    public static float getFloat(Object str){
        return Float.parseFloat((String) str);
    }
    public static boolean getBoolean(Object str){return Boolean.parseBoolean((String) str);}

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(!configHandler.welcome.get())return;
        if (delay > 0 && event.phase == TickEvent.Phase.END) {
            --delay;
            if (delay == 0) {
                FMLClientHandler.instance().getClient().displayGuiScreen(new cn.kuzuanpa.thinker.client.ThinkingGuiMain("HelloThinker"));
                configHandler.welcome.set(false);
                configHandler.welcome.save();
            }
        }
    }

    public void registerObjectAdaptor(IThinkerObjectsAdaptor adaptor){
        objectsAdaptors.add(adaptor);
    }
    public void registerAnimeAdaptor(IThinkerAnimeAdaptor adaptor){
        animesAdaptors.add(adaptor);
    }
}
