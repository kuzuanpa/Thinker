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
package cn.kuzuanpa.thinker.client.render.gui.anime;

import cn.kuzuanpa.thinker.client.render.gui.ThinkerButtonBase;

public interface IGuiAnime {
    void animeDraw(long time);
    void animeDrawPre(long time);
    void animeDrawAfter(long time);
    /**Some Anime changed Position or Scale of buttons. update them in there to make things perform correctly when clicked on these button**/
    void updateButton(long time, ThinkerButtonBase button);
    String jsonName();
}

