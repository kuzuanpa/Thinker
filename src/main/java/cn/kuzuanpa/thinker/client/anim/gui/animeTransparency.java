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
package cn.kuzuanpa.thinker.client.anim.gui;

import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;

import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getInt;

public class animeTransparency extends animeRGBA implements IGuiAnime {
    public animeTransparency(int startTime, int endTime, int startA, int dA){
        super(startTime,endTime,255,255,255,startA,0,0,0,dA);
    }
    @Override
    public String jsonName() {
        return "Gui.Transparency";
    }


    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("startTime")&&
                values.containsKey("endTime")&&
                values.containsKey("originA")&&
                values.containsKey("dA");
        if(!result)
            thinkerJsonReader.requestLogError("Not Enough contents for gui.Transparency: startTime, endTime, originA, dA");
        return result;
    }

    public static animeTransparency create(Map<String, Object> values) {
        return new animeTransparency(getInt(values.get("startTime")),getInt(values.get("endTime")),getInt(values.get("originA")),getInt(values.get("dA")));
    }
}
