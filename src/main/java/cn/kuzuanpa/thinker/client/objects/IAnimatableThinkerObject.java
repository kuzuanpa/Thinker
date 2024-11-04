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
package cn.kuzuanpa.thinker.client.objects;

import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.client.anim.dummyWorld.IDummyWorldAnimes;
import cn.kuzuanpa.thinker.client.anim.gui.IGuiAnime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface IAnimatableThinkerObject extends IThinkerObject {
    ArrayList<IGuiAnime> getGuiAnimeList();

    ArrayList<IDummyWorldAnimes> getWorldAnimeList();
    default IAnimatableThinkerObject addAnimes(List<IGuiAnime> guiAnimes, List<IDummyWorldAnimes> dummyWorldAnimes){
        if(this.getGuiAnimeList()==null&&!guiAnimes.isEmpty()) Thinker.err("Object "+this.toString()+ "don't support GUIAnime!");
        else this.getGuiAnimeList().addAll(guiAnimes);
        if(this.getWorldAnimeList()==null&&!dummyWorldAnimes.isEmpty())Thinker.err("Object "+this.toString()+ "don't support World Anime!");
        else this.getWorldAnimeList().addAll(dummyWorldAnimes);
        return this;
    }
    default IAnimatableThinkerObject addAnimes(List<IThinkerAnime> animes){
        if(this.getGuiAnimeList()!=null) this.getGuiAnimeList().addAll(animes.stream().filter(anime->anime instanceof IGuiAnime).map(anime->(IGuiAnime)anime).collect(Collectors.toList()));
        if(this.getWorldAnimeList()!=null)this.getWorldAnimeList().addAll(animes.stream().filter(anime->anime instanceof IDummyWorldAnimes).map(anime->(IDummyWorldAnimes)anime).collect(Collectors.toList()));
        return this;
    }
}
