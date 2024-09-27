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
package cn.kuzuanpa.thinker.client.objects.gui.anime;

import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import cn.kuzuanpa.thinker.client.objects.gui.ThinkerButtonBase;
import org.lwjgl.opengl.GL11;

import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getFloat;
import static cn.kuzuanpa.thinker.Thinker.getInt;

public class animeScale implements IGuiAnime {
    public animeScale(int startTime,int endTime,float scaleRate,float scaleX,float scaleY){
        this.startTime=startTime;
        this.endTime=endTime;
        this.scaleRate=scaleRate;
        this.scaleX=scaleX;
        this.scaleY=scaleY;
    }
    public int startTime, endTime;
    public float scaleRate,scaleX,scaleY;
    @Override
    public void animeDraw(long timer) {
        if(timer<startTime) return;
        if(timer<endTime) GL11.glScalef(((float)(timer - startTime)/(float)(endTime-startTime))*scaleX*scaleRate,((float)(timer - startTime)/(float)(endTime-startTime))*scaleY*scaleRate,1);
        else GL11.glScalef(scaleX*scaleRate,scaleY*scaleRate,1);
    }

    @Override
    public void animeDrawPre(long time) {}

    @Override
    public void animeDrawAfter(long time) {}
    @Override
    public void updateButton(long time, ThinkerButtonBase button) {
    }
    @Override
    public String jsonName() {
        return "Gui.Scale";
    }

    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("startTime")&&
                values.containsKey("endTime")&&
                values.containsKey("scaleRate");
        if(!result)
            thinkerJsonReader.requestLogError("Not Enough contents for gui.Scale: startTime, endTime, scaleRate");
        return result;
    }

    public static animeScale create(Map<String, Object> values) {
        float scaleX=1.0F,scaleY=1.0F;
        if(values.containsKey("scaleX"))scaleX=getFloat(values.get("scaleX"));
        if(values.containsKey("scaleY"))scaleY=getFloat(values.get("scaleY"));

        return new animeScale(getInt(values.get("startTime")),getInt(values.get("endTime")),getFloat(values.get("scaleRate")),scaleX,scaleY);
    }
}
