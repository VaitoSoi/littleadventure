package dev.vaito.items;

import dev.vaito.LittleAdventure;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final RegistryKey<ItemGroup> KEY = RegistryKey.of(
            Registries.ITEM_GROUP.getKey(),
            Identifier.of(LittleAdventure.MOD_ID, "item_group")
    );
    public static final ItemGroup INSTANCE = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.BACKPACK))
            .displayName(Text.translatable("itemGroup.littleadventure"))
            .build();

    public static void init() {
        LittleAdventure.LOGGER.info("Loading Item group...");

        Registry.register(Registries.ITEM_GROUP, KEY, INSTANCE);
        ItemGroupEvents.modifyEntriesEvent(KEY)
                .register((itemGroup) -> {
                    itemGroup.add(ModItems.BACKPACK);
                    itemGroup.add(ModItems.MINE_HELMET);
                    itemGroup.add(ModItems.TORCH_WAND);
                });
    }
}
