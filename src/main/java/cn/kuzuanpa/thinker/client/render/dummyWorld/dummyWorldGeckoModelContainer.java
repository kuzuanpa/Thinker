package cn.kuzuanpa.thinker.client.render.dummyWorld;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;


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
        protected final String modelLocation,textureLocation,animeLocation;

        public thinkerModel(String modelLocation,String textureLocation,String animeLocation) {
            this.modelLocation=modelLocation;
            this.textureLocation=textureLocation;
            this.animeLocation=animeLocation;
        }

        public ResourceLocation getAnimationFileLocation(dummyWorldGeckoModelContainer entity) {
            return new ResourceLocation("geckolib3", "animations/botarium.animation.json");
        }

        public ResourceLocation getModelLocation(dummyWorldGeckoModelContainer animatable) {
            return new ResourceLocation("geckolib3", "geo/botarium.geo.json");
        }

        public ResourceLocation getTextureLocation(dummyWorldGeckoModelContainer entity) {
            return new ResourceLocation("geckolib3", "textures/block/botarium.png");
        }
        public IAnimatableModel<?> get(){return this;}
    }
}
