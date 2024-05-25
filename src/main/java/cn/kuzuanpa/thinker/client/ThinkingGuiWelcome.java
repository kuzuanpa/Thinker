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

import cn.kuzuanpa.thinker.client.handler.profileHandler;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.IdummyWorldThinkerObject;
import cn.kuzuanpa.thinker.client.handler.dummyWorldHandler;
import cn.kuzuanpa.thinker.client.objects.gui.DummyWorldButton;
import cn.kuzuanpa.thinker.client.objects.gui.ThinkerButtonBase;
import cn.kuzuanpa.thinker.client.objects.gui.ThinkingBackground;
import cn.kuzuanpa.thinker.client.objects.gui.anime.animeRotateSteadily;
import cn.kuzuanpa.thinker.client.objects.gui.thinkerImage;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.kuzuanpa.thinker.client.handler.dummyWorldHandler.*;
import static cn.kuzuanpa.thinker.client.handler.keyBindHandler.keyThink;

/**
 * @author kuzuanpa
 */
@SideOnly(Side.CLIENT)
public class ThinkingGuiWelcome extends GuiScreen {

	private int displayWidth,displayHeight;
	public String selectedProfileID="HelloThinker";
	public boolean openByUser,themeSelectorFolded=false;
	public long initTime=0,lastProfileSelectedTime=0;
	private List<String> hoveringString=new ArrayList<>();
	protected List<ThinkerButtonBase> buttonsHaveAnime = new ArrayList<ThinkerButtonBase>();
	public ThinkingGuiWelcome() {
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
		dummyWorldObjects.clear();
		//buttonList.add(new ThinkerButton(-1,displayWidth-20,displayHeight-20,20,20,l10n("R")));
		buttonList.add(new ThinkingBackground(0, displayWidth,displayHeight));
		buttonList.add(new DummyWorldButton(1,0,0,displayWidth,displayHeight));
		buttonList.add(new thinkerImage(2,displayWidth-52,20,0,0,32,32,"textures/base.png", l10n("thinker.settings")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime));


		ArrayList<IdummyWorldThinkerObject> blocks=new ArrayList<>();
		profileHandler.clearAllProfile();
		profileHandler.addProfile(new profileHandler.thinkingProfile("test1",Items.string.getIconFromDamage(0),new thinkerImage(12,displayWidth-122,20,0,0,32,32,"textures/base.png", l10n("test")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime)));
		profileHandler.addProfile(new profileHandler.thinkingProfile("HelloThinker",Items.string.getIconFromDamage(0),blocks,new thinkerImage(13,displayWidth-122,20,0,0,32,32,"textures/base.png", l10n("test")).addAnime(new animeRotateSteadily(0.05F)).addToList(buttonsHaveAnime)));

		if(openByUser)postInit();
		List<ThinkerButtonBase> buttonsProfile= profileHandler.getProfile("HelloThinker").buttons;
		buttonList.addAll(buttonsProfile);
		profileHandler.onProfileChanged("HelloThinker");
		dummyWorldHandler.onProfileChanged("HelloThinker");
		((DummyWorldButton)buttonList.get(1)).onProfileChanged();
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
	protected boolean onButtonPressed(GuiButton button) {
		((DummyWorldButton)buttonList.get(1)).clickOnOtherButton=button.id!=1;
		return true;
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
	public long getTimer(){
		return System.currentTimeMillis()-initTime;
	}
}
