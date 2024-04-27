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

package cn.kuzuanpa.thinker.client;

import cn.kuzuanpa.thinker.client.render.gui.ThinkingGuiMain;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregapi.data.LH;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.input.Keyboard;

public class TooltipHandler {
    int pressedTime=0;
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTooltipEvent(ItemTooltipEvent event) {
        boolean isThinkerGoingToDisplay=Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&Keyboard.isKeyDown(keyBindHandler.keyThink.getKeyCode());
        if(!isThinkerGoingToDisplay)pressedTime=0;
        else pressedTime++;

        if(event.itemStack.getItem() == Item.getItemFromBlock(Blocks.dirt)) {
            StringBuilder processBar = new StringBuilder();
            if(isThinkerGoingToDisplay) {
                for (int i = 0; i < 20; i++) {
                    if (i < calculateProcessBar()) processBar.append(EnumChatFormatting.GREEN.toString()).append("| ");
                    else processBar.append(EnumChatFormatting.GRAY.toString()).append("| ");
                }
                event.toolTip.add(processBar.toString());
            }else event.toolTip.add(StatCollector.translateToLocal("tooltip.thinker.0")+" LShift + "+Keyboard.getKeyName(keyBindHandler.keyThink.getKeyCode())+" "+StatCollector.translateToLocal("tooltip.thinker.1"));
            if(pressedTime>configHandler.keyPressedTimeNeededToStartThink.getI())Minecraft.getMinecraft().displayGuiScreen(new ThinkingGuiMain(event.itemStack));
        }
    }
    public int calculateProcessBar(){
        return pressedTime*20 / configHandler.keyPressedTimeNeededToStartThink.getI();
    }
}
