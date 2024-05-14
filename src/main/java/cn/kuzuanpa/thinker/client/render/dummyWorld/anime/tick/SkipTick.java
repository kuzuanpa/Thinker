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

package cn.kuzuanpa.thinker.client.render.dummyWorld.anime.tick;

import net.minecraft.tileentity.TileEntity;

public class SkipTick implements IDummyWorldTileTickingAnime{
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
    public String jsonName() {
        return "Tick.skip";
    }

    @Override
    public boolean beforeTick(long time,TileEntity tileEntity) {
        if(startTime<time&&time<endTime&&(skipCount==-1||skippedTicks<skipCount)){
            skipCount++;
            return true;
        }
        return false;
    }
}
