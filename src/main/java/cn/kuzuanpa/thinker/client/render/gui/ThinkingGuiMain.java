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

package cn.kuzuanpa.thinker.client.render.gui;

import cn.kuzuanpa.thinker.client.configHandler;
import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import cn.kuzuanpa.thinker.client.dummyWorldHandler;
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
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.*;

import static cn.kuzuanpa.thinker.client.dummyWorldHandler.*;
import static cn.kuzuanpa.thinker.client.profileHandler.displayProfileIDMap;
import static cn.kuzuanpa.thinker.client.profileHandler.YOffset;


/**
 * @author kuzuanpa
 */
@SideOnly(Side.CLIENT)
public class ThinkingGuiMain extends GuiScreen {

	private int displayWidth,displayHeight;
	public String selectedProfileID ="";
	public boolean openByUser,themeSelectorFolded=false;
	public long initTime=0,lastProfileSelectedTime=0;
	public final int ID_FOR_CUSTOM_BUTTONS=10;
	private List<String> hoveringString=new ArrayList<>();
	protected List<ThinkerButton> buttonsHaveAnime = new ArrayList<ThinkerButton>();
	public static ArrayList<ThinkerButton> buttonsProfile= new ArrayList<ThinkerButton>();
	public ThinkingGuiMain() {
		openByUser=true;
		allowUserInput = false;
	}
	public ThinkingGuiMain(ItemStack item) {
		openByUser=true;
		allowUserInput = false;
		selectedProfileID = item.getDisplayName();
	}
	public void onOpenByUser(){
		initTime=System.currentTimeMillis();
		openByUser=false;
	}
	public void initGui() {
		super.initGui();
		displayWidth= FMLClientHandler.instance().getClient().currentScreen.width;
		displayHeight= FMLClientHandler.instance().getClient().currentScreen.height;
		buttonList.clear();
		buttonsHaveAnime.clear();
		dummyWorldObjects.clear();

		buttonList.add(new ThinkingBackground(0, displayWidth,displayHeight));
		buttonList.add(new DummyWorld(1,0,0,displayWidth,displayHeight));
		buttonList.add(new thinkerImage(2,displayWidth-52,20,0,0,32,32,"textures/gui/think/base.png", l10n("thinker.settings")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime));
		buttonList.add(new ThinkingProfileList(3,0,0,displayHeight).addToList(buttonsHaveAnime));
		buttonList.add(new thinkerImage(4,65,0,0,32,16,16,"textures/gui/think/base.png", l10n("thinker.list.fold")).addToList(buttonsHaveAnime));
		buttonList.add(new thinkerImage(5,-16,0,16,32,16,16,"textures/gui/think/base.png",l10n("thinker.list.unfold")).addToList(buttonsHaveAnime));

		//ArrayList<IdummyWorldThinkerObject> blocks=new ArrayList<>();
		////blocks.put(new BlockPosition(4,2,4),new dummyWorldBlock(Blocks.chest,new DummyBlockAnimeOutlineGlowth(1000,20000,new BlockPosition(4,2,4),-1,4)));
		////blocks.put(new BlockPosition(5,2,5),new dummyWorldBlock(Blocks.chest,new DummyBlockAnimeRotateSteadily()));
		//blocks.add(new dummyWorldBlock( new BlockPosition(0,2,0), Blocks.dark_oak_stairs,new DummyWorldGraphicAnimeRotateSteadily()).setRenderAllFace(true));
		//blocks.add(new dummyWorldBlock( new BlockPosition(0,3,0), Blocks.daylight_detector,new DummyWorldGraphicAnimeRotateSteadily()));
		//blocks.add(new dummyWorldBlock( new BlockPosition(1,2,0), Blocks.double_wooden_slab));
		//blocks.add(new dummyWorldBlock( new BlockPosition(3,2,0), Blocks.fence));
		//blocks.add(new dummyWorldBlock( new BlockPosition(1,1,0), Blocks.acacia_stairs).setRenderAllFace(true));
		//blocks.add(new dummyWorldBlock( new BlockPosition(0,2,5), Blocks.diamond_block));
		//blocks.add(new dummyWorldBlock( new BlockPosition(0,2,4), Blocks.diamond_block));
		//blocks.add(new dummyWorldBlock( new BlockPosition(2,2,0), Blocks.stained_glass));
		//blocks.add(new dummyWorldBlock( new BlockPosition(2,2,1), MultiTileEntityRegistry.getRegistry("gt.multitileentity").getItem(10005), new DummyWorldGraphicAnimeMoveLinear(0,10000,1,1,1)));
		//blocks.add(new dummyWorldBlock( new BlockPosition(2,3,1), MultiTileEntityRegistry.getRegistry("ktfru.multitileentity").getItem(31001), new DummyWorldGraphicAnimeMoveLinear(0,10000,1,1,1)));
		//blocks.add(new dummyWorldBlock( new BlockPosition(2,4,1), MultiTileEntityRegistry.getRegistry("ktfru.multitileentity").getItem(31001), new DummyWorldGraphicAnimeMoveLinear(0,10000,1,1,1)));
		//blocks.add(new dummyWorldGeckoModel( new BlockPosition(2,5,1), "botarium.geo.json","ideas/botarium.png","botarium.animation.json"));
		profileHandler.clearAllProfile();

		try {
			thinkerJsonReader.readAllProfiles("ideas");
		}catch (Exception ignored){}

		if(openByUser) onOpenByUser();
		buttonsHaveAnime.forEach(button-> button.updateInitTime(initTime));
		if(!selectedProfileID.equals(""))onProfileChanged(selectedProfileID);
	}

