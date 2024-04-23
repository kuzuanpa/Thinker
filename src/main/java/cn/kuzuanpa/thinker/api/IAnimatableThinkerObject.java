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
package cn.kuzuanpa.thinker.api;

import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnimes;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;

import java.util.ArrayList;
import java.util.List;

public interface IAnimatableThinkerObject extends IThinkerObject{
    ArrayList<IGuiAnime> getGuiAnimeList();

    ArrayList<IDummyWorldAnimes> getWorldAnimeList();
    default IAnimatableThinkerObject addAnimes(List<IGuiAnime> guiAnimes, List<IDummyWorldAnimes> dummyWorldAnimes){
        if(this.getGuiAnimeList()==null&&!guiAnimes.isEmpty()) Thinker.err("Object "+this.toString()+ "don't support GUIAnime!");
            else this.getGuiAnimeList().addAll(guiAnimes);
        if(this.getWorldAnimeList()==null&&!dummyWorldAnimes.isEmpty())Thinker.err("Object "+this.toString()+ "don't support World Anime!");
            else this.getWorldAnimeList().addAll(dummyWorldAnimes);
        return this;
    }
}
