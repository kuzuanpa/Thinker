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
package cn.kuzuanpa.thinker.client.handler;

import cn.kuzuanpa.thinker.client.render.dummyWorld.IdummyWorldThinkerObject;
import cn.kuzuanpa.thinker.client.render.gui.ThinkerButtonBase;
import net.minecraft.util.IIcon;
import org.lwjgl.input.Mouse;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class profileHandler {
    public static thinkingProfile selectedProfile;
    public static float oldWheel=0F,YOffset=0;
    private static final HashMap<String,thinkingProfile> profileMap =new HashMap<>();
    public static int profileLayer=1;
    public static LinkedHashMap<String,Object> rootDir=new LinkedHashMap<>();
    public static ArrayList<String> unfoldedDirs =new ArrayList<>();
    public static void handleMouseWheel(){
        oldWheel+=Mouse.getEventDWheel();
    }
    public static void tick(){

        oldWheel+=oldWheel>0?-configHandler.themeSelectorScrollInertia.get()*10F : configHandler.themeSelectorScrollInertia.get()*10F;
        if(Math.abs(oldWheel)<= configHandler.themeSelectorScrollInertia.get()*10F)oldWheel=0;
        YOffset+=oldWheel/300*(configHandler.themeSelectorScrollSpeed.get());
        if(!configHandler.themeSelectorFreelyScroll.get()&&(YOffset)>0){YOffset=0;oldWheel=0;return;}
        int i1=-((profileMap.size()-1)*(16+configHandler.themeSelectorProfileGap.getI()));
        if(!configHandler.themeSelectorFreelyScroll.get()&&(YOffset)<i1){YOffset=i1;oldWheel=0;}
    }
    public static void onProfileChanged(String profileID){
        selectedProfile= profileMap.get(profileID);
    }
    public static void addProfile(thinkingProfile profile){ profileMap.put(profile.id,profile);}
    public static void addProfiles(List<thinkingProfile> profiles){profiles.forEach(profile->profileMap.put(profile.id,profile));}
    public static void clearAllProfile(){profileMap.clear();}
    public static void removeProfile(String id){profileMap.remove(id);}
    public static HashMap<String,thinkingProfile> getProfileMap(){return profileMap;}

    public static thinkingProfile getProfile(String id){return profileMap.get(id);}
    public static boolean isItemHaveProfile(String itemId){return profileMap.values().stream().anyMatch(v-> v.bindItemId.equals(itemId));}
    public static thinkingProfile getProfileFromItem(String itemId){return profileMap.values().stream().filter(v->v.bindItemId.equalsIgnoreCase(itemId)).findFirst().orElse(null);}
    public static void buildDirTree(){
        rootDir.clear();
        profileMap.forEach((id,profile)->{
            if (profile.dir==null)rootDir.put(id,profile);
            else rootDir.putAll(sortMap(walkDirTree(profile.dir, profile.id,rootDir)));
        });
        rootDir=sortMap(rootDir);
    }

    public static LinkedHashMap<String,Object> sortMap(LinkedHashMap<String,Object> map){
        LinkedHashMap<String,Object> profiles = new LinkedHashMap<>();
        LinkedHashMap<String,Object> dirs = new LinkedHashMap<>();
        map.forEach((k,v)->{
            if(v instanceof thinkingProfile)profiles.put(k,v);
            if(v instanceof LinkedHashMap) {
                dirs.put(k,sortMap((LinkedHashMap<String,Object>) v));
            }
        });
        LinkedHashMap<String,Object> mapReturn = new LinkedHashMap<>();

        mapReturn.putAll(dirs);
        mapReturn.putAll(profiles);

        return mapReturn;
    }
    public static LinkedHashMap<String, Object> walkDirTree(String[] input, String profileID,LinkedHashMap<String,Object> upperDir){

        LinkedHashMap<String,Object> result = new LinkedHashMap<>();
        if(input.length>0&&upperDir.get(input[0]) instanceof LinkedHashMap){
            //currentDir already exist
            ((LinkedHashMap<String, Object>)upperDir.get(input[0])).putAll(walkDirTree(Arrays.copyOfRange(input, 1, input.length), profileID,(LinkedHashMap<String, Object>)upperDir.get(input[0])));
        }else {
            //no more dir, return profile
            if(input.length==0||input[0].equals(""))result.put(profileID,profileMap.get(profileID));
            //current Dir not exist, create new one
            else result.put(input[0],walkDirTree(Arrays.copyOfRange(input, 1, input.length), profileID,result));
        }
        return result;
    }
    public static class thinkingProfile {
        public thinkingProfile(String id, ThinkerButtonBase... buttons) {
            this(id, null, 0, 0, 0, 0, buttons);
        }

        public thinkingProfile(String id, IIcon icon, ThinkerButtonBase... buttons) {
            this(id, icon, 1, 1, 1, 1, buttons);
        }

        public thinkingProfile(String id, IIcon icon, int iconRGBA, ThinkerButtonBase... buttons) {
            this(id, icon, (float) (iconRGBA >> 16 & 255) / 255.0F, (iconRGBA >> 8 & 255) / 255.0F, (iconRGBA & 255) / 255.0F, (float) (iconRGBA >> 24 & 255), buttons);
        }

        public thinkingProfile(String id, IIcon icon, short[] iconRGBA, ThinkerButtonBase... buttons) {
            this(id, icon, (float) iconRGBA[0] / 255.0F, (float) iconRGBA[1] / 255.0F, (float) iconRGBA[2] / 255.0F, (float) iconRGBA[3] / 255.0F, buttons);
        }

        public thinkingProfile(String id, IIcon icon, float iconR, float iconG, float iconB, float iconA, ThinkerButtonBase... buttons) {
            this.id = id;
            this.disableDummyWorldRend = true;
            this.icon = icon;
            this.iconR = iconR;
            this.iconG = iconG;
            this.iconB = iconB;
            this.iconA = iconA;
            Collections.addAll(this.buttons, buttons);
        }

        public thinkingProfile(String id, List<ThinkerButtonBase> buttons) {
            this(id, null, 0, 0, 0, 0, buttons);
        }

        public thinkingProfile(String id, IIcon icon, List<ThinkerButtonBase> buttons) {
            this(id, icon, 1, 1, 1, 1, buttons);
        }

        public thinkingProfile(String id, IIcon icon, int iconRGBA, List<ThinkerButtonBase> buttons) {
            this(id, icon, (float) (iconRGBA >> 16 & 255) / 255.0F, (iconRGBA >> 8 & 255) / 255.0F, (iconRGBA & 255) / 255.0F, (float) (iconRGBA >> 24 & 255), buttons);
        }

        public thinkingProfile(String id, IIcon icon, short[] iconRGBA, List<ThinkerButtonBase> buttons) {
            this(id, icon, (float) iconRGBA[0] / 255.0F, (float) iconRGBA[1] / 255.0F, (float) iconRGBA[2] / 255.0F, (float) iconRGBA[3] / 255.0F, buttons);
        }

        public thinkingProfile(String id, IIcon icon, float iconR, float iconG, float iconB, float iconA, List<ThinkerButtonBase> buttons) {
            this.id = id;
            this.disableDummyWorldRend = true;
            this.icon = icon;
            this.iconR = iconR;
            this.iconG = iconG;
            this.iconB = iconB;
            this.iconA = iconA;
            this.buttons.addAll(buttons);
        }

        public thinkingProfile(String id, List<IdummyWorldThinkerObject> dummyWorldThinkerObjects, ThinkerButtonBase... buttons) {
            this(id, null, 0, 0, 0, 0, dummyWorldThinkerObjects, buttons);
        }

        public thinkingProfile(String id, IIcon icon, List<IdummyWorldThinkerObject> dummyWorldThinkerObjects, ThinkerButtonBase... buttons) {
            this(id, icon, 1, 1, 1, 1, dummyWorldThinkerObjects, buttons);
        }

        public thinkingProfile(String id, IIcon icon, int iconRGBA, List<IdummyWorldThinkerObject> dummyWorldThinkerObjects, ThinkerButtonBase... buttons) {
            this(id, icon, (float) (iconRGBA >> 16 & 255) / 255.0F, (iconRGBA >> 8 & 255) / 255.0F, (iconRGBA & 255) / 255.0F, (float) (iconRGBA >> 24 & 255), dummyWorldThinkerObjects, buttons);
        }

        public thinkingProfile(String id, IIcon icon, short[] iconRGBA, List<IdummyWorldThinkerObject> dummyWorldThinkerObjects, ThinkerButtonBase... buttons) {
            this(id, icon, (float) iconRGBA[0] / 255.0F, (float) iconRGBA[1] / 255.0F, (float) iconRGBA[2] / 255.0F, (float) iconRGBA[3] / 255.0F, dummyWorldThinkerObjects, buttons);
        }

        public thinkingProfile(String id, IIcon icon, float iconR, float iconG, float iconB, float iconA, List<IdummyWorldThinkerObject> dummyWorldThinkerObjects, ThinkerButtonBase... buttons) {
            this.id = id;
            this.disableDummyWorldRend = false;
            this.icon = icon;
            this.iconR = iconR;
            this.iconG = iconG;
            this.iconB = iconB;
            this.iconA = iconA;
            this.dummyWorldThinkerObjects.addAll(dummyWorldThinkerObjects);
            Collections.addAll(this.buttons, buttons);
        }

        public thinkingProfile(String id, IIcon icon, float iconR, float iconG, float iconB, float iconA, ArrayList<IdummyWorldThinkerObject> dummyWorldThinkerObjects, List<ThinkerButtonBase> buttons) {
            this.id = id;
            this.disableDummyWorldRend = false;
            this.icon = icon;
            this.iconR = iconR;
            this.iconG = iconG;
            this.iconB = iconB;
            this.iconA = iconA;
            this.dummyWorldThinkerObjects.addAll(dummyWorldThinkerObjects);
            this.buttons.addAll(buttons);
        }

        public final boolean disableDummyWorldRend;
        public final IIcon icon;
        public final float iconR, iconG, iconB, iconA;
        public final List<IdummyWorldThinkerObject> dummyWorldThinkerObjects = new ArrayList<>();
        public final ArrayList<ThinkerButtonBase> buttons = new ArrayList<>();
        public final String id;
        public String[] dir;
        public String bindItemId = "";

        public thinkingProfile setBindItemId(String unlocalizedName) {
            bindItemId = unlocalizedName;
            return this;
        }

        public thinkingProfile setDir(String[] dir) {
            this.dir = dir;
            return this;
        }
    }
}
