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
package cn.kuzuanpa.thinker.client.handler;

import cn.kuzuanpa.thinker.client.render.dummyWorld.IdummyWorldThinkerObject;

import java.util.ArrayList;
import java.util.List;

public class dummyWorldHandler {
    public static List<IdummyWorldThinkerObject> dummyWorldObjects =new ArrayList<>();
    public static void onProfileChanged(String profileID){
        dummyWorldObjects.clear();
        if(profileHandler.getProfile(profileID).dummyWorldThinkerObjects.isEmpty())return;
        dummyWorldObjects.addAll(profileHandler.getProfile(profileID).dummyWorldThinkerObjects);
    }

}
