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

public class animeRotate implements IGuiAnime {
    public animeRotate(int startTime,int endTime,int rotateAngle){
        this.startTime=startTime;
        this.rotateAngle=rotateAngle;
        this.endTime=endTime;
    }
    public int startTime, endTime, rotateAngle;
    @Override
    public void animeDraw(long initTime) {
        long timer = System.currentTimeMillis()-initTime;

        if(timer<startTime) return;
        if(timer<endTime)GL11.glRotatef(((float)(timer - startTime)/(float)(endTime-startTime))*rotateAngle,0,0,1);
        else GL11.glRotatef(rotateAngle,0,0,1);
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
        return "Gui.Rotate";
    }
}
