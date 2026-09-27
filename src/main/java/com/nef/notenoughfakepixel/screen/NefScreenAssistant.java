package com.nef.notenoughfakepixel.screen;

import com.nef.notenoughfakepixel.NefRuntime;
import com.nef.notenoughfakepixel.feature.FeatureRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;

import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NefScreenAssistant {
    private static final Pattern NUMBER = Pattern.compile("(\\d+)");
    private static long lastNotice;

    private NefScreenAssistant() {}

    public static void tick(MinecraftClient client) {
        if (!FeatureRegistry.isEnabled("dungeons.terminals") || !(client.currentScreen instanceof HandledScreen<?> screen)) return;

        String title = screen.getTitle().getString().toLowerCase(Locale.ROOT);
        boolean terminal = title.contains("click in order")
                || title.contains("starts with")
                || title.contains("select all")
                || title.contains("correct all the panes")
                || title.contains("click the button on the floor");

        if (!terminal) return;
        if (System.currentTimeMillis() - lastNotice < 750L) return;

        Slot target = screen.getScreenHandler().slots.stream()
                .filter(s -> !s.getStack().isEmpty())
                .filter(s -> NUMBER.matcher(s.getStack().getName().getString()).find())
                .min(Comparator.comparingInt(NefScreenAssistant::number))
                .orElse(null);

        if (target != null) {
            lastNotice = System.currentTimeMillis();
            NefRuntime.alert(client, "Terminal target slot: " + target.id);
        }
    }

    private static int number(Slot slot) {
        Matcher m = NUMBER.matcher(slot.getStack().getName().getString());
        return m.find() ? Integer.parseInt(m.group(1)) : Integer.MAX_VALUE;
    }
}
