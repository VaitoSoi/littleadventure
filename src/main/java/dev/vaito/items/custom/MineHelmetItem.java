package dev.vaito.items.custom;

import dev.vaito.LittleAdventure;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class MineHelmetItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MineHelmetItem(ArmorMaterial armorMaterial, EquipmentType type, Settings properties) {
        super(properties.armor(armorMaterial, type));
    }

    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private MineHelmetRenderer<?> renderer;

            public <S extends BipedEntityRenderState> MineHelmetRenderer<?> getGeoArmorRenderer(@Nullable S renderState, ItemStack itemStack, EquipmentSlot equipmentSlot,
                                                                                               EquipmentModel.LayerType type, @Nullable BipedEntityModel<S> original) {
                // Important that we do this. If we just instantiate it directly in the field it can cause incompatibilities with some mods.
                if(this.renderer == null)
                    this.renderer = new MineHelmetRenderer<>();

                return this.renderer;
            }
        });

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("mine_helmet", 20, animTest -> PlayState.CONTINUE));
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

class MineHelmetModel extends GeoModel<MineHelmetItem> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.of(LittleAdventure.MOD_ID, "geckolib/models/humanoid/mine_helmet.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.of(LittleAdventure.MOD_ID, "textures/item/mine_helmet.png");
    }

    @Override
    public Identifier getAnimationResource(MineHelmetItem animatable) {
        return Identifier.of(LittleAdventure.MOD_ID, "geckolib/animations/humanoid/mine_helmet.animation.json");
    }
}

class MineHelmetRenderer<R extends BipedEntityRenderState & GeoRenderState> extends GeoArmorRenderer<MineHelmetItem, R> {
    public MineHelmetRenderer() {
        super(new MineHelmetModel());
    }
}