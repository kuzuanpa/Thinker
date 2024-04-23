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
package cn.kuzuanpa.thinker.client.render.dummyWorld.anime;

import cn.kuzuanpa.thinker.Thinker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class SetTileNBT implements IDummyWorldTilePropertiesAnime {
    public NBTTagCompound tag;
    public SetTileNBT(NBTTagCompound tag){
        this.tag=tag;
    }
    public void doAnime(TileEntity tileEntity){
        tileEntity.readFromNBT( tag);
    }

    @Override
    public String jsonName() {
        return "Prop.SetTileNBT";
    }
}
