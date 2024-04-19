package cn.kuzuanpa.thinker.client.render.dummyWorld;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;


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


    public static class thinkerModel extends AnimatedGeoModel<dummyWorldGeckoModelContainer> {
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
