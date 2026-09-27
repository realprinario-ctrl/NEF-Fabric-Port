package com.nef.notenoughfakepixel;

import com.nef.notenoughfakepixel.feature.FeatureRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class NefRuntime {
    private static final List<String> RECENT_ALERTS = new ArrayList<>();
    private static long lastTick;

    private NefRuntime() {}

    public static void tick(MinecraftClient client) {
        long now = System.currentTimeMillis();
        if (now - lastTick < 50L) return;
        lastTick = now;

        if (client.player == null) return;

        try {
            if (FeatureRegistry.isEnabled("qol.always_sprint")
                    && client.player.input.hasForwardMovement()
                    && !client.player.isSneaking()) {
                client.player.setSprinting(true);
            }

            if (FeatureRegistry.isEnabled("qol.fullbright")) {
                client.options.getGamma().setValue(16.0);
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

    public static String describe(String id) {
        String normalized = id.toLowerCase(Locale.ROOT);
        return normalized + ": " + FeatureRegistry.isEnabled(normalized);
    }
}
