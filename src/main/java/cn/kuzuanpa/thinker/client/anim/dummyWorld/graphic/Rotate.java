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
package cn.kuzuanpa.thinker.client.anim.dummyWorld.graphic;

import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import org.lwjgl.opengl.GL11;

import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getFloat;
import static cn.kuzuanpa.thinker.Thinker.getInt;

public class Rotate implements IDummyWorldGraphicAnime {
    public Rotate(int startTime, int endTime, float dRoll, float dPitch, float dYaw){
        this.startTime=startTime;
        this.endTime=endTime;
        this.dRoll =dRoll;
        this.dPitch =dPitch;
        this.dYaw= dYaw;
    }
    public int startTime, endTime;
    public float dRoll, dPitch, dYaw;
    @Override
    public boolean animeDraw(long timer) {
        if(timer<startTime) return false;
        float progress=(float)(timer - startTime)/(float)(endTime-startTime);
        if (timer < endTime) GL11.glTranslatef(progress * dPitch,progress * dYaw, progress*dRoll);
        else GL11.glTranslatef(dPitch, dYaw, dRoll);
        return false;
    }

    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("startTime")&&
                values.containsKey("endTime")&&
                values.containsKey("dRoll")&&
                values.containsKey("dPitch")&&
                values.containsKey("dYaw");
        if(!result)
            thinkerJsonReader.requestLogError("Not Enough contents for world.graphic.Rotate: startTime, endTime, dRoll, dPitch, dYaw");
        return result;
    }

    public static Rotate create(Map<String, Object> values) {
        return new Rotate(getInt(values.get("startTime")),getInt(values.get("endTime")),getFloat(values.get("dRoll")),getFloat(values.get("dPitch")),getFloat(values.get("dYaw")));
    }

}
