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

import cn.kuzuanpa.thinker.client.render.gui.button.ThinkerButton;
import org.lwjgl.opengl.GL11;

public class animeRotateSteadily implements IGuiAnime {
    public animeRotateSteadily(float speed){
        this.speed=speed;
    }
    public float speed=1.0F;
    @Override
    public void animeDraw(long initTime) {
        long timer = System.currentTimeMillis()-initTime;

        GL11.glRotated((timer*speed)%360,0,0,1);
    }

    @Override
    public void animeDrawPre(long initTime) {

    }

    @Override
    public void animeDrawAfter(long initTime) {

    }
    @Override
    public void updateButton(long initTime, ThinkerButton button) {

    }
    @Override
    public String jsonName() {
        return "Gui.RotateSteadily";
    }
}
