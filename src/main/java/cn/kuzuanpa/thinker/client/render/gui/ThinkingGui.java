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

import cn.kuzuanpa.thinker.client.render.gui.anime.animeMove;
import cn.kuzuanpa.thinker.client.render.gui.anime.animeRotate;
import cn.kuzuanpa.thinker.client.render.gui.button.*;
import cn.kuzuanpa.thinker.clientProxy;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

/**
 * @author kuzuanpa
 */
@SideOnly(Side.CLIENT)
public class ThinkingGui extends GuiScreen {

	private int displayWidth,displayHeight;
	public long initTime=0;
	protected List<CommonGuiButton> buttonHaveAnimeList = new ArrayList();
	public ThinkingGui() {
		allowUserInput = false;
	}
	public void initGui() {
		super.initGui();
		initTime=System.currentTimeMillis();
		buttonHaveAnimeList.clear();
		displayWidth= FMLClientHandler.instance().getClient().currentScreen.width;
		displayHeight= FMLClientHandler.instance().getClient().currentScreen.height;
		buttonList.clear();
		buttonList.add(new ThinkingBackground(0, displayWidth,displayHeight));
		buttonList.add(new CommonModel(1,50,50,displayWidth,displayHeight));
		buttonList.add(new customImage(2,displayWidth-60,60,0,0,32,32,"textures/gui/think/base.png"));
		buttonHaveAnimeList.add((CommonGuiButton) buttonList.get(2));
		buttonHaveAnimeList.get(0).addAnime(new animeMove(5000,10000,10,40));
	}

	protected void keyTyped(char p_73869_1_, int p_73869_2_)
	{
		if (p_73869_2_ == 1|| p_73869_2_== Keyboard.KEY_H)
		{
			close();
		}
	}
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
	{
		if (p_73864_3_ == 0)
		{
			for (int l = this.buttonList.size() - 1; l >= 0 ;l--)
			{
				GuiButton guibutton = (GuiButton)this.buttonList.get(l);

				if (guibutton.mousePressed(this.mc, p_73864_1_, p_73864_2_))
				{
					GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre(this, guibutton, this.buttonList);
					if (MinecraftForge.EVENT_BUS.post(event))
						break;
					event.button.func_146113_a(this.mc.getSoundHandler());
					if (this.onButtonPressed(event.button)) break;
					if (this.equals(this.mc.currentScreen))
						MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(this, event.button, this.buttonList));
				}
			}
		}
	}
	protected boolean onButtonPressed(GuiButton button) {
		switch (button.id){
			case 0:
			case 1: return close();
			case 2:
			default: return true;
		}
	}
	public void updateScreen() {
		buttonHaveAnimeList.forEach(button-> button.doAnime(initTime,System.currentTimeMillis()));
	}
	public boolean close() {
		this.mc.displayGuiScreen(null);
		this.mc.setIngameFocus();
		return true;
	}
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}
