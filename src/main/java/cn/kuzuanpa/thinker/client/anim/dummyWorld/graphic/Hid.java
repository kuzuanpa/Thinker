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

import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getInt;

public class Hid implements IDummyWorldGraphicAnime {
    public Hid(int startTime, int endTime){
        this.startTime=startTime;
        this.endTime=endTime;
    }
    public int startTime, endTime;
    @Override
    public boolean animeDraw(long timer) {
        if(timer<startTime) return false;
        return timer < endTime;
    }

    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("startTime")&&
                values.containsKey("endTime");
        if(!result)
            thinkerJsonReader.requestLogError("Not Enough contents for world.graphic.hid: startTime, endTime");
        return result;
    }

    public static Hid create(Map<String, Object> values) {
        return new Hid(getInt(values.get("startTime")),getInt(values.get("endTime")));
    }

}
