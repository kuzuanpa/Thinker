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


import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class configHandler {
    public static configNumber HUDBackgroundColorR =new configNumber(0,50,255,"BackgroundColorR","Red Color for Background");
    public static configNumber HUDBackgroundColorG =new configNumber(0,50,255,"BackgroundColorG","Green Color for Background");
    public static configNumber HUDBackgroundColorB =new configNumber(0,50,255,"BackgroundColorB","Blue Color for Background");
    public static configNumber HUDBackgroundColorA =new configNumber(0,200,255,"BackgroundTransparency","Background Transparency Level");
    public static configNumber animeSpeed=new configNumber(0.01F,1.0F,10.0F,"animeSpeed");
    public static configBoolean themeSelectorFreelyScroll=new configBoolean(false,"freelyScroll","themeSelector","Can themes be scrolled outside of window");
    public static configBoolean themeSelectorAutoFold=new configBoolean(true,"autoFold","themeSelector","will selector fold after select profile");
    public static configBoolean welcome =new configBoolean(true,"welcome","main","Will thinker show the welcome screen");
    public static configNumber themeSelectorProfileGap =new configNumber(0,4,80,"profileGap","themeSelector");
    public static configNumber themeSelectorScrollSpeed =new configNumber(0.01F,1.5F,10.0F,"scrollSpeed","themeSelector");

    public static configNumber themeSelectorScrollInertia =new configNumber(0.01F,1.0F,20.0F,"scrollInertia","themeSelector");
    public static configNumber keyPressedTimeNeededToStartThink =new configNumber(10,100,500,"holdTimeNeededToStartThink");
    public static configBoolean recordDummyWorldTickTooLong = new configBoolean(false,"recordDummyWorldTickTooLong","main","Will thinker show a message while a dummyWorld tick takes too long");
    public static configBoolean displayItemStackUnlocalizedName = new configBoolean(false,"displayItemStackUnlocalizedName","main","Will every ItemStack show their Unlocalized Name in tooltip");

    public static int getConfiguredAnimeTime(int originalTime){return (int) (originalTime*((float)1/animeSpeed.get()));}
    public static float getConfiguredAnimeTime(float originalTime){return originalTime*((float)1/animeSpeed.get());}
    public static Configuration config;
    public static boolean needSave =false;
    public static void preInit(FMLPreInitializationEvent event){
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();


        welcome.load();
        themeSelectorFreelyScroll.load();
        recordDummyWorldTickTooLong.load();
        displayItemStackUnlocalizedName.load();
        themeSelectorAutoFold.load();
    }

    public static void saveAll(){
        welcome.save();
        themeSelectorFreelyScroll.save();
        recordDummyWorldTickTooLong.save();
        displayItemStackUnlocalizedName.save();
        themeSelectorAutoFold.save();
        config.save();
        needSave = false;
    }
    public static class configBoolean {
        private boolean value;
        public String forgeConfigCategory="main";
        public String forgeConfigComment="";
        public String name;
        public configBoolean(boolean value, String name){
            this.value=value;
            this.name = name;
        }
        public configBoolean(boolean value, String name,String forgeConfigCategory){
            this.value=value;
            this.name = name;
            this.forgeConfigCategory=forgeConfigCategory;
        }
        public configBoolean(boolean value, String name,String forgeConfigCategory,String forgeConfigComment){
            this.value=value;
            this.name = name;
            this.forgeConfigCategory=forgeConfigCategory;
            this.forgeConfigComment=forgeConfigComment;
        }

        public boolean get(){return value;}
        public void set(boolean value){this.value=value;needSave=true;}
        public boolean load(){
            this.value = config.getBoolean(name, forgeConfigCategory, value, forgeConfigComment);
            return value;
        }
        public boolean save(){
            config.get(forgeConfigCategory, name  , value, forgeConfigComment).set(value);
            config.save();
            return value;
        }
    }

    public static class configNumber {
        private final float minValue;
        private final float maxValue;
        private float value;
        public boolean isIntegerConfig = false;
        public String desc;
        public String forgeConfigCategory="main";
        public String name;
        public configNumber(int minValue, int value, int maxValue, String name){
            this.minValue=minValue;
            this.maxValue=maxValue;
            this.value = value;
            this.name=name;
            desc="";
            isIntegerConfig =true;
        }
        public configNumber(int minValue, int value, int maxValue,String name, String desc){
            this.minValue=minValue;
            this.maxValue=maxValue;
            this.value = value;
            this.name=name;
            this.desc=desc;
            isIntegerConfig =true;
        }
        public configNumber(int minValue, int value, int maxValue,String name, String category, String desc){
            this.minValue=minValue;
            this.maxValue=maxValue;
            this.value = value;
            this.name=name;
            this.forgeConfigCategory=category;
            this.desc=desc;
            isIntegerConfig =true;
        }
        //float
        public configNumber(float minValue, float value, float maxValue, String name){
            this.minValue=minValue;
            this.maxValue=maxValue;
            this.value = value;
            this.name=name;
            this.desc="";
        }
        public configNumber(float minValue, float value, float maxValue, String name, String desc){
            this.minValue=minValue;
            this.maxValue=maxValue;
            this.value = value;
            this.name=name;
            this.desc=desc;
        }
        public configNumber(float minValue, float value, float maxValue, String name, String category, String desc){
            this.minValue=minValue;
            this.maxValue=maxValue;
            this.value = value;
            this.name=name;
            this.forgeConfigCategory=category;
            this.desc=desc;
        }

        public configNumber setInteger(boolean isIntegerConfig){
            this.isIntegerConfig=isIntegerConfig;
            return this;
        }
        public float get(){return isIntegerConfig?getI():value;}
        public int getI(){
            return (int) value;
        }
        public float min(){
            return isIntegerConfig?minI():minValue;
        }
        public int minI(){return (int) Math.ceil(minValue);}
        public float max(){
            return isIntegerConfig?maxI():maxValue;
        }
        public int maxI(){return (int)Math.floor(maxValue);}
        public void update(float newValue){
            if(min()>newValue)newValue=min();
            else if(max()<newValue)newValue=max();
            this.value=newValue;
            needSave=true;
        }
        public float load(){
            this.value = isIntegerConfig?config.getInt(name, forgeConfigCategory, getI(), minI(),maxI(),desc):config.getFloat(name, forgeConfigCategory, get(), min(),max(),desc);
            return value;
        }
        public float save(){
            config.get(forgeConfigCategory, name  , get(), desc).set(this.get());
            config.save();
            return value;
        }


    }
}
