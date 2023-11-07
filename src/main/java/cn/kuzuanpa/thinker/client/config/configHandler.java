package cn.kuzuanpa.thinker.client.config;



public class configHandler {
    public static configNumber HUDBackgroundColorR =new configNumber(0,50,255);
    public static configNumber HUDBackgroundColorG =new configNumber(0,50,255);
    public static configNumber HUDBackgroundColorB =new configNumber(0,50,255);
    public static configNumber HUDBackgroundColorA =new configNumber(0,200,255);
    public static configNumber animeSpeed=new configNumber(0.01F,1.0F,100.0F);
    public static configBoolean themeSelectorFreelyScroll=new configBoolean(false);
    public static configNumber themeSelectorProfileGap =new configNumber(0,4,200);
    public static configNumber themeSelectorScrollInertia =new configNumber(0.01F,200,100.0F);

    public static int getConfiguredAnimeTime(int originalTime){return (int) (originalTime*((float)1/animeSpeed.get()));}
    public static float getConfiguredAnimeTime(float originalTime){return originalTime*((float)1/animeSpeed.get());}

    public static class configBoolean {
        private boolean value;
        public String desc;
        public configBoolean(boolean value){
            this.value=value;
            this.desc="";
        }
        public configBoolean(boolean value, String desc){
            this.value=value;
            this.desc=desc;
        }

        public boolean get(){return value;}
        public void set(boolean value){this.value=value;}
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
            if(min()>newValue){newValue=min();return;}
            if(max()<newValue){newValue=max();return;}
            this.value=newValue;
        }

    }
}
