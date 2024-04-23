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

public class animeScale implements IGuiAnime {
    public animeScale(int startTime,int endTime,float scaleRate,float scaleX,float scaleY){
        this.startTime=startTime;
        this.endTime=endTime;
        this.scaleRate=scaleRate;
        this.scaleX=scaleX;
        this.scaleY=scaleY;
    }
    public int startTime, endTime;
    public float scaleRate,scaleX,scaleY;
    @Override
    public void animeDraw(long initTime) {
        long timer = System.currentTimeMillis()-initTime;

        if(timer<startTime) return;
        if(timer<endTime) GL11.glScalef(((float)(timer - startTime)/(float)(endTime-startTime))*scaleX*scaleRate,((float)(timer - startTime)/(float)(endTime-startTime))*scaleY*scaleRate,1);
        else GL11.glScalef(scaleX*scaleRate,scaleY*scaleRate,1);
    }

    @Override
    public void animeDrawPre(long initTime) {}

    @Override
    public void animeDrawAfter(long initTime) {}
    @Override
    public void updateButton(long initTime, ThinkerButton button) {
        //TODO
    }
    @Override
    public String jsonName() {
        return "Gui.Scale";
    }
}
