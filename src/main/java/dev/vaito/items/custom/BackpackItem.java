package dev.vaito.items.custom;

import dev.vaito.LittleAdventure;
import dev.vaito.screen.custom.BackpackScreenHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
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

public class BackpackItem extends Item implements GeoItem {
    public final static int INVENTORY_SIZE = 27;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BackpackItem(ArmorMaterial armorMaterial, EquipmentType type, Settings settings) {
        super(settings.armor(armorMaterial, type));
    }

    /**
     * Get a 27-slot container component (create if missing).
     */
    public static ContainerComponent getAndAppendContainer(ItemStack stack) {
        var inventory = new ObjectArrayList<ItemStack>(INVENTORY_SIZE);
        ContainerComponent component = stack.get(DataComponentTypes.CONTAINER);
        if (component != null)
            component.stream().limit(INVENTORY_SIZE).forEach(inventory::add);
        while (inventory.size() < INVENTORY_SIZE) inventory.add(ItemStack.EMPTY);
        ContainerComponent newComponent = ContainerComponent.fromStacks(inventory);
        stack.set(DataComponentTypes.CONTAINER, newComponent);
        return newComponent;
    }

    /**
     * Replace stored contents from a 27-slot view.
     */
    public static void saveContainer(ItemStack holding, java.util.List<ItemStack> inventory) {
        var fixed = new ObjectArrayList<ItemStack>(INVENTORY_SIZE);
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            fixed.add(i < inventory.size() ? inventory.get(i) : ItemStack.EMPTY);
        }
        holding.set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(fixed));
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            user.openHandledScreen(new ScreenFactory(hand));
            return ActionResult.SUCCESS;
        }

        return ActionResult.SUCCESS;
    }


    /*
     * Gecko stuffs
     */
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private BackpackRenderer<?> renderer;

            public <S extends BipedEntityRenderState> BackpackRenderer<?> getGeoArmorRenderer(@Nullable S renderState, ItemStack itemStack, EquipmentSlot equipmentSlot,
                                                                                              EquipmentModel.LayerType type, @Nullable BipedEntityModel<S> original) {
                // Important that we do this. If we just instantiate it directly in the field it can cause incompatibilities with some mods.
                if (this.renderer == null)
                    this.renderer = new BackpackRenderer<>();

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

class BackpackModel extends GeoModel<BackpackItem> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.of(LittleAdventure.MOD_ID, "geckolib/models/humanoid/backpack.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.of(LittleAdventure.MOD_ID, "textures/item/backpack.png");
    }

    @Override
    public Identifier getAnimationResource(BackpackItem animatable) {
        return Identifier.of(LittleAdventure.MOD_ID, "geckolib/animations/humanoid/backpack.animation.json");
    }
}

class BackpackRenderer<R extends BipedEntityRenderState & GeoRenderState> extends GeoArmorRenderer<BackpackItem, R> {
    public BackpackRenderer() {
        super(new BackpackModel());
    }
}

class ScreenFactory implements NamedScreenHandlerFactory {
    Hand openingHand;

    public ScreenFactory(Hand openingHand) {
        this.openingHand = openingHand;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen." + LittleAdventure.MOD_ID + ".backpack");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BackpackScreenHandler(syncId, playerInventory, openingHand);
    }
}
