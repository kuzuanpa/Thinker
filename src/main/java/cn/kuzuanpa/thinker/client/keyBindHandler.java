/*
 * This class was created by <kuzuanpa>. It is distributed as
 * part of the kTFRUAddon Mod. Get the Source Code in github:
 * https://github.com/kuzuanpa/kTFRUAddon
 *
 * kTFRUAddon is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 */

package cn.kuzuanpa.thinker.client;

import cn.kuzuanpa.thinker.client.render.gui.ThinkingGui;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import static cn.kuzuanpa.thinker.Thinker.MOD_NAME;

public class keyBindHandler {
    public static KeyBinding keyThink = new KeyBinding("key.Think", Keyboard.KEY_H, MOD_NAME);
    private boolean ThinkKeyPressed;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (keyThink.isPressed()) {
                FMLClientHandler.instance().getClient().displayGuiScreen(new ThinkingGui());
        }

    }
}
