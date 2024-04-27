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

import cn.kuzuanpa.thinker.client.TooltipHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cn.kuzuanpa.thinker.client.render.gui.ThinkingGuiMain;
import cn.kuzuanpa.thinker.client.keyBindHandler;
import net.minecraftforge.common.MinecraftForge;

public class clientProxy extends commonProxy {


    public void init(FMLInitializationEvent event) {
        super.init(event);
        ClientRegistry.registerKeyBinding(keyBindHandler.keyThink);
        FMLCommonHandler.instance().bus().register(new keyBindHandler());
        MinecraftForge.EVENT_BUS.register(new TooltipHandler());
    }


    @SideOnly(Side.CLIENT)
    public void registerRenderers(){
    }
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch(id) {
            case 0:
                return new ThinkingGuiMain();
        }
        return null;
    }

}