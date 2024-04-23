package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import blockrenderer6343.world.DummyWorld;
import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.api.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnimes;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import net.geckominecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.ArrayList;
import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getInt;

/**This is actually a renderer, but "new Render(new Model(..))" seems stupid...**/
public class dummyWorldGeckoModel implements IGeoRenderer<dummyWorldGeckoModelContainer>,IdummyWorldThinkerObject, IAnimatableThinkerObject {

    public final ArrayList<IGuiAnime> GuiAnimeList = new ArrayList<>();
    public final ArrayList<IDummyWorldAnimes> WorldAnimeList = new ArrayList<>();

    public final BlockPosition pos ;
    public static boolean doesMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("posX")&&
                values.containsKey("posY")&&
                values.containsKey("posZ")&&
                values.containsKey("modelPath")&&
                values.containsKey("texturePath")&&
                values.containsKey("animePath");
        if(!result) Thinker.err("Not Enough contents for dummyWorldGeckoModel: modelPath, texturePath, animePath, posX, posY, posZ");
        return result;
    }

    public static dummyWorldGeckoModel create(Map<String, Object> values) {
        return new dummyWorldGeckoModel(new BlockPosition(getInt(values.get("posX")),getInt(values.get("posY")),getInt(values.get("posZ"))),(String) values.get("modelPath"),(String) values.get("texturePath"),(String) values.get("animePath"));
    }
        private final dummyWorldGeckoModelContainer dummyGeckoModel;
        public dummyWorldGeckoModel(BlockPosition pos,String modelLocation, String textureLocation, String animeLocation) {
            this.pos = pos;
            dummyGeckoModel =new dummyWorldGeckoModelContainer(modelLocation, textureLocation, animeLocation);
        }

        public void render(DummyWorld world, long initTime, boolean isMousePointed) {
            GeoModel model = getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(dummyGeckoModel));
            getGeoModelProvider().setLivingAnimations(dummyGeckoModel, this.getUniqueID(dummyGeckoModel));
            int light = 15;
            if (world != null) {
                light = world.getLightBrightnessForSkyBlocks(pos.x,pos.y,pos.z,0);
            }

            int lx = light % 65536;
            int ly = light / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)lx, (float)ly);
            float partialTicks=(System.currentTimeMillis()-initTime)/100000F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(pos.x,pos.y,pos.z);
            GlStateManager.translate(0.0F, 0.01F, 0.0F);
            GlStateManager.translate(0.5, 0.0, 0.5);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, dummyGeckoModel.thinkerModel.getTextureID());
            Color renderColor = this.getRenderColor(dummyGeckoModel, partialTicks);
            this.render(model, dummyGeckoModel, partialTicks, (float)renderColor.getRed() / 255.0F, (float)renderColor.getGreen() / 255.0F, (float)renderColor.getBlue() / 255.0F, (float)renderColor.getAlpha() / 255.0F);
            GlStateManager.popMatrix();

        }

    @Override
    public BlockPosition getPos() {
        return pos;
    }

    public AnimatedGeoModel<dummyWorldGeckoModelContainer> getGeoModelProvider() {
            return this.dummyGeckoModel.thinkerModel;
        }


        public ResourceLocation getTextureLocation(dummyWorldGeckoModelContainer instance) {
            return this.dummyGeckoModel.thinkerModel.getTextureLocation(instance);
        }

    static {
        AnimationController.addModelFetcher((object) -> {
            if (! (object instanceof dummyWorldGeckoModelContainer)) return null;
            dummyWorldGeckoModelContainer model = (dummyWorldGeckoModelContainer)object;
            return (IAnimatableModel<Object>) model.thinkerModel.get();
        });
    }

    public ArrayList<IGuiAnime> getGuiAnimeList() {return GuiAnimeList;}

    public ArrayList<IDummyWorldAnimes> getWorldAnimeList() {return WorldAnimeList;};
    }
