package dev.vaito;

import dev.vaito.entity.ModEntities;
import dev.vaito.items.ModItemGroup;
import dev.vaito.items.ModItems;
import dev.vaito.screen.ModScreenHandler;
import net.fabricmc.api.ModInitializer;

public class LittleAdventure implements ModInitializer {
    public static final String MOD_ID = "littleadventure";

    @Override
    public void onInitialize() {
        ModItems.init();
        ModItemGroup.init();
        ModScreenHandler.init();
        ModEntities.init();
    }
}
