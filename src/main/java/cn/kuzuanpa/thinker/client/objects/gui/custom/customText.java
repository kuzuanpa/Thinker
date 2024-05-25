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
package cn.kuzuanpa.thinker.client.objects.gui.custom;

import cn.kuzuanpa.thinker.client.handler.mixinHandler;
import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import cn.kuzuanpa.thinker.client.objects.gui.ThinkerButtonBase;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getInt;

public class customText extends ThinkerButtonBase {
    int color;
    String text;
    public customText(int id, String text, int posX, int posY){
        super(id,posX,posY,text.length(),12,"");
        this.text=text;
        this.color=-1;
    }
    public customText(int id, String text, int posX, int posY, int color){
        super(id,posX,posY,text.length()*5,12,"");
        this.text=text;
        this.color=color;
    }
    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("text")&&
                values.containsKey("posX")&&
                values.containsKey("posY");
        if(!result) thinkerJsonReader.requestLogError("Not Enough contents for customText: text, posX, posY");
        return result;
    }

    public static customText create(Map<String, Object> values) {
        int color = 0xffffff;
        if(values.containsKey("color")) color = getInt(values.get("color"));
        return new customText(10, (String) values.get("text"), getInt(values.get("posX")), getInt(values.get("posY")),color);
    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY){
        if (this.visible) {
            GL11.glPushMatrix();
            GuiAnimeList.forEach(anime -> anime.animeDrawPre(timer));
            GL11.glTranslatef(xPosition + (height / 2F), yPosition + (width / 2F),0);
            GuiAnimeList.forEach(anime -> anime.animeDraw(timer));
            GL11.glTranslatef(-(xPosition + (height / 2F)), -(yPosition + (width / 2F)),0);
            mixinHandler.cancelForgeFontColorOverride=true;
            Minecraft.getMinecraft().fontRenderer.drawString(text, xPosition, yPosition, 0xffffffff);
            mixinHandler.cancelForgeFontColorOverride=false;
            GuiAnimeList.forEach(anime -> anime.animeDrawAfter(timer));
            GL11.glPopMatrix();
        }
    }

}
