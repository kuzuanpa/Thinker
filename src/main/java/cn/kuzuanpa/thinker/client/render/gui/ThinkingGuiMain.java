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

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.configHandler;
import cn.kuzuanpa.thinker.client.json.jsonReader;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.DummyBlockAnimeOutlineGlowth;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.DummyBlockAnimeRotateSteadily;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldBlock;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldHandler;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldTileEntity;
import cn.kuzuanpa.thinker.client.render.gui.anime.animeMoveLinear;
import cn.kuzuanpa.thinker.client.render.gui.anime.animeRotateSteadily;
import cn.kuzuanpa.thinker.client.render.gui.button.*;
import cn.kuzuanpa.thinker.client.profileHandler;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static cn.kuzuanpa.thinker.client.keyBindHandler.keyThink;
import static cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldHandler.dummyWorldBlocksHashMap;
import static cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldHandler.dummyWorldTileEntityHashMap;
import static cn.kuzuanpa.thinker.client.profileHandler.YOffset;
import static cn.kuzuanpa.thinker.client.profileHandler.profileList;

/**
 * @author kuzuanpa
 */
@SideOnly(Side.CLIENT)
public class ThinkingGuiMain extends GuiScreen {

	private int displayWidth,displayHeight;
	public int selectedProfile=-1;
	public boolean openByUser,themeSelectorFolded=false;
	public long initTime=0,lastProfileSelectedTime=0;
	private List<String> hoveringString=new ArrayList<>();
	protected List<ThinkerButton> buttonsHaveAnime = new ArrayList<ThinkerButton>();
	public static ArrayList<ThinkerButton> buttonsProfile= new ArrayList<ThinkerButton>();
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
		dummyWorldBlocksHashMap.clear();
		dummyWorldTileEntityHashMap.clear();
		buttonList.add(new ThinkerButton(-1,displayWidth-20,displayHeight-20,20,20,l10n("R")));

		buttonList.add(new DummyWorld(1,0,0,displayWidth,displayHeight));
		buttonList.add(new thinkerImage(2,displayWidth-52,20,0,0,32,32,"textures/gui/think/base.png", l10n("thinker.settings")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime));
		buttonList.add(new ThinkingProfileList(3,0,0,displayHeight).addToList(buttonsHaveAnime));
		buttonList.add(new thinkerImage(4,65,0,0,32,16,16,"textures/gui/think/base.png", l10n("thinker.list.fold")).addToList(buttonsHaveAnime));
		buttonList.add(new thinkerImage(5,-16,0,16,32,16,16,"textures/gui/think/base.png",l10n("thinker.list.unfold")).addToList(buttonsHaveAnime));

		HashMap<BlockPosition, dummyWorldBlock> blocks=new HashMap<>();
		HashMap<BlockPosition, dummyWorldTileEntity> tiles=new HashMap<>();
		blocks.put(new BlockPosition(4,2,4),new dummyWorldBlock(Blocks.chest,new DummyBlockAnimeOutlineGlowth(1000,20000,new BlockPosition(4,2,4),-1,4)));
		blocks.put(new BlockPosition(5,2,5),new dummyWorldBlock(Blocks.chest,new DummyBlockAnimeRotateSteadily()));
		blocks.put(new BlockPosition(0,2,0),new dummyWorldBlock(Blocks.dark_oak_stairs,new DummyBlockAnimeRotateSteadily()));
		blocks.put(new BlockPosition(0,3,0),new dummyWorldBlock(Blocks.daylight_detector,new DummyBlockAnimeRotateSteadily()));
		blocks.put(new BlockPosition(1,2,0),new dummyWorldBlock(Blocks.double_wooden_slab));
		blocks.put(new BlockPosition(3,2,0),new dummyWorldBlock(Blocks.fence));
		blocks.put(new BlockPosition(0,2,1),new dummyWorldBlock(Blocks.command_block));
		blocks.put(new BlockPosition(4,2,0),new dummyWorldBlock(Blocks.command_block));
		blocks.put(new BlockPosition(1,1,0),new dummyWorldBlock(Blocks.acacia_stairs));
		blocks.put(new BlockPosition(0,2,5),new dummyWorldBlock(Blocks.diamond_block));
		blocks.put(new BlockPosition(0,1,0),new dummyWorldBlock(Blocks.jukebox));
		blocks.put(new BlockPosition(2,2,0),new dummyWorldBlock(Blocks.stained_glass));
		profileList.clear();
		profileList.add(new profileHandler.thinkingProfile(Items.string.getIconFromDamage(0),new thinkerImage(12,displayWidth-122,20,0,0,32,32,"textures/gui/think/base.png", l10n("test")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime)));
		profileList.add(new profileHandler.thinkingProfile(Items.string.getIconFromDamage(0),blocks,tiles,new thinkerImage(13,displayWidth-122,20,0,0,32,32,"textures/gui/think/base.png", l10n("test")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime)));
		profileList.add(new profileHandler.thinkingProfile(Items.apple.getIconFromDamage(0),new thinkerImage(14,displayWidth-122,20,0,0,32,32,"textures/gui/think/base.png", l10n("test")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime)));
		profileList.add(new profileHandler.thinkingProfile(Items.beef.getIconFromDamage(0),new thinkerImage(15,displayWidth-122,20,0,0,32,32,"textures/gui/think/base.png", l10n("test")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime)));
		profileList.add(new profileHandler.thinkingProfile(Items.saddle.getIconFromDamage(0),new thinkerImage(16,displayWidth-122,20,0,0,32,32,"textures/gui/think/base.png", l10n("test")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime)));
		profileList.add(new profileHandler.thinkingProfile(Items.string.getIconFromDamage(0),new thinkerImage(17,displayWidth-122,20,0,0,32,32,"textures/gui/think/base.png", l10n("test")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime)));
		profileList.add(new profileHandler.thinkingProfile(Items.diamond_axe.getIconFromDamage(0),new thinkerImage(18,displayWidth-122,20,0,0,32,32,"textures/gui/think/base.png", l10n("test")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime)));
		profileList.add(new profileHandler.thinkingProfile(Items.snowball.getIconFromDamage(0),new thinkerImage(19,displayWidth-122,20,0,0,32,32,"textures/gui/think/base.png", l10n("test")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime)));
		try{profileHandler.profileList.add(jsonReader.readProfiles("testJson"));}catch (Exception e){e.printStackTrace();}

