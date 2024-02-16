package cn.kuzuanpa.thinker.client;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldBlock;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldTileEntity;
import cn.kuzuanpa.thinker.client.render.gui.button.ThinkerButton;
import net.minecraft.util.IIcon;
import org.lwjgl.input.Mouse;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class profileHandler {
    public static thinkingProfile selectedProfile;
    public static float oldWheel=0F,YOffset=0;
    private static HashMap<String,thinkingProfile> profileMap =new HashMap<>();
    public static Map<Integer,String> displayProfileIDMap = new HashMap<>();
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
    public static void onProfileChanged(String profileID){
        selectedProfile= profileMap.get(profileID);
    }
    public static void addProfile(thinkingProfile profile){ profileMap.put(profile.id,profile); refreshDisplayIDMap();}
    public static void addProfiles(List<thinkingProfile> profiles){profiles.forEach(profile->profileMap.put(profile.id,profile));refreshDisplayIDMap();}
    public static void clearAllProfile(){profileMap.clear();}
    public static void removeProfile(String id){profileMap.remove(id);}
    public static HashMap<String,thinkingProfile> getProfileMap(){return profileMap;}

    public static void refreshDisplayIDMap(){
        AtomicInteger i = new AtomicInteger(0);
        profileMap.forEach((k, v)->displayProfileIDMap.put(i.getAndIncrement(),k));
    }

    public static thinkingProfile getProfile(String id){return profileMap.get(id);}
    public static class thinkingProfile{
        public thinkingProfile(String id, ThinkerButton... buttons){this(id,null,0,0,0,0,buttons);}
        public thinkingProfile(String id, IIcon icon, ThinkerButton... buttons){this(id,icon,1,1,1,1,buttons);}
        public thinkingProfile(String id, IIcon icon,int iconRGBA, ThinkerButton... buttons){this(id,icon,(float)(iconRGBA >> 16 & 255)  / 255.0F,(iconRGBA >> 8 & 255) / 255.0F,(iconRGBA & 255) /255.0F,(float)(iconRGBA >> 24 & 255),buttons);}
        public thinkingProfile(String id, IIcon icon,short[] iconRGBA, ThinkerButton... buttons){this(id,icon,(float)iconRGBA[0] / 255.0F,(float)iconRGBA[1] / 255.0F,(float)iconRGBA[2] /255.0F,(float)iconRGBA[3] / 255.0F,buttons);}
        public thinkingProfile(String id, IIcon icon, float iconR, float iconG, float iconB, float iconA, ThinkerButton... buttons){
            this.id=id;
            this.disableDummyWorldRend=true;
            this.icon=icon;
            this.iconR=iconR;
            this.iconG=iconG;
            this.iconB=iconB;
            this.iconA=iconA;
            Collections.addAll(this.buttons, buttons);
        }

        public thinkingProfile(String id, List<ThinkerButton> buttons){this(id,null,0,0,0,0,buttons);}
        public thinkingProfile(String id, IIcon icon, List<ThinkerButton> buttons){this(id,icon,1,1,1,1,buttons);}
        public thinkingProfile(String id, IIcon icon,int iconRGBA, List<ThinkerButton> buttons){this(id,icon,(float)(iconRGBA >> 16 & 255)  / 255.0F,(iconRGBA >> 8 & 255) / 255.0F,(iconRGBA & 255) /255.0F,(float)(iconRGBA >> 24 & 255),buttons);}
        public thinkingProfile(String id, IIcon icon,short[] iconRGBA, List<ThinkerButton> buttons){this(id,icon,(float)iconRGBA[0] / 255.0F,(float)iconRGBA[1] / 255.0F,(float)iconRGBA[2] /255.0F,(float)iconRGBA[3] / 255.0F,buttons);}
        public thinkingProfile(String id, IIcon icon, float iconR, float iconG, float iconB, float iconA, List<ThinkerButton> buttons){
            this.id=id;
            this.disableDummyWorldRend=true;
            this.icon=icon;
            this.iconR=iconR;
            this.iconG=iconG;
            this.iconB=iconB;
            this.iconA=iconA;
            this.buttons= (ArrayList<ThinkerButton>) buttons;
        }

        public thinkingProfile(String id, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, ThinkerButton... buttons){this(id,null,0,0,0,0,blocks,tileEntities,buttons);}
        public thinkingProfile(String id, IIcon icon, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, ThinkerButton... buttons){this(id,icon,1,1,1,1,blocks,tileEntities,buttons);}
        public thinkingProfile(String id, IIcon icon, int iconRGBA, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, ThinkerButton... buttons){this(id,icon,(float)(iconRGBA >> 16 & 255)  / 255.0F,(iconRGBA >> 8 & 255) / 255.0F,(iconRGBA & 255) /255.0F,(float)(iconRGBA >> 24 & 255),blocks,tileEntities,buttons);}
        public thinkingProfile(String id, IIcon icon, short[] iconRGBA, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, ThinkerButton... buttons){this(id,icon,(float)iconRGBA[0] / 255.0F,(float)iconRGBA[1] / 255.0F,(float)iconRGBA[2] /255.0F,(float)iconRGBA[3] / 255.0F,blocks,tileEntities,buttons);}
        public thinkingProfile(String id, IIcon icon, float iconR, float iconG, float iconB, float iconA, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, ThinkerButton... buttons){
            this.id=id;
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


        public thinkingProfile(String id, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, List<ThinkerButton> buttons){this(id,null,0,0,0,0,blocks,tileEntities,buttons);}
        public thinkingProfile(String id, IIcon icon, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, List<ThinkerButton> buttons){this(id,icon,1,1,1,1,blocks,tileEntities,buttons);}
        public thinkingProfile(String id, IIcon icon, int iconRGBA, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, List<ThinkerButton> buttons){this(id,icon,(float)(iconRGBA >> 16 & 255)  / 255.0F,(iconRGBA >> 8 & 255) / 255.0F,(iconRGBA & 255) /255.0F,(float)(iconRGBA >> 24 & 255),blocks,tileEntities,buttons);}
        public thinkingProfile(String id, IIcon icon, short[] iconRGBA, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, List<ThinkerButton> buttons){this(id,icon,(float)iconRGBA[0] / 255.0F,(float)iconRGBA[1] / 255.0F,(float)iconRGBA[2] /255.0F,(float)iconRGBA[3] / 255.0F,blocks,tileEntities,buttons);}
        public thinkingProfile(String id, IIcon icon, float iconR, float iconG, float iconB, float iconA, HashMap<BlockPosition,dummyWorldBlock> blocks, HashMap<BlockPosition,dummyWorldTileEntity> tileEntities, List<ThinkerButton> buttons){
            this.id=id;
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
        public String id;
    }
}
