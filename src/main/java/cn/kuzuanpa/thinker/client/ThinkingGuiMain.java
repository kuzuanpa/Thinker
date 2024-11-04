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

package cn.kuzuanpa.thinker.client;

import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.client.handler.configHandler;
import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import cn.kuzuanpa.thinker.client.handler.dummyWorldHandler;
import cn.kuzuanpa.thinker.client.objects.gui.*;
import cn.kuzuanpa.thinker.client.anim.gui.animeMoveLinear;
import cn.kuzuanpa.thinker.client.anim.gui.animeRotateSteadily;
import cn.kuzuanpa.thinker.client.handler.profileHandler;
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

import static cn.kuzuanpa.thinker.client.handler.profileHandler.*;


/**
 * @author kuzuanpa
 */
@SideOnly(Side.CLIENT)
public class ThinkingGuiMain extends GuiScreen {

	private int displayWidth,displayHeight;
	public String selectedProfileID ="";
	public boolean openByUser,themeSelectorFolded=false;
	public long initTime=0;
	public final int ID_FOR_CUSTOM_BUTTONS=10;
	private List<String> hoveringString=new ArrayList<>();
	protected List<ThinkerButtonBase> buttonsHaveAnime = new ArrayList<ThinkerButtonBase>();
	public static ArrayList<ThinkerButtonBase> buttonsProfile= new ArrayList<ThinkerButtonBase>();
	public ThinkingGuiMain() {
		openByUser=true;
		allowUserInput = false;
		selectedProfileID = "";
		selectedProfile=null;
	}
	public ThinkingGuiMain(ItemStack item) {
		openByUser=true;
		allowUserInput = false;
		selectedProfileID = profileHandler.getProfileFromItem(item.getUnlocalizedName()).id;
	}
	public ThinkingGuiMain(String id) {
		openByUser=true;
		allowUserInput = false;
		selectedProfileID =id;
	}
	public DummyWorldButton worldButton= new DummyWorldButton(1,0,0,displayWidth,displayHeight);
	public void onOpenByUserAfter(){
		initTime=System.currentTimeMillis();
		openByUser=false;
		if(!selectedProfileID.isEmpty()){
			onProfileChanged(selectedProfileID);
			foldThemeSelector(true);
		}
	}
	public void initGui() {
		super.initGui();
		profileHandler.clearAllProfile();

		try {
			thinkerJsonReader.readAllProfiles("ideas");
		}catch (Exception ignored){}

		displayWidth= FMLClientHandler.instance().getClient().currentScreen.width;
		displayHeight= FMLClientHandler.instance().getClient().currentScreen.height;
		buttonList.clear();
		buttonsHaveAnime.clear();
		if (openByUser) worldButton=new DummyWorldButton(1,0,0,displayWidth,displayHeight);
		else worldButton.resizeToScreen(displayWidth,displayHeight);
		buttonList.add(new ThinkingBackground(0, displayWidth,displayHeight));
		buttonList.add(worldButton.addToList(buttonsHaveAnime));
		buttonList.add(new thinkerImage(2,displayWidth-52,20,0,0,32,32,"textures/base.png", l10n("thinker.settings")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime));
		buttonList.add(new ThinkingProfileList(3,0,0,displayHeight).addToList(buttonsHaveAnime));
		buttonList.add(new thinkerImage(4,69+ 8*currentProfileLayer,0,0,32,16,16,"textures/base.png", l10n("thinker.list.fold")).addToList(buttonsHaveAnime));
		buttonList.add(new thinkerImage(5,-16,0,16,32,16,16,"textures/base.png",l10n("thinker.list.unfold")).addToList(buttonsHaveAnime));

		if(openByUser) onOpenByUserAfter();
		else if(themeSelectorFolded)foldThemeSelector(true);
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
        	ThinkerButtonBase guibutton = (ThinkerButtonBase)this.buttonList.get(l);
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
		initTime=System.currentTimeMillis();

		buttonsHaveAnime.removeAll(buttonsProfile);
		buttonList.removeAll(buttonsProfile);
		buttonsProfile= profileHandler.getProfile(newProfileID).buttons;
		buttonList.addAll(buttonsProfile);
		buttonsHaveAnime.addAll(buttonsProfile);
		profileHandler.onProfileChanged(newProfileID);
		dummyWorldHandler.onProfileChanged(newProfileID);
		((DummyWorldButton)buttonList.get(1)).onProfileChanged();
		selectedProfileID =newProfileID;
	}
	protected boolean onButtonPressed(GuiButton button) {
		((DummyWorldButton)buttonList.get(1)).clickOnOtherButton=button.id!=1&&button.id<10;
		if(button.id==2) this.mc.displayGuiScreen(new ThinkerSettingsGui());
		if(button.id==3) {
			int mouseY=this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			if(Mouse.isInsideWindow()){
				String newID=((ThinkingProfileList) button).onMouseClick(mouseY, (thinkerImage) buttonList.get(4));
				if(!Objects.equals(selectedProfileID, newID)&&profileHandler.getProfile(newID)!=null){
					onProfileChanged(newID);
					if(configHandler.themeSelectorAutoFold.get())foldThemeSelector(false);
					((DummyWorldButton)buttonList.get(1)).clickOnOtherButton=false;
				}
			}
		}
		if(button.id==4&&!themeSelectorFolded) foldThemeSelector(false);
		if(button.id==5&&themeSelectorFolded) unfoldThemeSelector();
		if(button.id==ID_FOR_CUSTOM_BUTTONS)((ThinkerButtonBase)button).onButtonPressed((Mouse.getX() * this.width / this.mc.displayWidth),(this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1));
		return true;
	}
	public void foldThemeSelector(boolean immediately){
		((ThinkerButtonBase)buttonList.get(3)).getGuiAnimeList().clear();
		((ThinkerButtonBase)buttonList.get(4)).getGuiAnimeList().clear();
		((ThinkerButtonBase)buttonList.get(5)).getGuiAnimeList().clear();
		((ThinkerButtonBase)buttonList.get(3)).addAnime(new animeMoveLinear((int) getTimer()-(immediately?10:0), (int) (getTimer()+ (immediately?0:configHandler.getConfiguredAnimeTime(500))),-80-8*currentProfileLayer,0));
		((ThinkerButtonBase)buttonList.get(4)).addAnime(new animeMoveLinear((int) getTimer()-(immediately?10:0), (int) (getTimer()+ (immediately?0:configHandler.getConfiguredAnimeTime(500))),-80-8*currentProfileLayer,0));
		((ThinkerButtonBase)buttonList.get(5)).addAnime(new animeMoveLinear((int) getTimer()-(immediately?10:0), (int) (getTimer()+ (immediately?0:configHandler.getConfiguredAnimeTime(200))),16,0));
		themeSelectorFolded=true;
	}
	public void unfoldThemeSelector(){
		((ThinkerButtonBase)buttonList.get(3)).addAnime(new animeMoveLinear((int) getTimer(), (int) (getTimer()+ configHandler.getConfiguredAnimeTime(500)),80+8*currentProfileLayer,0));
		((ThinkerButtonBase)buttonList.get(4)).addAnime(new animeMoveLinear((int) getTimer(), (int) (getTimer()+ configHandler.getConfiguredAnimeTime(500)),80+8*currentProfileLayer,0));
		((ThinkerButtonBase)buttonList.get(5)).addAnime(new animeMoveLinear((int) getTimer(), (int) (getTimer()+ configHandler.getConfiguredAnimeTime(200)),-16,0));
		themeSelectorFolded=false;
	}
	public void handleMouseInput(){
		super.handleMouseInput();
		int x = Mouse.getX() * this.width / this.mc.displayWidth;
		int y =this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
		if(((ThinkerButtonBase)buttonList.get(3)).visible&&Mouse.isInsideWindow()&&Mouse.getEventDWheel()!=0&& x< profileHandler.profileLayer*32+32&& x>0)
			profileHandler.handleMouseWheel();
	}
	public long getTimer(){
		return System.currentTimeMillis()-initTime;
	}
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_){
		this.buttonsHaveAnime.forEach(b -> b.updateTimer(getTimer()));
		super.drawScreen(p_73863_1_,p_73863_2_,p_73863_3_);
		profileHandler.tick();

		if(!Mouse.isInsideWindow())return;
		int x = Mouse.getX() * this.width / this.mc.displayWidth;
		int y = this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
		buttonList.forEach(b -> {
			if (!(b instanceof ThinkerButtonBase)) return;
			ThinkerButtonBase button = (ThinkerButtonBase) b;
			if(!button.visible)return;
			if(button.updateHoverState(x,y))hoveringString= Collections.singletonList(button.displayString);
		});
		if (hoveringString == null||hoveringString.stream().allMatch(String::isEmpty)) return;
		drawHoveringText(hoveringString, x, y+5, fontRendererObj);
	}
	public boolean close() {
		profileHandler.oldWheel=0;
		Thinker.dummyWorldTickThread.setTrackedDummyWorld(null);
		this.mc.displayGuiScreen(null);
		this.mc.setIngameFocus();
		return true;
	}
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}