		if(openByUser)postInit();
		buttonsHaveAnime.forEach(button-> button.updateInitTime(initTime));

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
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int mouseButton)
	{
			for (int l = this.buttonList.size() - 1; l >= 0 ;l--)
			{
				ThinkerButton guibutton = (ThinkerButton)this.buttonList.get(l);
				if(guibutton.updateHoverState(p_73864_1_, p_73864_2_))
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
	protected void onProfileChanged(int newProfileID){
		lastProfileSelectedTime= System.currentTimeMillis();
		buttonList.removeAll(buttonsProfile);
		buttonsProfile= profileList.get(newProfileID).buttons;
		buttonsProfile.forEach(button->button.updateInitTime(lastProfileSelectedTime));
		buttonList.addAll(buttonsProfile);
		profileHandler.onProfileChanged(newProfileID);
		dummyWorldHandler.onProfileChanged(newProfileID);
		((DummyWorld)buttonList.get(1)).onProfileChanged(lastProfileSelectedTime);
		selectedProfile=newProfileID;
	}
	protected boolean onButtonPressed(GuiButton button) {
		if(button.id==-1) try{profileHandler.profileList.add(jsonReader.readProfiles("testJson"));}catch (Exception e){e.printStackTrace();}
		((DummyWorld)buttonList.get(1)).clickOnOtherButton=button.id!=1;
		if(button.id==2) this.mc.displayGuiScreen(new ThinkerSettingsGui());
		if(button.id==3) {
			int mouseY=this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			if(Mouse.isInsideWindow())for (int i=0;i<profileList.size();i++)if(mouseY>=YOffset+i*(16+ configHandler.themeSelectorProfileGap.get()) && mouseY<=YOffset+16+i*(16+ configHandler.themeSelectorProfileGap.get())){
				if(selectedProfile!=i)onProfileChanged(i);
				break;
			}
		}
		if(button.id==4&&!themeSelectorFolded) {
			((ThinkerButton)buttonList.get(3)).addAnime(new animeMoveLinear((int) (System.currentTimeMillis()-initTime), (int) (System.currentTimeMillis()-initTime+ configHandler.getConfiguredAnimeTime(500)),-80,0));
			((ThinkerButton)buttonList.get(4)).addAnime(new animeMoveLinear((int) (System.currentTimeMillis()-initTime), (int) (System.currentTimeMillis()-initTime+ configHandler.getConfiguredAnimeTime(500)),-80,0));
			((ThinkerButton)buttonList.get(5)).addAnime(new animeMoveLinear((int) (System.currentTimeMillis()-initTime), (int) (System.currentTimeMillis()-initTime+ configHandler.getConfiguredAnimeTime(200)),16,0));
			themeSelectorFolded=true;
		}
		if(button.id==5&&themeSelectorFolded) {
			((ThinkerButton)buttonList.get(3)).addAnime(new animeMoveLinear((int) (System.currentTimeMillis()-initTime), (int) (System.currentTimeMillis()-initTime+ configHandler.getConfiguredAnimeTime(500)),80,0));
			((ThinkerButton)buttonList.get(4)).addAnime(new animeMoveLinear((int) (System.currentTimeMillis()-initTime), (int) (System.currentTimeMillis()-initTime+ configHandler.getConfiguredAnimeTime(500)),80,0));
			((ThinkerButton)buttonList.get(5)).addAnime(new animeMoveLinear((int) (System.currentTimeMillis()-initTime), (int) (System.currentTimeMillis()-initTime+ configHandler.getConfiguredAnimeTime(200)),-16,0));
			themeSelectorFolded=false;
		}
		return true;
	}
	public void handleMouseInput(){
		super.handleMouseInput();
		int x = Mouse.getX() * this.width / this.mc.displayWidth;
		int y =this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
		if(((ThinkerButton)buttonList.get(3)).visible&&Mouse.isInsideWindow()&&Mouse.getEventDWheel()!=0&& x< profileHandler.profileLayer*32+32&& x>0)
			profileHandler.MouseWheelHandler();
	}
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_){
		super.drawScreen(p_73863_1_,p_73863_2_,p_73863_3_);
		profileHandler.tick();

		if(!Mouse.isInsideWindow())return;
		int x = Mouse.getX() * this.width / this.mc.displayWidth;
		int y = this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
		buttonList.forEach(b -> {
			if (!(b instanceof ThinkerButton)) return;
			ThinkerButton button = (ThinkerButton) b;
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
