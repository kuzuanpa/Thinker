package cn.kuzuanpa.thinker.client.render.gui;

import cn.kuzuanpa.thinker.client.render.gui.anime.animeMoveLinear;
import cn.kuzuanpa.thinker.client.render.gui.anime.animeRotate;
import cn.kuzuanpa.thinker.client.render.gui.button.*;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

import static cn.kuzuanpa.thinker.client.keyBindHandler.keyThink;

public class ThinkerSettingsGui extends GuiScreen {


    private int displayWidth,displayHeight;
    private boolean openByUser=false;
    public long initTime=0;
    protected List<CommonGuiButton> buttonHaveAnimeList = new ArrayList();
    public ThinkerSettingsGui() {
        allowUserInput = false;
        this.openByUser=true;
    }
    public void postInitGui(){
        initTime=System.currentTimeMillis();
        openByUser=false;
    }
    public void initGui() {
        super.initGui();
        displayWidth= FMLClientHandler.instance().getClient().currentScreen.width;
        displayHeight= FMLClientHandler.instance().getClient().currentScreen.height;
        buttonHaveAnimeList.clear();
        buttonList.clear();
        buttonList.add(new ThinkingBackground(0, displayWidth,displayHeight));
        buttonList.add(new customImage(1,20,20,128,128,32,32,"textures/gui/think/base.png",""));
        buttonList.add(new customImage(1,displayWidth-52,20,0,0,32,32,"textures/gui/think/base.png","").addAnime(new animeMoveLinear(0,1000,-(displayWidth-72),0)).addAnime(new animeRotate(0,1000,-720)).addToList(buttonHaveAnimeList));
        buttonList.add(new CommonGuiButton(2, (int) (displayWidth*0.05),80,180,20,l10n("thinker.settings.background")));
        buttonList.add(new CommonGuiButton(3,(int) (displayWidth*0.05),105,180,20,l10n("thinker.settings.anime")));
        buttonList.add(new CommonGuiButton(3,(int) (displayWidth*0.05),130,180,20,l10n("thinker.settings.profile_selector")));
        buttonList.add(new CommonGuiButton(3,(int) (displayWidth*0.05),155,180,20,l10n("thinker.settings.dummy_world")));
        buttonList.add(new CommonGuiButton(4,(int) (displayWidth*0.05),180,180,20,l10n("thinker.settings.help")));
        buttonList.add(new CommonGuiButton(5,(int) (displayWidth*0.05),205,180,20,l10n("thinker.settings.contributors")));
        if(openByUser)postInitGui();
    }
    public String l10n(String key){String text1= LanguageRegistry.instance().getStringLocalization(key);if(text1.equals(""))return key;return text1;}

    protected void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        if (p_73869_2_ == 1|| p_73869_2_== keyThink.getKeyCode())close();
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
        System.out.println(button.id);
        if(button.id==1)close();
        return true;
    }
    public void updateScreen() {
        buttonHaveAnimeList.forEach(button-> button.doAnime(initTime));
    }
    public void close() {
        this.mc.displayGuiScreen(new ThinkingGuiMain());
    }
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
