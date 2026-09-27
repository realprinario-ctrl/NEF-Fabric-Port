package com.nef.notenoughfakepixel.message;

import com.nef.notenoughfakepixel.NefRuntime;
import com.nef.notenoughfakepixel.feature.FeatureRegistry;
import net.minecraft.text.Text;

import java.util.Locale;

public final class NefMessageEngine {
    private NefMessageEngine() {}

    public static boolean allowChat(Text message) {
        String s = message.getString().toLowerCase(Locale.ROOT);

        if (FeatureRegistry.isEnabled("qol.chat_disable_friend_join")
                && (s.contains("joined the lobby") || s.contains("joined your party") || s.contains("joined the game"))
                && s.contains("friend")) {
            return false;
        }

        if (FeatureRegistry.isEnabled("qol.chat_disable_info_watchdog")
                && (s.contains("watchdog") || s.contains("info:"))) {
            return false;
        }

        if (FeatureRegistry.isEnabled("qol.chat_disable_selling_ranks")
                && (s.contains("sold") || s.contains("selling rank"))) {
            return false;
        }

        return true;
    }

    public static boolean allowGame(Text message, boolean overlay) {
        String s = message.getString().toLowerCase(Locale.ROOT);

        if (FeatureRegistry.isEnabled("qol.chat_cleaner")) {
            if (s.contains("you are currently on") && s.contains("profile")) {
                // Keep SkyBlock context messages useful; do not suppress them.
            }
        }

        if (!overlay && FeatureRegistry.isEnabled("alerts")) {
            if (s.contains("legendary sea creature")
                    || s.contains("trophy fish")
                    || s.contains("minos inquisitor")
                    || s.contains("ashfang")
                    || s.contains("voidgloom")
                    || s.contains("seraph")
                    || s.contains("blaze")) {
                NefRuntime.alert(net.minecraft.client.MinecraftClient.getInstance(), message.getString());
            }
        }

        if (FeatureRegistry.isEnabled("fishing.notifiers")
                && (s.contains("legendary sea creature") || s.contains("trophy"))) {
            NefRuntime.alert(net.minecraft.client.MinecraftClient.getInstance(), message.getString());
        }

        if (FeatureRegistry.isEnabled("diana.helpers")
                && (s.contains("burrow") || s.contains("minos inquisitor") || s.contains("gaia construct"))) {
            NefRuntime.alert(net.minecraft.client.MinecraftClient.getInstance(), message.getString());
        }

        if (FeatureRegistry.isEnabled("crimson.helpers")
                && s.contains("ashfang")) {
            NefRuntime.alert(net.minecraft.client.MinecraftClient.getInstance(), message.getString());
        }

        if (FeatureRegistry.isEnabled("slayers.helpers")
                && (s.contains("slayer") || s.contains("voidgloom") || s.contains("blaze attunement"))) {
            NefRuntime.alert(net.minecraft.client.MinecraftClient.getInstance(), message.getString());
        }

        return true;
    }
}
