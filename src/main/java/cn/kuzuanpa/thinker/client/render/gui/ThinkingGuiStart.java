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
import cn.kuzuanpa.thinker.client.json.jsonReader;
import cn.kuzuanpa.thinker.client.profileHandler;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.DummyBlockAnimeOutlineGlowth;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.DummyBlockAnimeRotateSteadily;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldBlock;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldHandler;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldTileEntity;
import cn.kuzuanpa.thinker.client.render.gui.anime.animeRotateSteadily;
import cn.kuzuanpa.thinker.client.render.gui.button.*;
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
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static cn.kuzuanpa.thinker.client.keyBindHandler.keyThink;
import static cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldHandler.dummyWorldBlocksHashMap;
import static cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldHandler.dummyWorldTileEntityHashMap;

/**
 * @author kuzuanpa
 */
@SideOnly(Side.CLIENT)
public class ThinkingGuiStart extends GuiScreen {

	private int displayWidth,displayHeight;
	public String selectedProfileID="HelloThinker";
	public boolean openByUser,themeSelectorFolded=false;
	public long initTime=0,lastProfileSelectedTime=0;
	private List<String> hoveringString=new ArrayList<>();
	protected List<ThinkerButton> buttonsHaveAnime = new ArrayList<ThinkerButton>();
	public ThinkingGuiStart() {
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
		//buttonList.add(new ThinkerButton(-1,displayWidth-20,displayHeight-20,20,20,l10n("R")));
		buttonList.add(new ThinkingBackground(0, displayWidth,displayHeight));
		buttonList.add(new DummyWorld(1,0,0,displayWidth,displayHeight));
		buttonList.add(new thinkerImage(2,displayWidth-52,20,0,0,32,32,"textures/gui/think/base.png", l10n("thinker.settings")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime));


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
		profileHandler.clearAllProfile();
		profileHandler.addProfile(new profileHandler.thinkingProfile("test1",Items.string.getIconFromDamage(0),new thinkerImage(12,displayWidth-122,20,0,0,32,32,"textures/gui/think/base.png", l10n("test")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime)));
		profileHandler.addProfile(new profileHandler.thinkingProfile("HelloThinker",Items.string.getIconFromDamage(0),blocks,tiles,new thinkerImage(13,displayWidth-122,20,0,0,32,32,"textures/gui/think/base.png", l10n("test")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime)));
		try{profileHandler.addProfile(jsonReader.readProfiles("testJson"));}catch (Exception e){e.printStackTrace();}

		if(openByUser)postInit();
		buttonsHaveAnime.forEach(button-> button.updateInitTime(initTime));

		lastProfileSelectedTime= System.currentTimeMillis();
		List<ThinkerButton> buttonsProfile= profileHandler.getProfile("HelloThinker").buttons;
		buttonsProfile.forEach(button->button.updateInitTime(lastProfileSelectedTime));
		buttonList.addAll(buttonsProfile);
		profileHandler.onProfileChanged("HelloThinker");
		dummyWorldHandler.onProfileChanged("HelloThinker");
		((DummyWorld)buttonList.get(1)).onProfileChanged(lastProfileSelectedTime);
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
	protected boolean onButtonPressed(GuiButton button) {
		if(button.id==2) try{profileHandler.addProfile(jsonReader.readProfiles("testJson"));}catch (Exception e){e.printStackTrace();}
		((DummyWorld)buttonList.get(1)).clickOnOtherButton=button.id!=1;
		return true;
	}
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
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
