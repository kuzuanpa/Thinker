package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.world.DummyWorld;
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

/**This is actually a renderer, but "new Render(new Model(..))" seems stupid...**/
public class dummyWorldGeckoModel implements IGeoRenderer<dummyWorldGeckoModelContainer>, IAnimatableThinkerObject {

    public static final ArrayList<IGuiAnime> GuiAnimeList = new ArrayList<>();
    public static final ArrayList<IDummyWorldAnimes> WorldAnimeList = new ArrayList<>();

    public static boolean doesMapHaveValidContents(Map<String,Object> values) {
        return values.containsKey("modelPath")&&
                values.containsKey("texturePath")&&
                values.containsKey("animePath");
    }

    public static dummyWorldGeckoModel create(Map<String, Object> values) {
        return new dummyWorldGeckoModel((String) values.get("modelPath"),(String) values.get("texturePath"),(String) values.get("animePath"));
    }
        private final dummyWorldGeckoModelContainer dummyGeckoModel;
        public dummyWorldGeckoModel(String modelLocation, String textureLocation, String animeLocation) {
            dummyGeckoModel =new dummyWorldGeckoModelContainer(modelLocation, textureLocation, animeLocation);
        }

        public void render(DummyWorld world, double x, double y, double z,long initTime) {
            GeoModel model = getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(dummyGeckoModel));
            getGeoModelProvider().setLivingAnimations(dummyGeckoModel, this.getUniqueID(dummyGeckoModel));
            int light = 15;
            if (world != null) {
                light = world.getLightBrightnessForSkyBlocks((int)Math.floor(x),(int)Math.floor(y),(int)Math.floor(z),0);
            }

            int lx = light % 65536;
            int ly = light / 65536;
            if (x != 0 && y != 0 && z != 0) {
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)lx, (float)ly);
            }
            float partialTicks=(System.currentTimeMillis()-initTime)/100000F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.translate(0.0F, 0.01F, 0.0F);
            GlStateManager.translate(0.5, 0.0, 0.5);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, dummyGeckoModel.thinkerModel.getTextureID());
            Color renderColor = this.getRenderColor(dummyGeckoModel, partialTicks);
            this.render(model, dummyGeckoModel, partialTicks, (float)renderColor.getRed() / 255.0F, (float)renderColor.getGreen() / 255.0F, (float)renderColor.getBlue() / 255.0F, (float)renderColor.getAlpha() / 255.0F);
            GlStateManager.popMatrix();

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
