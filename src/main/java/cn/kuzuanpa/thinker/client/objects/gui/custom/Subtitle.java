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

import java.util.*;

import static cn.kuzuanpa.thinker.Thinker.getInt;

public class Subtitle extends ThinkerButtonBase {
    /**Format: String, Map:(color joinTime leaveTime fadeInLength fadeOutLength)**/
    public List<SubtitleLine> lines = new ArrayList<>();

    public Subtitle(int id, Map<String, Object> texts, int posX, int posY){
        super(id,posX,posY,texts.keySet().stream().mapToInt(String::length).max().orElse(64)*5,12,"");
        for (Map.Entry<String, Object> entry : texts.entrySet()) {
            String text = entry.getKey();
            Object value = entry.getValue();
            int color = 0xffffff;
            int joinTime = 0;
            int leaveTime = Integer.MAX_VALUE;
            int fadeInLength = 0;
            int fadeOutLength = 0;
            if (value instanceof Map<?,?>) {
                Map<String, Object> props = (Map<String, Object>) value;
                if (props.containsKey("color")) color = getInt(props.get("color"));
                if (props.containsKey("joinTime")) joinTime = getInt(props.get("joinTime"));
                if (props.containsKey("leaveTime")) leaveTime = getInt(props.get("leaveTime"));
                if (props.containsKey("fadeInLength")) fadeInLength = getInt(props.get("fadeInLength"));
                if (props.containsKey("fadeOutLength")) fadeOutLength = getInt(props.get("fadeOutLength"));
            }
            lines.add(new SubtitleLine(text, color, joinTime, leaveTime, fadeInLength, fadeOutLength));
        }
        // Sort lines by joinTime so they stack in order
        lines.sort(Comparator.comparingInt(l -> l.joinTime));
    }

    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("texts")&&
                values.containsKey("posX")&&
                values.containsKey("posY");
        if(!result) thinkerJsonReader.requestLogError("Not Enough contents for Subtitle: texts, posX, posY");
        return result;
    }

    @SuppressWarnings("unchecked")
    public static Subtitle create(Map<String, Object> values) {
        int posX = getInt(values.get("posX"));
        int posY = getInt(values.get("posY"));
        Map<String, Object> texts = (Map<String, Object>) values.get("texts");
        return new Subtitle(10, texts, posX, posY);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY){
        if (this.visible) {
            GL11.glPushMatrix();
            GuiAnimeList.forEach(anime -> anime.animeDrawPre(timer));
            GL11.glTranslatef(xPosition + (height / 2F), yPosition + (width / 2F),0);
            GuiAnimeList.forEach(anime -> anime.animeDraw(timer));
            GL11.glTranslatef(-(xPosition + (height / 2F)), -(yPosition + (width / 2F)),0);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            int lineIndex = 0;
            for (SubtitleLine line : lines) {
                if (timer < line.joinTime || timer > line.leaveTime) continue;

                float alpha = 1.0f;
                // Fade in
                if (timer < line.joinTime + line.fadeInLength) {
                    alpha = (float)(timer - line.joinTime) / line.fadeInLength;
                }
                // Fade out
                else if (timer > line.leaveTime - line.fadeOutLength) {
                    alpha = (float)(line.leaveTime - timer) / line.fadeOutLength;
                }
                alpha = Math.max(0f, Math.min(1f, alpha));

                float r = ((line.color >> 16) & 0xFF) / 255f;
                float g = ((line.color >> 8) & 0xFF) / 255f;
                float b = (line.color & 0xFF) / 255f;

                GL11.glColor4f(r, g, b, alpha);
                mixinHandler.cancelForgeFontColorOverride = true;
                int yOffset = lineIndex * 12;
                Minecraft.getMinecraft().fontRenderer.drawString(line.text, xPosition, yPosition + yOffset, 0xffffffff);
                mixinHandler.cancelForgeFontColorOverride = false;
                lineIndex++;
            }

            GL11.glColor4f(1f, 1f, 1f, 1f);
            GL11.glDisable(GL11.GL_BLEND);

            GuiAnimeList.forEach(anime -> anime.animeDrawAfter(timer));
            GL11.glPopMatrix();
        }
    }

    public static class SubtitleLine {
        public String text;
        public int color;
        public int joinTime;
        public int leaveTime;
        public int fadeInLength;
        public int fadeOutLength;

        public SubtitleLine(String text, int color, int joinTime, int leaveTime, int fadeInLength, int fadeOutLength){
            this.text = text;
            this.color = color;
            this.joinTime = joinTime;
            this.leaveTime = leaveTime;
            this.fadeInLength = fadeInLength;
            this.fadeOutLength = fadeOutLength;
        }
    }
}