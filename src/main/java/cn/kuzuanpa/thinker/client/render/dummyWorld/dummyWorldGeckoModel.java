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
package cn.kuzuanpa.thinker.client.render.dummyWorld;

import blockrenderer6343.api.utils.BlockPosition;
import blockrenderer6343.world.DummyWorld;
import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.api.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.client.json.thinkerJsonReader;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnimes;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import cpw.mods.fml.common.FMLLog;
import net.geckominecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;
import static cn.kuzuanpa.thinker.Thinker.getInt;

/**This is actually a renderer, but "new Render(new Model(..))" seems stupid...**/
public class dummyWorldGeckoModel implements IGeoRenderer<dummyWorldGeckoModel.dummyWorldGeckoModelContainer>,IdummyWorldThinkerObject, IAnimatableThinkerObject {

    public final ArrayList<IGuiAnime> GuiAnimeList = new ArrayList<>();
    public final ArrayList<IDummyWorldAnimes> WorldAnimeList = new ArrayList<>();

    public final BlockPosition pos ;
    public static boolean isMapHaveValidContents(Map<String,Object> values) {
        boolean result = values.containsKey("posX")&&
                values.containsKey("posY")&&
                values.containsKey("posZ")&&
                values.containsKey("modelPath")&&
                values.containsKey("texturePath")&&
                values.containsKey("animePath");
        if(!result) thinkerJsonReader.requestLogError("Not Enough contents for dummyWorldGeckoModel: modelPath, texturePath, animePath, posX, posY, posZ");
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


    public class dummyWorldGeckoModelContainer implements IAnimatable {
        private final AnimationFactory manager = new AnimationFactory(this);

        public final thinkerModel thinkerModel;

        public dummyWorldGeckoModelContainer(String modelLocation, String textureLocation, String animeLocation) {
            this.thinkerModel= new thinkerModel(modelLocation, textureLocation, animeLocation);
        }
        private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
            event.getController().transitionLengthTicks = 0.0;

            event.getController().setAnimation((new AnimationBuilder()).addAnimation("Botarium.anim.deploy", false).addAnimation("Botarium.anim.idle", true));

            return PlayState.CONTINUE;
        }
        public void registerControllers(AnimationData data) {
            data.addAnimationController(new AnimationController<>(this, "controller", 0.0F, this::predicate));
        }

        public AnimationFactory getFactory() {
            return this.manager;
        }


        public class thinkerModel extends AnimatedGeoModel<dummyWorldGeckoModelContainer> {
            protected final String modelLocation, texturePath, animeLocation;
            protected int glTextureId = -1;

            public thinkerModel(String modelLocation, String textureLocation, String animeLocation) {
                this.modelLocation = modelLocation;
                this.texturePath = textureLocation;
                this.animeLocation = animeLocation;
                loadTexture();
            }

            public void loadTexture() {
                try (InputStream inputstream = Files.newInputStream(Paths.get(texturePath))) {
                    if (this.glTextureId != -1) {
                        TextureUtil.deleteTexture(this.glTextureId);
                        this.glTextureId = -1;
                    }
                    BufferedImage bufferedimage = ImageIO.read(inputstream);
                    glTextureId = TextureUtil.uploadTextureImage(glTextureId, bufferedimage);
                } catch (IOException ioexception) {
                    FMLLog.log(Level.WARN, "Failed to load texture: " + texturePath);
                    ioexception.printStackTrace();
                }
            }

            public ResourceLocation getAnimationFileLocation(dummyWorldGeckoModelContainer entity) {
                return new ResourceLocation("custom", animeLocation);
            }

            public ResourceLocation getModelLocation(dummyWorldGeckoModelContainer animatable) {
                return new ResourceLocation("custom", modelLocation);
            }

            public ResourceLocation getTextureLocation(dummyWorldGeckoModelContainer entity) {
                return new ResourceLocation(MOD_ID, texturePath);
            }

            public IAnimatableModel<?> get() {
                return this;
            }

            public int getTextureID() {
                return glTextureId;
            }

        }
    }
}
