package cn.kuzuanpa.thinker.client.render.gui.button.custom;

import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.api.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnimes;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import cn.kuzuanpa.thinker.client.render.gui.button.ThinkerButton;
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
import java.util.ArrayList;
import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getInt;

public class customImage extends ThinkerButton implements IAnimatableThinkerObject {
    public static final ArrayList<IGuiAnime> GuiAnimeList = new ArrayList<>();
    public static final ArrayList<IDummyWorldAnimes> WorldAnimeList = new ArrayList<>();

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
    public static boolean doesMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("path")&&
                values.containsKey("posX")&&
                values.containsKey("posY")&&
                values.containsKey("width")&&
                values.containsKey("height");
        if(!result) Thinker.err("Not Enough contents for customImage: path, posX, posY, width, height");
        return result;
    }

    public static customImage create(Map<String, Object> values) {
        return new customImage(10,(String) values.get("path"),getInt(values.get("posX")),getInt(values.get("posY")),getInt(values.get("width")),getInt(values.get("height")));
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
            GL11.glTranslatef(xPosition + (height / 2F), yPosition + (width / 2F),0);
            GuiAnimeList.forEach(anime -> anime.animeDraw(initTime));
            GL11.glTranslatef(-(xPosition + (height / 2F)), -(yPosition + (width / 2F)),0);
            GuiUtils.drawContinuousTexturedBox(posX, posY, 0, 0, width, height, width, height, 0, zLevel);
        }
    }

}
