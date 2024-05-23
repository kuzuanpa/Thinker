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

public class MoveLinear implements IDummyWorldGraphicAnime {
    public MoveLinear(int startTime, int endTime, float dX, float dY, float dZ){
        this.startTime=startTime;
        this.endTime=endTime;
        this.dX =dX;
        this.dY =dY;
        this.dZ= dZ;
    }
    public int startTime, endTime;
    public float dX, dY, dZ;
    @Override
    public void animeDraw(long timer) {
        if(timer<startTime) return;
        float progress=(float)(timer - startTime)/(float)(endTime-startTime);
        if (timer < endTime) GL11.glTranslatef(progress * dX,progress * dY, progress*dZ);
        else GL11.glTranslatef(dX, dY, dZ);
    }

}
