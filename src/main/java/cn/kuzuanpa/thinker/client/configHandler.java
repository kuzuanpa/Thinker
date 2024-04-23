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


import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class configHandler {
    public static configNumber HUDBackgroundColorR =new configNumber(0,50,255);
    public static configNumber HUDBackgroundColorG =new configNumber(0,50,255);
    public static configNumber HUDBackgroundColorB =new configNumber(0,50,255);
    public static configNumber HUDBackgroundColorA =new configNumber(0,200,255);
    public static configNumber animeSpeed=new configNumber(0.01F,1.0F,10.0F);
    public static configBoolean themeSelectorFreelyScroll=new configBoolean(false,"freelyScroll","themeSelector","Can themes be scrolled outside of window");
    public static configBoolean welcome =new configBoolean(true,"welcome","main","Will thinker show the welcome screen");
    public static configNumber themeSelectorProfileGap =new configNumber(0,4,80);
    public static configNumber themeSelectorScrollSpeed =new configNumber(0.01F,1.0F,10.0F);

    public static configNumber themeSelectorScrollInertia =new configNumber(0.01F,8F,50.0F);
    public static configBoolean recordDummyWorldTickTooLong = new configBoolean(false,"recordDummyWorldTickTooLong","main","Will thinker show a message while a dummyWorld tick takes too long");

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
    }

    public static void saveAll(){
        welcome.save();
        themeSelectorFreelyScroll.save();
        recordDummyWorldTickTooLong.save();
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
        public String desc;
        public configNumber(int minValue, int value, int maxValue){
            this.minValue=minValue;
            this.maxValue=maxValue;
            this.value = value;
            desc="";
        }
        public configNumber(int minValue, int value, int maxValue, String desc){
            this.minValue=minValue;
            this.maxValue=maxValue;
            this.value = value;
            this.desc=desc;
        }
        public configNumber(float minValue, float value, float maxValue){
            this.minValue=minValue;
            this.maxValue=maxValue;
            this.value = value;
            this.desc="";
        }
        public configNumber(float minValue, float value, float maxValue, String desc){
            this.minValue=minValue;
            this.maxValue=maxValue;
            this.value = value;
            this.desc=desc;
        }
        public float get(){
            return value;
        }
        public int getI(){
            return (int) value;
        }
        public float min(){
            return minValue;
        }
        public int minI(){return (int) Math.ceil(minValue);}
        public float max(){
            return maxValue;
        }
        public int maxI(){return (int)Math.floor(maxValue);}
        public void update(float newValue){
            if(min()>newValue)newValue=min();
            else if(max()<newValue)newValue=max();
            this.value=newValue;
        }

    }
}
