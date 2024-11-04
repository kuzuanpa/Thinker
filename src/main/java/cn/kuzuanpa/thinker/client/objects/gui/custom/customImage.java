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

import cn.kuzuanpa.thinker.client.objects.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import cn.kuzuanpa.thinker.client.anim.gui.IGuiAnime;
import cn.kuzuanpa.thinker.client.objects.gui.ThinkerButtonBase;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getInt;

public class customImage extends ThinkerButtonBase implements IAnimatableThinkerObject {

    public customImage(int id, String texturePath, int posX, int posY, int width, int height){
        super(id,posX,posY,width,height,"");
        this.texturePath=texturePath;
        this.posX=posX;
        this.posY=posY;
        this.width=width;
        this.height=height;
        loadTexture();
    }
    public customImage(int id, String texturePath, int posX, int posY, int width, int height, IGuiAnime... animes){
        super(id,posX,posY,width,height,"");
        this.texturePath=texturePath;
        this.posX=posX;
        this.posY=posY;
        this.width=width;
        this.height=height;
        loadTexture();
        for (IGuiAnime anime : animes) this.addAnime(anime);
    }
    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("path")&&
                values.containsKey("posX")&&
                values.containsKey("posY")&&
                values.containsKey("width")&&
                values.containsKey("height");
        if(!result)
            thinkerJsonReader.requestLogError("Not Enough contents for customImage: path, posX, posY, width, height");
        return result;
    }

    public static customImage create(Map<String, Object> values) {
        return new customImage(10,(String) values.get("path"),getInt(values.get("posX")),getInt(values.get("posY")),getInt(values.get("width")),getInt(values.get("height")));
    }
    public void destroy(){
        TextureUtil.deleteTexture(this.glTextureId);
    }
    String texturePath;
    int posX,posY,width,height,glTextureId=-1;
    public void loadTexture(){
        try (InputStream inputstream = Files.newInputStream(Paths.get(texturePath)))
        {
            if (this.glTextureId != -1) {
                TextureUtil.deleteTexture(this.glTextureId);
                this.glTextureId = -1;
            }
            glTextureId=TextureUtil.uploadTextureImage(GL11.glGenTextures(), ImageIO.read(inputstream));
        }catch (IOException ioexception)
        {
            FMLLog.log(Level.WARN,"Failed to load texture: " + texturePath);
            ioexception.printStackTrace();
        }
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY){
        if (this.visible) {
            GL11.glPushMatrix();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTextureId);

            //why (256F/height)? idk, i just rendered some picture and guessed this argument.
            float var7 = 0.00390625F*(256F/height)*(height*1F/width);
            float var8 = 0.00390625F*(256F/height);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(posX, posY + height, zLevel, 0, height * var8);
            tessellator.addVertexWithUV(posX + width, posY + height, zLevel,  width * var7, height * var8);
            tessellator.addVertexWithUV(posX + width, posY, zLevel, width * var7, 0);
            tessellator.addVertexWithUV(posX, posY, zLevel, 0, 0);
            GL11.glTranslatef(xPosition, yPosition ,0);
            GuiAnimeList.forEach(anime -> anime.animeDraw(timer));
            GL11.glTranslatef(-(xPosition ), -(yPosition ),0);
            tessellator.draw();
            GL11.glPopMatrix();
        }
    }

}
