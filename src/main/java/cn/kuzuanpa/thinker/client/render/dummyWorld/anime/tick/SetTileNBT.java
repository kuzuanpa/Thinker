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

import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.tick.IDummyWorldTileTickingAnime;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class SetTileNBT implements IDummyWorldTileTickingAnime {
    public NBTTagCompound tag;
    long startTime, endTime;
    boolean onlySetOnce,alreadySet=false;
    public SetTileNBT(long startTime,long endTime,NBTTagCompound tag,boolean onlySetOnce){
        this.startTime = startTime;
        this.endTime=endTime;
        this.tag=tag;
        this.onlySetOnce=onlySetOnce;
    }
    @Override
    public String jsonName() {
        return "Tick.SetTileNBT";
    }

    @Override
    public boolean beforeTick(long time,TileEntity tileEntity) {
        if(startTime<time&&time<endTime&&(!onlySetOnce||!alreadySet)){
            tileEntity.readFromNBT( tag);
            alreadySet=true;
        }
        return false;
    }
}