	public String l10n(String key){String text1= LanguageRegistry.instance().getStringLocalization(key);return text1.equals("")? key: text1;}
	protected void keyTyped(char p_73869_1_, int p_73869_2_)
	{
		if (p_73869_2_ == Keyboard.KEY_ESCAPE) close();
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
	protected void onProfileChanged(String newProfileID){
		lastProfileSelectedTime= System.currentTimeMillis();
		buttonList.removeAll(buttonsProfile);
		buttonsProfile= profileHandler.getProfile(newProfileID).buttons;
		buttonsProfile.forEach(button->button.updateInitTime(lastProfileSelectedTime));
		buttonList.addAll(buttonsProfile);
		profileHandler.onProfileChanged(newProfileID);
		dummyWorldHandler.onProfileChanged(newProfileID);
		((DummyWorld)buttonList.get(1)).onProfileChanged(lastProfileSelectedTime);
		selectedProfileID =newProfileID;
	}
	protected boolean onButtonPressed(GuiButton button) {
		((DummyWorld)buttonList.get(1)).clickOnOtherButton=button.id!=1;
		if(button.id==2) this.mc.displayGuiScreen(new ThinkerSettingsGui());
		if(button.id==3) {
			int mouseY=this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			if(Mouse.isInsideWindow())for (int i : profileHandler.displayProfileIDMap.keySet())if(mouseY>=YOffset+i*(16+ configHandler.themeSelectorProfileGap.get()) && mouseY<=YOffset+16+i*(16+ configHandler.themeSelectorProfileGap.get())){
				if(!Objects.equals(selectedProfileID, displayProfileIDMap.get(i))){
					onProfileChanged(displayProfileIDMap.get(i));
					onButtonPressed((GuiButton) buttonList.get(4));
					((DummyWorld)buttonList.get(1)).clickOnOtherButton=false;
				}
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
		if(button.id==ID_FOR_CUSTOM_BUTTONS)((ThinkerButton)button).onButtonPressed((Mouse.getX() * this.width / this.mc.displayWidth),(this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1));
		return true;
	}
	public void handleMouseInput(){
		super.handleMouseInput();
		int x = Mouse.getX() * this.width / this.mc.displayWidth;
		int y =this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
		if(((ThinkerButton)buttonList.get(3)).visible&&Mouse.isInsideWindow()&&Mouse.getEventDWheel()!=0&& x< profileHandler.profileLayer*32+32&& x>0)
			profileHandler.handleMouseWheel();
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
