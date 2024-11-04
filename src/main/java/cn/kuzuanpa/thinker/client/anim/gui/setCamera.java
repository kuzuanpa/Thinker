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
import cn.kuzuanpa.thinker.client.objects.gui.ThinkerButtonBase;

import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getFloat;
import static cn.kuzuanpa.thinker.Thinker.getInt;

public class setCamera implements IGuiAnime {

    public setCamera(int time){
        this.time=time;
    }

    public int time;
    public float x =Float.MIN_VALUE, y=Float.MIN_VALUE, z=Float.MIN_VALUE, pitch=Float.MIN_VALUE, yaw=Float.MIN_VALUE, zoom=Float.MIN_VALUE;
    public boolean hasSet = false;

    @Override
    public void animeDraw(long time) {}

    @Override
    public void animeDrawPre(long time) {}

    @Override
    public void animeDrawAfter(long time) {}
    @Override
    public void updateButton(long time, ThinkerButtonBase button) {}
    @Override
    public String jsonName() {
        return "gui.setCamera";
    }

    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("time");
        if(!result) thinkerJsonReader.requestLogError("Not Enough contents for gui.SetCamera: time");
        return result;
    }

    public static setCamera create(Map<String, Object> values) {
        setCamera anime = new setCamera(getInt(values.get("time")));
        if(values.containsKey("pitch"))anime.pitch=getFloat(values.get("pitch"));
        if(values.containsKey("yaw"  ))anime.yaw  =getFloat(values.get("yaw"));
        if(values.containsKey("zoom" ))anime.zoom =getFloat(values.get("zoom"));
        if(values.containsKey("x"    ))anime.x    =getFloat(values.get("x"));
        if(values.containsKey("y"    ))anime.y    =getFloat(values.get("y"));
        if(values.containsKey("z"    ))anime.z    =getFloat(values.get("z"));
        return anime;
    }
}
