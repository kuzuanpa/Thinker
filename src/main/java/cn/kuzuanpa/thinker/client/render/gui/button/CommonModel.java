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

import cn.kuzuanpa.thinker.client.render.gui.ThinkingGui;
import cn.kuzuanpa.thinker.clientProxy;
import lib.lookingGlass.com.xcompwiz.lookingglass.api.view.IWorldView;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class CommonModel extends CommonGuiButton{

    private IWorldView v;
    public CommonModel(int id, int xPos, int yPos, int width, int height){
        super(id, xPos, yPos,width,height,"");
        try {
        v = clientProxy.getOrCreateDummyWorldView(null,1024,1024);
        }catch (Throwable t){
            t.printStackTrace();
        }
    }
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
        if (this.visible)
        {
            try {
                if (v.isReady()) {
                    int texture = v.getTexture();
                    if (texture != 0) {
                        v.markDirty();
                        GL11.glDisable(GL_LIGHTING);
                        GL11.glDisable(GL_ALPHA_TEST);
                        GL11.glPushMatrix();
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
                        GL11.glTranslatef(404,400,900);
                        GL11.glRotatef(180,0,0,1);
                        GL11.glColor4f(1.0F,1.0F,1.0F,1.0F);
                        this.drawTexturedModalRect(xPosition, yPosition+64, 0, 0, 256, 256);
                        GL11.glPopMatrix();
                        GL11.glEnable(GL_ALPHA_TEST);
                        GL11.glEnable(GL_LIGHTING);
                    }
                }
            }catch (Throwable t){
                t.printStackTrace();
            }
        }
    }

}
