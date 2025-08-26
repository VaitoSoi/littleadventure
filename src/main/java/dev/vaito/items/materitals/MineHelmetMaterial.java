package dev.vaito.items.materitals;

import dev.vaito.LittleAdventure;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.Map;

public class MineHelmetMaterial {
    public static final RegistryKey<EquipmentAsset> MINE_HELMET_MATERIAL_KEY = RegistryKey.of(
            EquipmentAssetKeys.REGISTRY_KEY,
            Identifier.of(LittleAdventure.MOD_ID, "mine_helmet")
    );
    public static final ArmorMaterial INSTANCE = new ArmorMaterial(
            15,
            Map.of(
                    EquipmentType.HELMET, 3,
                    EquipmentType.CHESTPLATE, 8,
                    EquipmentType.LEGGINGS, 6,
                    EquipmentType.BOOTS, 3
            ),
            5,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0F,
            0.0F,
            ItemTags.REPAIRS_IRON_ARMOR,
            MINE_HELMET_MATERIAL_KEY
    );
}
