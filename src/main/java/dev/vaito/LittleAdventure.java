package dev.vaito;

import dev.vaito.entity.ModEntities;
import dev.vaito.items.ModItemGroup;
import dev.vaito.items.ModItems;
import dev.vaito.screen.ModScreenHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class LittleAdventure implements ModInitializer {
    public static final String MOD_ID = "littleadventure";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Hello Fabric world!");
        ModItems.init();
        ModItemGroup.init();
        ModScreenHandler.init();
        ModEntities.init();

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