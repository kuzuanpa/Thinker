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
package cn.kuzuanpa.thinker.client.objects.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import blockrenderer6343.world.DummyWorld;
import cn.kuzuanpa.thinker.client.objects.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.client.objects.IThinkerObject;

import java.util.List;

public interface IdummyWorldThinkerObject extends IThinkerObject, IAnimatableThinkerObject {
    void render(DummyWorld world, long timer, BlockPosition mousePointingPos);

    /**
     * add object to world
     * @return objects needed to add to render list, return new ArrayList<>() if null
     */
    List<IdummyWorldThinkerObject> addToWorld(DummyWorld world);
    BlockPosition getPos();
}
