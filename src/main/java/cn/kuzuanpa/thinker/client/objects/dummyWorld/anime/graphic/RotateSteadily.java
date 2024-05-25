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

package cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.graphic;

import org.lwjgl.opengl.GL11;

public class RotateSteadily implements IDummyWorldGraphicAnime {
    @Override
    public boolean animeDraw(long time) {
        GL11.glTranslatef(0.5F,0.5F,0);
        GL11.glRotated(time/10F,0,0,1);
        GL11.glTranslatef(-0.5F,-0.5F,0);
        return false;
    }
}
