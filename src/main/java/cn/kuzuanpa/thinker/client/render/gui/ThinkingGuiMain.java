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

import cn.kuzuanpa.ktfruaddon.item.util.ItemList;
import cn.kuzuanpa.thinker.client.configHandler;
import cn.kuzuanpa.thinker.client.render.gui.anime.animeMoveLinear;
import cn.kuzuanpa.thinker.client.render.gui.anime.animeRotateSteadily;
import cn.kuzuanpa.thinker.client.render.gui.button.*;
import cn.kuzuanpa.thinker.client.thinkingProfileHandler;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregapi.data.MT;
import gregapi.data.OP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.kuzuanpa.thinker.client.keyBindHandler.keyThink;

/**
 * @author kuzuanpa
 */
@SideOnly(Side.CLIENT)
public class ThinkingGuiMain extends GuiScreen {

	private int displayWidth,displayHeight;
	public boolean openByUser,themeSelectorFolded=false;
	public long initTime=0;
	private List<String> hoveringString=new ArrayList<>();
	protected List<CommonGuiButton> buttonsHaveAnime = new ArrayList<CommonGuiButton>();
	public ThinkingGuiMain() {
		openByUser=true;
		allowUserInput = false;
	}
	public void postInit(){
		initTime=System.currentTimeMillis();
		openByUser=false;
	}
	public void initGui() {
		super.initGui();
		displayWidth= FMLClientHandler.instance().getClient().currentScreen.width;
		displayHeight= FMLClientHandler.instance().getClient().currentScreen.height;
		buttonList.clear();
		buttonsHaveAnime.clear();
		buttonList.add(new ThinkingBackground(0, displayWidth,displayHeight));
		buttonList.add(new DummyWorld(1,0,0,displayWidth,displayHeight));
		buttonList.add(new thinkerImage(2,displayWidth-52,20,0,0,32,32,"textures/gui/think/base.png", l10n("thinker.settings")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime));
		buttonList.add(new ThinkingProfileList(3,0,0,displayHeight).addToList(buttonsHaveAnime));
		buttonList.add(new thinkerImage(4,65,0,0,32,16,16,"textures/gui/think/base.png", l10n("thinker.list.fold")).addToList(buttonsHaveAnime));
		buttonList.add(new thinkerImage(5,-16,0,16,32,16,16,"textures/gui/think/base.png",l10n("thinker.list.unfold")).addToList(buttonsHaveAnime));
		thinkingProfileHandler.profileList.clear();
		thinkingProfileHandler.profileList.add(new thinkingProfileHandler.thinkingProfile(false, Items.string.getIconFromDamage(0)));
		thinkingProfileHandler.profileList.add(new thinkingProfileHandler.thinkingProfile(false, ItemList.Alpha_Particle.item().getIconFromDamage(1000)));
		thinkingProfileHandler.profileList.add(new thinkingProfileHandler.thinkingProfile(false, OP.dust.mat(MT.Na,1).getIconIndex(),MT.Na.fRGBaSolid));
		thinkingProfileHandler.profileList.add(new thinkingProfileHandler.thinkingProfile(false, Blocks.stone.getIcon(0,0)));
		thinkingProfileHandler.profileList.add(new thinkingProfileHandler.thinkingProfile(false, OP.ring.mat(MT.Bronze,1).getIconIndex()));
		thinkingProfileHandler.profileList.add(new thinkingProfileHandler.thinkingProfile(false));
		thinkingProfileHandler.profileList.add(new thinkingProfileHandler.thinkingProfile(false, OP.ring.mat(MT.Bronze,1).getIconIndex()));
		thinkingProfileHandler.profileList.add(new thinkingProfileHandler.thinkingProfile(false, OP.ring.mat(MT.Bronze,1).getIconIndex()));
		thinkingProfileHandler.profileList.add(new thinkingProfileHandler.thinkingProfile(false, OP.ring.mat(MT.Bronze,1).getIconIndex()));
		if(openByUser)postInit();
	}
	public String l10n(String key){String text1= LanguageRegistry.instance().getStringLocalization(key);if(text1.equals(""))return key;return text1;}
	protected void keyTyped(char p_73869_1_, int p_73869_2_)
	{
		if (p_73869_2_ == 1|| p_73869_2_== keyThink.getKeyCode())
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
				CommonGuiButton guibutton = (CommonGuiButton)this.buttonList.get(l);

				if (guibutton.updateHoverState(p_73864_1_, p_73864_2_))
				{
					GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre(this, guibutton, this.buttonList);
					if (MinecraftForge.EVENT_BUS.post(event))
						break;
					if(event.button.id!=0)event.button.func_146113_a(this.mc.getSoundHandler());
					if (this.onButtonPressed(event.button)) break;
					if (this.equals(this.mc.currentScreen))
						MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(this, event.button, this.buttonList));
				}
			}
		}
	}
	protected boolean onButtonPressed(GuiButton button) {
		((DummyWorld)buttonList.get(1)).clickOnOtherButton=button.id!=1;
		if(button.id==2) this.mc.displayGuiScreen(new ThinkerSettingsGui());
		if(button.id==3) thinkingProfileHandler.MouseClickHandler(this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1);
		if(button.id==4&&!themeSelectorFolded) {
			((CommonGuiButton)buttonList.get(3)).addAnime(new animeMoveLinear((int) (System.currentTimeMillis()-initTime), (int) (System.currentTimeMillis()-initTime+ configHandler.getConfiguredAnimeTime(500)),-80,0));
			((CommonGuiButton)buttonList.get(4)).addAnime(new animeMoveLinear((int) (System.currentTimeMillis()-initTime), (int) (System.currentTimeMillis()-initTime+ configHandler.getConfiguredAnimeTime(500)),-80,0));
			((CommonGuiButton)buttonList.get(5)).addAnime(new animeMoveLinear((int) (System.currentTimeMillis()-initTime), (int) (System.currentTimeMillis()-initTime+ configHandler.getConfiguredAnimeTime(200)),16,0));
			themeSelectorFolded=true;
		}
		if(button.id==5&&themeSelectorFolded) {
			((CommonGuiButton)buttonList.get(3)).addAnime(new animeMoveLinear((int) (System.currentTimeMillis()-initTime), (int) (System.currentTimeMillis()-initTime+ configHandler.getConfiguredAnimeTime(500)),80,0));
			((CommonGuiButton)buttonList.get(4)).addAnime(new animeMoveLinear((int) (System.currentTimeMillis()-initTime), (int) (System.currentTimeMillis()-initTime+ configHandler.getConfiguredAnimeTime(500)),80,0));
			((CommonGuiButton)buttonList.get(5)).addAnime(new animeMoveLinear((int) (System.currentTimeMillis()-initTime), (int) (System.currentTimeMillis()-initTime+ configHandler.getConfiguredAnimeTime(200)),-16,0));
			themeSelectorFolded=false;
		}
		return true;
	}
	public void handleMouseInput(){
		super.handleMouseInput();
		int x = Mouse.getX() * this.width / this.mc.displayWidth;
		int y =this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
		if(((CommonGuiButton)buttonList.get(3)).visible&&Mouse.isInsideWindow()&&Mouse.getEventDWheel()!=0&& x<thinkingProfileHandler.profileLayer*32+32&& x>0)thinkingProfileHandler.MouseWheelHandler();
	}
	public void updateScreen() {
		buttonsHaveAnime.forEach(button-> button.doAnime(initTime));
	}
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_){
		super.drawScreen(p_73863_1_,p_73863_2_,p_73863_3_);
		thinkingProfileHandler.tick();

		if(!Mouse.isInsideWindow())return;
		int x = Mouse.getX() * this.width / this.mc.displayWidth;
		int y = this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
		buttonList.forEach(b -> {
			if (!(b instanceof CommonGuiButton)) return;
			CommonGuiButton button = (CommonGuiButton) b;
			if(!button.visible)return;
			if(button.updateHoverState(x,y))hoveringString= Collections.singletonList(button.displayString);
		});
		if (hoveringString == null||hoveringString.stream().allMatch(string->string.equals(""))) return;
		drawHoveringText(hoveringString, x, y+5, fontRendererObj);
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
