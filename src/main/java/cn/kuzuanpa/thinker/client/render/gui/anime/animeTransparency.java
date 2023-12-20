package cn.kuzuanpa.thinker.client.render.gui.anime;

public class animeTransparency extends animeRGBA implements IGuiAnime {
    public animeTransparency(int startTime, int endTime, int startA, int dA){
        super(startTime,endTime,255,255,255,startA,0,0,0,dA);
    }
    @Override
    public String jsonName() {
        return "Gui.Transparency";
    }
}
