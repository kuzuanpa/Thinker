/**
 * Copyright (c) 2019 Gregorius Techneticies
 *
 * This file is part of GregTech.
 *
 * GregTech is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GregTech is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GregTech. If not, see <http://www.gnu.org/licenses/>.
 */

package cn.kuzuanpa.thinker.client.render.gui;

import cn.kuzuanpa.thinker.client.render.gui.button.ThinkingBackground;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

/**
 * @author kuzuanpa
 */
@SideOnly(Side.CLIENT)
public class ThinkingGui extends GuiScreen {

	public ThinkingGui() {
		allowUserInput = false;
	}
	public void initGui() {
		super.initGui();
		buttonList.clear();
		buttonList.add(new ThinkingBackground(0,1920,1080));
		buttonList.add(new GuiButton(1,2,2,20,20, "x"));

	}

	protected void keyTyped(char p_73869_1_, int p_73869_2_)
	{
		if (p_73869_2_ == 1|| p_73869_2_== Keyboard.KEY_H)
		{
			close();
		}
	}
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {

		if (mouseX >= 2 && mouseY >= 2 && mouseX < 22 + width && mouseY < 22 + height){
			close();
		}

		return true;
	}
	protected void actionPerformed(GuiButton button) {
		switch (button.id){
			case 0:
			case 1: close(); break;
		}
	}
	public void close() {
		this.mc.displayGuiScreen(null);
		this.mc.setIngameFocus();
	}
}
