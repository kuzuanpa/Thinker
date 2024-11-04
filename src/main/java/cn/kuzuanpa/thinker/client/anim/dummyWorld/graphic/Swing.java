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

import static cn.kuzuanpa.thinker.Thinker.*;

public class Swing implements IDummyWorldGraphicAnime {
    public Swing(int startTime, int endTime, int count, float angle,boolean flashBack){
        this.startTime=startTime;
        this.endTime=endTime;
        this.count =count;
        this.angle =angle;
        this.flashBack=flashBack;
    }
    public int startTime, endTime, count;
    public float angle;
    public boolean flashBack;
    @Override
    public boolean animeDraw(long timer) {
        if(timer<startTime||timer>endTime) return false;
        if (count<=0) throw new IllegalArgumentException("Anime Swing: Count must >0!");
        float eachTime = (endTime-startTime)/(float)count;
        float progress = (timer%eachTime)/eachTime;
        if(!flashBack)progress=Math.abs(Math.abs(progress-0.5F)-0.5F);
        GL11.glRotatef(angle*progress,0,0,1);
        return false;
    }

    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("startTime")&&
                values.containsKey("endTime")&&
                values.containsKey("count")&&
                values.containsKey("angle");
        if(!result)
            thinkerJsonReader.requestLogError("Not Enough contents for world.graphic.Rotate: startTime, endTime, count, angle");
        return result;
    }

    public static Swing create(Map<String, Object> values) {
        boolean flashBack = false;
        if(values.containsKey("flashBack"))flashBack=getBoolean(values.get("flashBack"));
        return new Swing(getInt(values.get("startTime")),getInt(values.get("endTime")),getInt(values.get("count")),getFloat(values.get("angle")),flashBack);
    }

}
