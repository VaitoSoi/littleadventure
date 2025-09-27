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

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LittleAdventureClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HandledScreens.register(ModScreenHandler.BACKPACK_SCREEN_HANDLER, BackpackScreen::new);

		EntityModelLayerRegistry.registerModelLayer(TorchEntityModel.TORCH, TorchEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(ModEntities.TORCH, TorchEntityRenderer::new);
		
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.world != null) {
                if (client.player.getMainHandStack().getItem() == Items.COMPASS ||
                        client.player.getMainHandStack().getItem() == Items.RECOVERY_COMPASS) {
                    String coords = String.format("X: %d Y: %d Z: %d",
                            client.player.getBlockX(),
                            client.player.getBlockY(),
                            client.player.getBlockZ());
                    client.inGameHud.setOverlayMessage(Text.of(coords), false);
                } else if (client.player.getMainHandStack().getItem() == Items.CLOCK) {
                    String time = getTime(client.world, client.player.getMainHandStack().getName().getString());
                    client.inGameHud.setOverlayMessage(Text.of(time), false);
                }
            }
        });
	}

    private static @NotNull String getTime(World world, String name) {
        long totalTick = world.getTimeOfDay() + 6_000;
        int days = 0;
        while (totalTick >= 24000) {
            totalTick -= 24000;
            days++;
        }
        int hour = (int) totalTick / 1000 % 24;
        int minute = (int) (totalTick % 1000) * 60 / 1000;
        String hourString = hour < 10 ? "0" + hour : "" + hour;
        String minuteString = minute == 0 ? "00" : minute < 10 ? "0" + minute : "" + minute;
        return Objects.equals(name, "DEBUG LORDS")
                ? String.format("So the total tick is %d, werido -_-", totalTick)
                : ("Day" + (days > 1 ? "s" : "") + " " + days + " " + hourString + ":" + minuteString);
    }
}
