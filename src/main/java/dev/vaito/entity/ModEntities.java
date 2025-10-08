package dev.vaito.entity;

import dev.vaito.LittleAdventure;
import dev.vaito.entity.custom.TorchEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {
    private static final RegistryKey<EntityType<?>> TORCH_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(LittleAdventure.MOD_ID, "torch"));

    public static final EntityType<TorchEntity> TORCH = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(LittleAdventure.MOD_ID, "torch"),
            EntityType.Builder.<TorchEntity>create(TorchEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 1.15f).build(TORCH_KEY));

    public static void init() {
        LittleAdventure.LOGGER.info("Loading Entity...");
    }
}
