package cn.kuzuanpa.thinker.client;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldBlock;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldTileEntity;
import cn.kuzuanpa.thinker.client.render.gui.button.ThinkerButton;
import net.minecraft.util.IIcon;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class profileHandler {
    public static thinkingProfile selectedProfile;
    public static float oldWheel=0F,YOffset=0;
    public static ArrayList<thinkingProfile> profileList=new ArrayList<>();
    public static int profileLayer=1;
    public static void MouseWheelHandler(){
        oldWheel+=Mouse.getEventDWheel();
    }
    public static void tick(){
        oldWheel+=oldWheel>0?-configHandler.themeSelectorScrollInertia.get() : configHandler.themeSelectorScrollInertia.get();
        if(Math.abs(oldWheel)<= configHandler.themeSelectorScrollInertia.get())oldWheel=0;
        YOffset+=oldWheel/300*(configHandler.themeSelectorScrollSpeed.get());
        if(!configHandler.themeSelectorFreelyScroll.get()&&YOffset>0)YOffset=0;
    }
    public static void onProfileChanged(int profileID){
        selectedProfile=profileList.get(profileID);
    }
    public static class thinkingProfile{
        public thinkingProfile(ThinkerButton... buttons){this(null,0,0,0,0,buttons);}
        public thinkingProfile(IIcon icon, ThinkerButton... buttons){this(icon,1,1,1,1,buttons);}
        public thinkingProfile(IIcon icon,int iconRGBA, ThinkerButton... buttons){this(icon,(float)(iconRGBA >> 16 & 255)  / 255.0F,(iconRGBA >> 8 & 255) / 255.0F,(iconRGBA & 255) /255.0F,(float)(iconRGBA >> 24 & 255),buttons);}
        public thinkingProfile(IIcon icon,short[] iconRGBA, ThinkerButton... buttons){this(icon,(float)iconRGBA[0] / 255.0F,(float)iconRGBA[1] / 255.0F,(float)iconRGBA[2] /255.0F,(float)iconRGBA[3] / 255.0F,buttons);}
        public thinkingProfile(IIcon icon, float iconR, float iconG, float iconB, float iconA, ThinkerButton... buttons){
            this.disableDummyWorldRend=true;
            this.icon=icon;
            this.iconR=iconR;
            this.iconG=iconG;
            this.iconB=iconB;
            this.iconA=iconA;
            Collections.addAll(this.buttons, buttons);
        }

        public thinkingProfile(List<ThinkerButton> buttons){this(null,0,0,0,0,buttons);}
        public thinkingProfile(IIcon icon, List<ThinkerButton> buttons){this(icon,1,1,1,1,buttons);}
        public thinkingProfile(IIcon icon,int iconRGBA, List<ThinkerButton> buttons){this(icon,(float)(iconRGBA >> 16 & 255)  / 255.0F,(iconRGBA >> 8 & 255) / 255.0F,(iconRGBA & 255) /255.0F,(float)(iconRGBA >> 24 & 255),buttons);}
        public thinkingProfile(IIcon icon,short[] iconRGBA, List<ThinkerButton> buttons){this(icon,(float)iconRGBA[0] / 255.0F,(float)iconRGBA[1] / 255.0F,(float)iconRGBA[2] /255.0F,(float)iconRGBA[3] / 255.0F,buttons);}
        public thinkingProfile(IIcon icon, float iconR, float iconG, float iconB, float iconA, List<ThinkerButton> buttons){
            this.disableDummyWorldRend=true;
            this.icon=icon;
            this.iconR=iconR;
            this.iconG=iconG;
            this.iconB=iconB;
            this.iconA=iconA;
            this.buttons= (ArrayList<ThinkerButton>) buttons;
        }

        public thinkingProfile(HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, ThinkerButton... buttons){this(null,0,0,0,0,blocks,tileEntities,buttons);}
        public thinkingProfile(IIcon icon, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, ThinkerButton... buttons){this(icon,1,1,1,1,blocks,tileEntities,buttons);}
        public thinkingProfile(IIcon icon, int iconRGBA, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, ThinkerButton... buttons){this(icon,(float)(iconRGBA >> 16 & 255)  / 255.0F,(iconRGBA >> 8 & 255) / 255.0F,(iconRGBA & 255) /255.0F,(float)(iconRGBA >> 24 & 255),blocks,tileEntities,buttons);}
        public thinkingProfile(IIcon icon, short[] iconRGBA, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, ThinkerButton... buttons){this(icon,(float)iconRGBA[0] / 255.0F,(float)iconRGBA[1] / 255.0F,(float)iconRGBA[2] /255.0F,(float)iconRGBA[3] / 255.0F,blocks,tileEntities,buttons);}
        public thinkingProfile(IIcon icon, float iconR, float iconG, float iconB, float iconA, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, ThinkerButton... buttons){
            this.disableDummyWorldRend=false;
            this.icon=icon;
            this.iconR=iconR;
            this.iconG=iconG;
            this.iconB=iconB;
            this.iconA=iconA;
            this.dummyWorldBlocks=blocks;
            this.dummyWorldTileEntities=tileEntities;
            Collections.addAll(this.buttons, buttons);
        }


        public thinkingProfile(HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, List<ThinkerButton> buttons){this(null,0,0,0,0,blocks,tileEntities,buttons);}
        public thinkingProfile(IIcon icon, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, List<ThinkerButton> buttons){this(icon,1,1,1,1,blocks,tileEntities,buttons);}
        public thinkingProfile(IIcon icon, int iconRGBA, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, List<ThinkerButton> buttons){this(icon,(float)(iconRGBA >> 16 & 255)  / 255.0F,(iconRGBA >> 8 & 255) / 255.0F,(iconRGBA & 255) /255.0F,(float)(iconRGBA >> 24 & 255),blocks,tileEntities,buttons);}
        public thinkingProfile(IIcon icon, short[] iconRGBA, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, List<ThinkerButton> buttons){this(icon,(float)iconRGBA[0] / 255.0F,(float)iconRGBA[1] / 255.0F,(float)iconRGBA[2] /255.0F,(float)iconRGBA[3] / 255.0F,blocks,tileEntities,buttons);}
        public thinkingProfile(IIcon icon, float iconR, float iconG, float iconB, float iconA, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, List<ThinkerButton> buttons){
            this.disableDummyWorldRend=false;
            this.icon=icon;
            this.iconR=iconR;
            this.iconG=iconG;
            this.iconB=iconB;
            this.iconA=iconA;
            this.dummyWorldBlocks=blocks;
            this.dummyWorldTileEntities=tileEntities;
            this.buttons= (ArrayList<ThinkerButton>) buttons;
        }
        public boolean disableDummyWorldRend=false;
        public IIcon icon;
        public float iconR,iconG,iconB,iconA;
        public HashMap<BlockPosition,dummyWorldBlock> dummyWorldBlocks = new HashMap<>();
        public HashMap<BlockPosition, dummyWorldTileEntity> dummyWorldTileEntities = new HashMap<>();
        public ArrayList<ThinkerButton> buttons = new ArrayList<>();
    }
}
