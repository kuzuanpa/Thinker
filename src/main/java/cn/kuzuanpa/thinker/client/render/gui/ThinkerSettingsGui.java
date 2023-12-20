package cn.kuzuanpa.thinker.client.render.gui;

import cn.kuzuanpa.thinker.client.configHandler;
import cn.kuzuanpa.thinker.client.render.gui.anime.*;
import cn.kuzuanpa.thinker.client.render.gui.button.*;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static cn.kuzuanpa.thinker.client.keyBindHandler.keyThink;

public class ThinkerSettingsGui extends GuiScreen {


    private int displayWidth,displayHeight;
    private boolean openByUser;
    public static float scrollInertia=6F,oldWheel=0F;
    public float YOffset=0;
    public long initTime=0;
    protected List<ThinkerButton> buttonsHaveAnime = new ArrayList();
    protected List<String> hoveringString = new ArrayList<>();
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
        buttonsHaveAnime.clear();
        buttonList.clear();
        buttonList.add(new ThinkingBackground(0, displayWidth,displayHeight));
        buttonList.add(new thinkerImage(1,displayWidth-52,20,128,384,384,32,"textures/gui/think/base.png",""));
        buttonList.add(new thinkerImage(2,displayWidth-52,20,0,0,32,32,"textures/gui/think/base.png","").addAnime(new animeMoveLinear(0,configHandler.getConfiguredAnimeTime(1000),-(displayWidth-56),-16)).addAnime(new animeRotate(0,configHandler.getConfiguredAnimeTime(1000),-720)).addToList(buttonsHaveAnime));
        buttonList.add(new ThinkerButton(3,5, 45,132,20,l10n("thinker.settings.HUD"))             .addAnime(new animeMoveLinear(-1,0,-600,0)).addAnime(new animeMoveLinear(0,configHandler.getConfiguredAnimeTime( 800),600,0)).addToList(buttonsHaveAnime));
        buttonList.add(new ThinkerButton(4,5, 70,132,20,l10n("thinker.settings.anime"))           .addAnime(new animeMoveLinear(-1,0,-550,0)).addAnime(new animeMoveLinear(0,configHandler.getConfiguredAnimeTime( 900),550,0)).addToList(buttonsHaveAnime));
        buttonList.add(new ThinkerButton(5,5, 95,132,20,l10n("thinker.settings.profile_selector")).addAnime(new animeMoveLinear(-1,0,-500,0)).addAnime(new animeMoveLinear(0,configHandler.getConfiguredAnimeTime(1000),500,0)).addToList(buttonsHaveAnime));
        buttonList.add(new ThinkerButton(6,5,120,132,20,l10n("thinker.settings.dummy_world"))     .addAnime(new animeMoveLinear(-1,0,-500,0)).addAnime(new animeMoveLinear(0,configHandler.getConfiguredAnimeTime(1100),500,0)).addToList(buttonsHaveAnime));
        buttonList.add(new ThinkerButton(7,5,145,132,20,l10n("thinker.settings.help"))            .addAnime(new animeMoveLinear(-1,0,-500,0)).addAnime(new animeMoveLinear(0,configHandler.getConfiguredAnimeTime(1200),500,0)).addToList(buttonsHaveAnime));
        buttonList.add(new thinkerImage(8,40,4,64,16,96,32,"textures/gui/think/base.png","").addAnime(new animeTransparency(-1,0,255,-255)).addAnime(new animeTransparency(configHandler.getConfiguredAnimeTime(1100),configHandler.getConfiguredAnimeTime(1800),0,255)).addToList(buttonsHaveAnime));
        buttonList.add( new NumberConfigButton(9 ,150,20 ,displayWidth-160,32,"ColorR",configHandler.HUDBackgroundColorR));
        buttonList.add( new NumberConfigButton(10,150,60 ,displayWidth-160,32,"ColorG",configHandler.HUDBackgroundColorG));
        buttonList.add( new NumberConfigButton(11,150,100,displayWidth-160,32,"ColorB",configHandler.HUDBackgroundColorB));
        buttonList.add( new NumberConfigButton(12,150,140,displayWidth-160,32,"ColorA",configHandler.HUDBackgroundColorA));
        buttonList.add( new NumberConfigButton(13,150,20,displayWidth-160,32,"anime Speed",configHandler.animeSpeed));
        buttonList.add(new BooleanConfigButton(14,150,20,displayWidth-160,32,"Freely Scroll",configHandler.themeSelectorFreelyScroll));
        buttonList.add( new NumberConfigButton(15,150,60,displayWidth-160,32,"Profile Gap",configHandler.themeSelectorProfileGap));
        buttonList.add( new NumberConfigButton(16,150,100,displayWidth-160,32,"Scroll Inertia",configHandler.themeSelectorScrollInertia));
        buttonList.add( new NumberConfigButton(17,150,140,displayWidth-160,32,"Scroll Speed",configHandler.themeSelectorScrollSpeed));

