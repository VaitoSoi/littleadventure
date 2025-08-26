package dev.vaito.items.custom;

import com.google.common.collect.Streams;
import dev.vaito.LittleAdventure;
import dev.vaito.entity.custom.TorchEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ProjectileItem;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class TorchWand extends Item implements GeoItem, ProjectileItem {
    public static float POWER = 1.5F;
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public TorchWand(Item.Settings settings) {
        super(settings.maxCount(1));

        GeoItem.registerSyncedAnimatable(this);
    }

    /*
     * Gecko things
     * */
    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private TorchWandRenderer renderer;

            @Override
            public GeoItemRenderer<TorchWand> getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new TorchWandRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(
                "Idle",
                0,
                animation -> {
                    animation.setAnimation(IDLE);
                    return PlayState.CONTINUE;
                })
        );
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    /*
     * Throwable
     * */
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        PlayerInventory inventory = user.getInventory();
        if (!inventory.contains(new ItemStack(Items.TORCH)))
            return ActionResult.FAIL;
        ItemStack itemStack;
        try {
            itemStack = Streams.stream(inventory.iterator())
                    .filter(stack -> stack.isOf(Items.TORCH))
                    .findFirst()
                    .orElseThrow();
        } catch (NoSuchElementException error) {
            return ActionResult.FAIL;
        }
        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ENTITY_SNOWBALL_THROW,
                SoundCategory.NEUTRAL,
                0.5F,
                0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (world instanceof ServerWorld serverWorld) {
            ProjectileEntity.spawnWithVelocity(TorchEntity::new, serverWorld, itemStack, user, 0.0F, POWER, 1.0F);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        itemStack.decrementUnlessCreative(1, user);
        return ActionResult.SUCCESS;
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        return new TorchEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
    }
}

class TorchWandModel extends GeoModel<TorchWand> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.of(LittleAdventure.MOD_ID, "geckolib/models/item/torch_wand.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.of(LittleAdventure.MOD_ID, "textures/item/torch_wand.png");
    }

    @Override
    public Identifier getAnimationResource(TorchWand animatable) {
        return Identifier.of(LittleAdventure.MOD_ID, "geckolib/animations/item/torch_wand.animation.json");
    }
}

class TorchWandRenderer extends GeoItemRenderer<TorchWand> {
    public TorchWandRenderer() {
        super(new TorchWandModel());
    }
}