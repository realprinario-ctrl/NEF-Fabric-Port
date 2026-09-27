package com.nef.notenoughfakepixel.message;

import com.nef.notenoughfakepixel.NefRuntime;
import com.nef.notenoughfakepixel.feature.FeatureRegistry;
import net.minecraft.text.Text;

import java.util.Locale;
import java.util.regex.Pattern;

public final class NefMessageEngine {
    private static final Pattern SELLING_RANK = Pattern.compile("(?i).*\\b(selling)\\b.*\\brank(s)?\\b.*");
    private static final Pattern FRIEND_JOIN = Pattern.compile("(?i).*friend\\s*>.*");
    private static final Pattern WATCHDOG = Pattern.compile("(?i).*\\[watchdog announcement\\].*");
    private static final Pattern PLAYER_INFO = Pattern.compile("(?i).*\\[player information\\].*");
    private static final Pattern GAME_INVITE = Pattern.compile("(?i).*invites you to play.*");
    private static final Pattern SERVER_ANNOUNCEMENT = Pattern.compile(
            "(?i).*?(\\[(visit|open|get|join)\\b[^\\]]*\\]|use /(?:help|report|streams)\\b|"
          + "is the best rank for price and benefits|protect yourself from scams|spotted a cheater|"
          + "a special discount|is streaming on the server).*");

    private NefMessageEngine() {}

    public static boolean allowChat(Text message) {
        String s = message.getString().toLowerCase(Locale.ROOT);

        if (FeatureRegistry.isEnabled("qol.chat_disable_friend_join") && FRIEND_JOIN.matcher(s).matches()) return false;
        if (FeatureRegistry.isEnabled("qol.chat_disable_info_watchdog")
                && (WATCHDOG.matcher(s).matches() || PLAYER_INFO.matcher(s).matches())) return false;
        if (FeatureRegistry.isEnabled("qol.chat_disable_selling_ranks") && SELLING_RANK.matcher(s).matches()) return false;
        if (FeatureRegistry.isEnabled("qol.chat_disable_game_invites") && GAME_INVITE.matcher(s).matches()) return false;
        if (FeatureRegistry.isEnabled("qol.chat_disable_server_announcements") && SERVER_ANNOUNCEMENT.matcher(s).matches()) return false;
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
