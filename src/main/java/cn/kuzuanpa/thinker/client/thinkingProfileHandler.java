package cn.kuzuanpa.thinker.client;

import cn.kuzuanpa.thinker.client.render.gui.button.CommonGuiButton;
import net.minecraft.util.IIcon;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Collections;

public class thinkingProfileHandler {
    public static int selectedProfile=0;
    public static float oldWheel=0F,YOffset=0;
    public static ArrayList<thinkingProfile> profileList=new ArrayList<>();
    public static int profileLayer=1;
    public static void MouseClickHandler(int mouseY){
        if(Mouse.isInsideWindow())for (int i=0;i<profileList.size();i++)if(mouseY>=YOffset+i*(16+ configHandler.themeSelectorProfileGap.get()) && mouseY<=YOffset+16+i*(16+ configHandler.themeSelectorProfileGap.get())){
            selectedProfile=i;
            break;
        }
        System.out.println(selectedProfile);
    }
    public static void MouseWheelHandler(){
        oldWheel+=Mouse.getEventDWheel();
    }
    public static void tick(){
        oldWheel+=oldWheel>0?-configHandler.themeSelectorScrollInertia.get() : configHandler.themeSelectorScrollInertia.get();
        if(Math.abs(oldWheel)<= configHandler.themeSelectorScrollInertia.get())oldWheel=0;
        YOffset+=oldWheel/300*(configHandler.themeSelectorScrollSpeed.get());
        if(!configHandler.themeSelectorFreelyScroll.get()&&YOffset>0)YOffset=0;
    }
    public static class thinkingProfile{
        public thinkingProfile(boolean disableDummyWorldRend, CommonGuiButton... buttons){
            this.disableDummyWorldRend=disableDummyWorldRend;
            this.icon=null;
            this.iconR=0;
            this.iconG=0;
            this.iconB=0;
            this.iconA=0;
            Collections.addAll(this.buttons, buttons);
        }
        public thinkingProfile(boolean disableDummyWorldRend, IIcon icon, CommonGuiButton... buttons){
            this.disableDummyWorldRend=disableDummyWorldRend;
            this.icon=icon;
            this.iconR=1;
            this.iconG=1;
            this.iconB=1;
            this.iconA=1;
            Collections.addAll(this.buttons, buttons);
        }
        public thinkingProfile(boolean disableDummyWorldRend, IIcon icon,int iconRGBA, CommonGuiButton... buttons){
            this.disableDummyWorldRend=disableDummyWorldRend;
            this.icon=icon;
            this.iconR=(float)(iconRGBA >> 16 & 255) / 255.0F;
            this.iconG=(float)(iconRGBA >> 8 & 255) / 255.0F;
            this.iconB=(float)(iconRGBA & 255) / 255.0F;
            this.iconA=(float)(iconRGBA >> 24 & 255) / 255.0F;
            Collections.addAll(this.buttons, buttons);
        }
        public thinkingProfile(boolean disableDummyWorldRend, IIcon icon,short[] iconRGBA, CommonGuiButton... buttons){
            this.disableDummyWorldRend=disableDummyWorldRend;
            this.icon=icon;
            this.iconR=(float)iconRGBA[0] / 255.0F;
            this.iconG=(float)iconRGBA[1] / 255.0F;
            this.iconB=(float)iconRGBA[2] / 255.0F;
            this.iconA=(float)iconRGBA[3]/ 255.0F;
            Collections.addAll(this.buttons, buttons);
        }
        public thinkingProfile(boolean disableDummyWorldRend, IIcon icon,float iconR,float iconG,float iconB,float iconA, CommonGuiButton... buttons){
            this.disableDummyWorldRend=disableDummyWorldRend;
            this.icon=icon;
            this.iconR=iconR;
            this.iconG=iconG;
            this.iconB=iconB;
            this.iconA=iconA;
            Collections.addAll(this.buttons, buttons);
        }
        public boolean disableDummyWorldRend=false;
        public IIcon icon;
        public float iconR,iconG,iconB,iconA;
        public ArrayList<CommonGuiButton> dummyWorldBlocks = new ArrayList<>();
        public ArrayList<CommonGuiButton> buttons = new ArrayList<>();
    }
}