        if(openByUser)postInitGui();
        for (int i = 9; i < buttonList.size(); i++) {
            ((ThinkerButton) buttonList.get(i)).visible=false;
        }
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
                ThinkerButton guibutton = (ThinkerButton)this.buttonList.get(l);

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
        if(button.id==1||button.id==2)close();
        if(2<button.id&&button.id<9)updateButtonList(button.id);
        System.out.println(button.id);
        return true;
    }
    private void updateButtonList(int categoryId){
        YOffset=0;
        if(buttonList.size()>9)for (int i = 9; i < buttonList.size(); i++) {
            ((ThinkerButton) buttonList.get(i)).visible=false;
        }
        int starti=9,endi=10;
        switch (categoryId){
            case 3:starti=9;endi=12;break;
            case 4:starti=13;endi=13;break;
            case 5:starti=14;endi=17;break;
            default: return;
        }
        for (int i = starti; i <= endi; i++) {
            if(i>buttonList.size())return;
            ((ThinkerButton) buttonList.get(i)).visible=true;
        }
    }
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_){
        super.drawScreen(p_73863_1_,p_73863_2_,p_73863_3_);
        oldWheel+=oldWheel>0?-scrollInertia : scrollInertia;
        if(Math.abs(oldWheel)<= scrollInertia)oldWheel=0;
        YOffset+=oldWheel/300;
        if(YOffset>0)YOffset=0;
        buttonList.forEach(b->{
            if (b instanceof NumberConfigButton) {
                NumberConfigButton button = (NumberConfigButton) b;
                button.yPosition = (int) (YOffset + button.originalY);
            }else if(b instanceof BooleanConfigButton){
                BooleanConfigButton button = (BooleanConfigButton) b;
                button.yPosition = (int) (YOffset + button.originalY);
            }
        });
        if(!Mouse.isInsideWindow())return;
        int x = Mouse.getX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
        buttonList.forEach(b -> {
            if (!(b instanceof ThinkerButton)) return;
            ThinkerButton button = (ThinkerButton) b;
            if(!button.visible)return;
            if(button.updateHoverState(x,y))hoveringString= getButtonInfo(button.id);
        });
        if (hoveringString == null||hoveringString.stream().allMatch(string->string.equals(""))) return;
        drawHoveringText(hoveringString, x, y+5, fontRendererObj);
    }
    public List<String> getButtonInfo(int id){
        List<String> list=new ArrayList<>();
        switch (id){
            case 3:
                list.add(l10n("thinker.settings.HUD.info"));
                break;
            case 4:
                list.add(l10n("thinker.settings.anime.info"));
                break;
            case 5:
                list.add(l10n("thinker.settings.profile_selector.info"));
                break;
            case 6:
                list.add(l10n("thinker.settings.dummy_world.info"));
                break;
            case 7:
                list.add(l10n("thinker.settings.help.info"));
                break;
            case 8:
                list.add(l10n("thinker.settings.contributors.info"));
                break;
        }
        return list;
    }
    public void handleMouseInput(){
        super.handleMouseInput();
        int x = Mouse.getX() * this.width / this.mc.displayWidth;
        int y =this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
        if(Mouse.isInsideWindow()&&Mouse.getEventDWheel()!=0&& x>50)oldWheel+=Mouse.getEventDWheel();
    }
    public void updateScreen() {
        buttonsHaveAnime.forEach(button-> button.updateInitTime(initTime));
    }
    public void close() {
        this.mc.displayGuiScreen(new ThinkingGuiMain());
    }
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
