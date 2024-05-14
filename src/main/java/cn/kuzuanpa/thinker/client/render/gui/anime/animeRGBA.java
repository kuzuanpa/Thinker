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

import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.client.render.gui.ThinkerButtonBase;
import org.lwjgl.opengl.GL11;

public class animeRGBA implements IGuiAnime {
    public animeRGBA(int startTime, int endTime,int startR,int startG,int startB,int startA,int dR,int dG,int dB,int dA){
        this.startTime=startTime;
        this.endTime=endTime;
        this.startR=startR;
        this.startG=startG;
        this.startB=startB;
        this.startA=startA;
        this.dR=dR;
        this.dG=dG;
        this.dB=dB;
        this.dA=dA;
    }
    public int startTime, endTime,startR, startG, startB, startA,dR,dG,dB,dA;
    @Override
    public void animeDraw(long timer) {

        if((startR+dR)>255||(startG+dG)>255||(startB+dB)>255||(startA+dA)>255) Thinker.err(new IllegalArgumentException("RGBA value is too big: dR:"+dR+",dG:"+dG+",dB:"+dB+",dA:"+dA));
        if(timer<startTime) return;
        if(timer<endTime){
            float f1=((float)(timer - startTime)/(float)(endTime-startTime));
            GL11.glColor4ub((byte) (startR+(f1*dR)), (byte) (startG+(f1*dG)), (byte) (startB+(f1*dB)), (byte) (startA+(f1*dA)));
        }if(timer>endTime){
            GL11.glColor4ub((byte) (startR+dR), (byte) (startG+dG), (byte) (startB+dB), (byte) (startA+dA));
        }
    }

    @Override
    public void animeDrawPre(long time) {
    }

    @Override
    public void animeDrawAfter(long time) {
    }
    @Override
    public void updateButton(long time, ThinkerButtonBase button) {
        long timer = System.currentTimeMillis()- time;
        if(timer<startTime) return;
        if(timer<endTime)button.visible= (startA + ((float) (timer - startTime) / (float) (endTime - startTime) * dA) >= 1);
         else button.visible= startA + dA > 1;

    }

    @Override
    public String jsonName() {
        return "Gui.RGBA";
    }
}
