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

package cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.tick;

import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.IdummyWorldThinkerObject;
import net.minecraft.world.World;

import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.*;

public class SkipTick implements IDummyWorldTickingAnime {
    public SkipTick(long startTime,long endTime){
        this.startTime = startTime;
        this.endTime=endTime;
        this.skipCount=-1;
    }
    /**
     * @param skipCount only skip this num of ticks even within the time. -1 for infinity.
     */
    public SkipTick(long startTime,long endTime, long skipCount){
        this.startTime = startTime;
        this.endTime=endTime;
        this.skipCount=skipCount;
    }
    long startTime, endTime,  skipCount,skippedTicks=0;
    @Override
    public boolean beforeTick(long time, IdummyWorldThinkerObject tileEntity, World world) {
        if(startTime<time&&time<endTime&&(skipCount==-1||skippedTicks<skipCount)){
            skippedTicks++;
            return true;
        }
        return false;
    }
    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("startTime")&&
                values.containsKey("endTime");
        if(!result) thinkerJsonReader.requestLogError("Not Enough contents for world.tick.skip: startTime, endTime");
        return result;
    }

    public static SkipTick create(Map<String, Object> values) {
        return new SkipTick(getInt(values.get("startTime")),getInt(values.get("endTime")), getLong(values.getOrDefault("skipCount","-1")));
    }
}
