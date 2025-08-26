package dev.vaito;

import dev.vaito.entity.ModEntities;
import dev.vaito.entity.custom.TorchEntityModel;
import dev.vaito.entity.custom.TorchEntityRenderer;
import dev.vaito.screen.ModScreenHandler;
import dev.vaito.screen.custom.BackpackScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class LittleAdventureClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HandledScreens.register(ModScreenHandler.BACKPACK_SCREEN_HANDLER, BackpackScreen::new);

		EntityModelLayerRegistry.registerModelLayer(TorchEntityModel.TORCH, TorchEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(ModEntities.TORCH, TorchEntityRenderer::new);
	}
}