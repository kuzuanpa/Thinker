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

package cn.kuzuanpa.thinker.client.handler;

import cn.kuzuanpa.thinker.client.ThinkingGuiMain;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.input.Keyboard;

public class tooltipHandler {
    int pressedTime=0;
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTooltipEvent(ItemTooltipEvent event) {
        boolean isThinkerGoingToDisplay=Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&Keyboard.isKeyDown(keyBindHandler.keyThink.getKeyCode());
        if(!isThinkerGoingToDisplay)pressedTime=0;
        else pressedTime++;
        if(configHandler.displayItemStackUnlocalizedName.get())event.toolTip.add(event.itemStack.getUnlocalizedName());
        if(profileHandler.isItemHaveProfile(event.itemStack.getUnlocalizedName())) {
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
