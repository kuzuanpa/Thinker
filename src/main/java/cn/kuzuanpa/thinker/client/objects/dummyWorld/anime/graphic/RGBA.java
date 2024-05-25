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

import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import org.lwjgl.opengl.GL11;

import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getInt;

public class RGBA implements IDummyWorldGraphicAnime {
    public RGBA(int startTime, int endTime,int startR,int startG,int startB,int startA,int dR,int dG,int dB,int dA){
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

    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("startTime")&&
                values.containsKey("endTime")&&
                values.containsKey("originR")&&
                values.containsKey("originG")&&
                values.containsKey("originB")&&
                values.containsKey("originA")&&
                values.containsKey("dR")&&
                values.containsKey("dG")&&
                values.containsKey("dB")&&
                values.containsKey("dA");
        if(!result)
            thinkerJsonReader.requestLogError("Not Enough contents for world.graphic.MoveLinear: startTime, endTime, originR, originG, originB, originA, dR, dG, dB, dA");
        return result;
    }

    public static RGBA create(Map<String, Object> values) {
        return new RGBA(getInt(values.get("startTime")),getInt(values.get("endTime")),getInt(values.get("originR")),getInt(values.get("originG")),getInt(values.get("originB")),getInt(values.get("originA")),getInt(values.get("dR")),getInt(values.get("dG")),getInt(values.get("dB")),getInt(values.get("dA")));
    }

}
