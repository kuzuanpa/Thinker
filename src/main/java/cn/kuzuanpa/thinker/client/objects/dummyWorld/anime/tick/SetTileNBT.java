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
import cn.kuzuanpa.thinker.client.objects.dummyWorld.dummyWorldTile;
import cn.kuzuanpa.thinker.util.Nbt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getBoolean;
import static cn.kuzuanpa.thinker.Thinker.getInt;

public class SetTileNBT implements IDummyWorldTickingAnime {
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
    public boolean beforeTick(long time, IdummyWorldThinkerObject obj, World world) {
        if(!(obj instanceof dummyWorldTile))return false;
        TileEntity tileEntity = ((dummyWorldTile) obj).tile;
        if(startTime<time&&time<endTime&&(!onlySetOnce||!alreadySet)){
            tileEntity.readFromNBT( tag);
            alreadySet=true;
        }
        return false;
    }

    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("startTime")&&
                values.containsKey("endTime")&&
                values.containsKey("tag");
        if(!result) thinkerJsonReader.requestLogError("Not Enough contents for world.tick.setTileNBT: startTime, endTime, tag");
        NBTTagCompound nbt =null;
        if(result)try {nbt = (NBTTagCompound) Nbt.stringToNBT((String) values.get("tag"));}catch (Exception e){thinkerJsonReader.requestLogError("Invaild NBT String");return false;}
        return result&&nbt!=null;
    }

    public static SetTileNBT create(Map<String, Object> values) {
        return new SetTileNBT(getInt(values.get("startTime")),getInt(values.get("endTime")), (NBTTagCompound) Nbt.stringToNBT((String) values.get("tag")),getBoolean(values.getOrDefault("onlySetOnce","false")));
    }
}
