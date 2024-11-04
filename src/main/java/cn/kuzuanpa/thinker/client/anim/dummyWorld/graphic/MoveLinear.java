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

import static cn.kuzuanpa.thinker.Thinker.getInt;

public class MoveLinear implements IDummyWorldGraphicAnime {
    public MoveLinear(int startTime, int endTime, float dX, float dY, float dZ){
        this.startTime=startTime;
        this.endTime=endTime;
        this.dX =dX;
        this.dY =dY;
        this.dZ= dZ;
    }
    public int startTime, endTime;
    public float dX, dY, dZ;
    @Override
    public boolean animeDraw(long timer) {
        if(timer<startTime) return false;
        float progress=(float)(timer - startTime)/(float)(endTime-startTime);
        if (timer < endTime) GL11.glTranslatef(progress * dX,progress * dY, progress*dZ);
        else GL11.glTranslatef(dX, dY, dZ);
        return false;
    }

    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("startTime")&&
                values.containsKey("endTime")&&
                values.containsKey("dX")&&
                values.containsKey("dY")&&
                values.containsKey("dZ");
        if(!result)
            thinkerJsonReader.requestLogError("Not Enough contents for world.graphic.MoveLinear: startTime, endTime, dX, dY, dZ");
        return result;
    }

    public static MoveLinear create(Map<String, Object> values) {
        return new MoveLinear(getInt(values.get("startTime")),getInt(values.get("endTime")),getInt(values.get("dX")),getInt(values.get("dY")),getInt(values.get("dZ")));
    }

}
