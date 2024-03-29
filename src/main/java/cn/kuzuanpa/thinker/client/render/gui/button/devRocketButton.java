/*
 * This class was created by <kuzuanpa>. It is distributed as
 * part of the kTFRUAddon Mod. Get the Source Code in github:
 * https://github.com/kuzuanpa/kTFRUAddon
 *
 * kTFRUAddon is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 */

package cn.kuzuanpa.thinker.client.render.gui.button;

import cn.kuzuanpa.thinker.client.configHandler;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

public class devRocketButton extends ThinkerButton {


    public devRocketButton(int id, int x, int y, int w, int h) {
    super(id, x, y,w,h,"");
    this.id=id;
    this.x=x;
    this.y=y;
    reset();
}
    public int id,x,y;
    public static float rocketX=228,rocketY=400;
    public String path;
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        updateHoverState(mouseX,mouseY);
        if (this.visible) {
            GL11.glEnable(GL11.GL_BLEND);
            mc.getTextureManager().bindTexture(baseTexture);
            GL11.glColor4f(1.0F,1.0F,1.0F,1.0F);
            drawTexturedModalRect(328,400,0,0,32,32);

            //drawRocket
            GL11.glPushMatrix();
            GL11.glTranslatef(rocketX+8,rocketY+16,0);
            GL11.glRotatef(configHandler.devRocketRocketAngle.get()+180,0,0,1);
            GL11.glTranslatef(-8,-16,0);

            drawTexturedModalRect(0,0,69,20,16,32);
            GL11.glPopMatrix();

            updateRocket();
        }
    }
    public static void reset(){
        rocketX=228;rocketY=400;
    }
    public void updateRocket(){
        rocketX+= configHandler.devRocketRocketSpeed.get() *Math.cos(3.14*(configHandler.devRocketRocketAngle.get()-90)/180);
        rocketY+=configHandler.devRocketRocketSpeed.get() *Math.sin(3.14*(configHandler.devRocketRocketAngle.get()-90)/180);
    }
}
