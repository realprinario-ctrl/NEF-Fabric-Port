package com.nef.notenoughfakepixel.waypoint;

import com.nef.notenoughfakepixel.feature.FeatureRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

import java.util.Comparator;

public final class WaypointHud {
    private WaypointHud() {}

    public static void register() {
        HudRenderCallback.EVENT.register(WaypointHud::render);
    }

    private static void render(DrawContext draw, RenderTickCounter tickCounter) {
        if (!FeatureRegistry.isEnabled("waypoints")) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        String world = client.world.getRegistryKey().getValue().toString();
        var points = WaypointStore.all().stream()
                .filter(w -> w.world().equals(world))
                .map(w -> new Entry(w, client.player.getX() - w.x(), client.player.getY() - w.y(), client.player.getZ() - w.z()))
                .sorted(Comparator.comparingDouble(Entry::distance))
                .limit(8)
                .toList();

        int y = 8;
        for (Entry entry : points) {
            int color = entry.distance() < 10 ? 0xFFFF5555 : 0xFFFFFFFF;
            String text = String.format("%s  %.0fm", entry.waypoint().name(), entry.distance());
            draw.drawTextWithShadow(client.textRenderer, Text.literal(text), 8, y, color);
            y += 11;
        }
    }

    private record Entry(WaypointStore.Waypoint waypoint, double dx, double dy, double dz) {
        double distance() {
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }
}
