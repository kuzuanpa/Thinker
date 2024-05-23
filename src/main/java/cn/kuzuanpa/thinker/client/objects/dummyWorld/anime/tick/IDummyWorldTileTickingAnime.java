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

import cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.IDummyWorldAnimes;
import net.minecraft.tileentity.TileEntity;

public interface IDummyWorldTileTickingAnime extends IDummyWorldAnimes {
    boolean beforeTick(long time,TileEntity tileEntity);
    default void afterTick(long time,TileEntity tileEntity){}
}
