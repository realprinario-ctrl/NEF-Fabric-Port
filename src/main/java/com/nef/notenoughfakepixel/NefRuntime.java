package com.nef.notenoughfakepixel;

import com.nef.notenoughfakepixel.feature.FeatureRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import com.nef.notenoughfakepixel.screen.NefScreenAssistant;

import java.util.ArrayList;
import java.util.List;

public final class NefRuntime {
    private static final List<String> RECENT_ALERTS = new ArrayList<>();
    private static long lastTick;
    private static double savedGamma = 1.0;
    private static boolean savedGammaState;

    private NefRuntime() {}

    public static void tick(MinecraftClient client) {
        long now = System.currentTimeMillis();
        if (now - lastTick < 50L) return;
        lastTick = now;

        if (client.player == null) return;

        try {
            NefScreenAssistant.tick(client);

            if (FeatureRegistry.isEnabled("qol.always_sprint")
                    && client.player.input.hasForwardMovement()
                    && !client.player.isSneaking()) {
                client.player.setSprinting(true);
            }

            boolean fullbright = FeatureRegistry.isEnabled("qol.fullbright");
            if (fullbright && !savedGammaState) {
                savedGamma = client.options.getGamma().getValue();
                savedGammaState = true;
            }

            if (fullbright) {
                client.options.getGamma().setValue(16.0);
            } else if (savedGammaState) {
                client.options.getGamma().setValue(savedGamma);
                savedGammaState = false;
            }
        } catch (Throwable ignored) {
            // A feature must never bring down the client.
        }
    }

    public static void alert(MinecraftClient client, String message) {
        if (client.player == null || !FeatureRegistry.isEnabled("alerts")) return;
        String clean = message == null ? "" : message.trim();
        if (clean.isEmpty()) return;
        while (RECENT_ALERTS.size() >= 8) RECENT_ALERTS.remove(0);
        RECENT_ALERTS.add(clean);
        client.player.sendMessage(Text.literal("§b[NEF] §f" + clean), false);
    }

    public static List<String> recentAlerts() {
        return List.copyOf(RECENT_ALERTS);
    }
}
