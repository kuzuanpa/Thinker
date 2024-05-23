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
package cn.kuzuanpa.thinker.client.objects.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

public class thinkerImage extends ThinkerButtonBase {
    public thinkerImage(int id, int x, int y, int u, int v, int width, int height, String path, String displayText) {
        super(id, x, y,width,height,displayText);
        this.id=id;
        this.xPosition=x;
        this.yPosition=y;
        this.u=u;
        this.v=v;
        this.width=width;
        this.height=height;
        this.path=path;
        rotateAngle=0;
    }
    public int id,rotateAngle, u, v, width, height;
    public String path;
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        updateHoverState(mouseX,mouseY);
        if (this.visible) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GuiAnimeList.forEach(anime->anime.animeDrawPre(timer));
            GL11.glColor4f(1.0F,1.0F,1.0F,1.0F);
            mc.getTextureManager().bindTexture(new ResourceLocation(MOD_ID,path));
            GL11.glTranslatef(xPosition + (height / 2F), yPosition + (width / 2F),0);
            GuiAnimeList.forEach(anime->anime.animeDraw(timer));
            GL11.glTranslatef(-(xPosition + (height / 2F)), -(yPosition + (width / 2F)),0);
            this.drawTexturedModalRect(xPosition, yPosition, u, v, this.width, this.height);
            GuiAnimeList.forEach(anime->anime.animeDrawAfter(timer));
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glPopMatrix();
        }
    }
}
