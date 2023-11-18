package cn.kuzuanpa.thinker.client.render.gui.button.custom;

import cn.kuzuanpa.thinker.client.render.gui.button.CommonGuiButton;
import cpw.mods.fml.client.config.GuiUtils;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class customImage extends CommonGuiButton {
    public customImage(int id, String texturePath, int posX, int posY, int width, int height){
        super(id,posX,posY,width,height,"");
        this.texturePath=texturePath;
        this.posX=posX;
        this.posY=posY;
        this.width=width;
        this.height=height;
        loadTexture();
    }

    String texturePath;
    int posX,posY,width,height,glTextureId=-1;
    public void loadTexture(){
        try (InputStream inputstream = Files.newInputStream(Paths.get(texturePath)))
        {
            if (this.glTextureId != -1)
            {
                TextureUtil.deleteTexture(this.glTextureId);
                this.glTextureId = -1;
            }
            BufferedImage bufferedimage = ImageIO.read(inputstream);
            glTextureId=TextureUtil.uploadTextureImage(glTextureId, bufferedimage);
        }catch (IOException ioexception)
        {
            FMLLog.log(Level.WARN,"Failed to load texture: " + texturePath);
            ioexception.printStackTrace();
        }
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY){
        if (this.visible) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTextureId);
            GuiUtils.drawContinuousTexturedBox(posX, posY, 0, 0, width, height, width, height, 0, zLevel);
        }
    }
}
