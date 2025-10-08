package dev.vaito.items;

import dev.vaito.LittleAdventure;
import dev.vaito.items.custom.*;
import dev.vaito.items.materitals.BackpackMaterial;
import dev.vaito.items.materitals.MineHelmetMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {
    public static final Item MINE_HELMET = register(
            "mine_helmet",
            settings -> new MineHelmetItem(MineHelmetMaterial.INSTANCE, EquipmentType.HELMET, settings),
            new Item.Settings()
    );
    public static final Item BACKPACK = register(
            "backpack",
            settings -> new BackpackItem(BackpackMaterial.INSTANCE, EquipmentType.CHESTPLATE, settings),
            new Item.Settings()
    );
    public static final Item TORCH_WAND = register(
            "torch_wand",
            TorchWand::new,
            new Item.Settings()
    );

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(LittleAdventure.MOD_ID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item.
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static void init() {
        LittleAdventure.LOGGER.info("Loading mod items...");
    }
}