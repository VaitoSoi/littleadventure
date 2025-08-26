package dev.vaito.screen;

import dev.vaito.LittleAdventure;
import dev.vaito.screen.custom.BackpackScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandler {
    public final static ScreenHandlerType<BackpackScreenHandler> BACKPACK_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    Identifier.of(LittleAdventure.MOD_ID, "backpack_screen_handler"),
                    new ScreenHandlerType<>(BackpackScreenHandler::new, FeatureSet.empty())
            );

    public static void init() {
        LittleAdventure.LOGGER.info("Loading Screen handler...");
    }
}
