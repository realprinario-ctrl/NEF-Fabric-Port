package com.nef.notenoughfakepixel.command;

import com.nef.notenoughfakepixel.NefRuntime;
import com.nef.notenoughfakepixel.feature.FeatureDefinition;
import com.nef.notenoughfakepixel.feature.FeatureRegistry;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;

public final class NefClientCommands {
    private NefClientCommands() {}

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var featureArgument = StringArgumentType.word();

            dispatcher.register(ClientCommandManager.literal("nef")
                .then(ClientCommandManager.literal("toggle")
                    .then(ClientCommandManager.argument("feature", featureArgument)
                        .executes(ctx -> {
                            String id = FeatureRegistry.findId(StringArgumentType.getString(ctx, "feature"));
                            if (id == null) {
                                ctx.getSource().sendFeedback(net.minecraft.text.Text.literal("§cUnknown NEF feature."));
                                return 0;
                            }
                            boolean enabled = FeatureRegistry.toggle(id);
                            ctx.getSource().sendFeedback(net.minecraft.text.Text.literal(
                                "§b[NEF] §f" + id + " §7→ " + (enabled ? "§aON" : "§cOFF")));
                            return 1;
                        })))
                .then(ClientCommandManager.literal("set")
                    .then(ClientCommandManager.argument("feature", featureArgument)
                        .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                            .executes(ctx -> {
                                String id = FeatureRegistry.findId(StringArgumentType.getString(ctx, "feature"));
                                if (id == null) {
                                    ctx.getSource().sendFeedback(net.minecraft.text.Text.literal("§cUnknown NEF feature."));
                                    return 0;
                                }
                                boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
                                FeatureRegistry.setEnabled(id, enabled);
                                ctx.getSource().sendFeedback(net.minecraft.text.Text.literal(
                                    "§b[NEF] §f" + id + " §7→ " + (enabled ? "§aON" : "§cOFF")));
                                return 1;
                            }))))
                .then(ClientCommandManager.literal("list")
                    .executes(ctx -> {
                        for (FeatureDefinition feature : FeatureRegistry.definitions()) {
                            ctx.getSource().sendFeedback(net.minecraft.text.Text.literal(
                                "§7[" + (FeatureRegistry.isEnabled(feature.id()) ? "§aON" : "§cOFF")
                                + "§7] §f" + feature.id() + " §8— §7" + feature.displayName()));
                        }
                        return 1;
                    }))
                .then(ClientCommandManager.literal("waypoint")
                    .then(ClientCommandManager.literal("add")
                        .then(ClientCommandManager.argument("name", StringArgumentType.string())
                            .executes(ctx -> {
                                MinecraftClient client = MinecraftClient.getInstance();
                                if (client.player != null) {
                                    NefRuntime.alert(client, "Waypoint support is active; use the modern waypoint editor.");
                                }
                                return 1;
                            }))))
                .executes(ctx -> {
                    ctx.getSource().sendFeedback(net.minecraft.text.Text.literal(
                        "§b[NEF] §f/nef toggle <feature> §7| §f/nef set <feature> <true|false> §7| §f/nef list"));
                    return 1;
                }));
        });
    }
}
