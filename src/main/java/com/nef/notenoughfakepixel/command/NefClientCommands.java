package com.nef.notenoughfakepixel.command;

import com.nef.notenoughfakepixel.feature.FeatureDefinition;
import com.nef.notenoughfakepixel.feature.FeatureRegistry;
import com.nef.notenoughfakepixel.waypoint.WaypointStore;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public final class NefClientCommands {
    private NefClientCommands() {}

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var root = ClientCommandManager.literal("nef");

            root.then(ClientCommandManager.literal("toggle")
                .then(ClientCommandManager.argument("feature", StringArgumentType.word())
                    .executes(ctx -> {
                        String id = FeatureRegistry.findId(StringArgumentType.getString(ctx, "feature"));
                        if (id == null) {
                            ctx.getSource().sendFeedback(Text.literal("§cUnknown NEF feature."));
                            return 0;
                        }
                        boolean enabled = FeatureRegistry.toggle(id);
                        ctx.getSource().sendFeedback(Text.literal(
                            "§b[NEF] §f" + id + " §7→ " + (enabled ? "§aON" : "§cOFF")));
                        return 1;
                    })));

            root.then(ClientCommandManager.literal("set")
                .then(ClientCommandManager.argument("feature", StringArgumentType.word())
                    .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                        .executes(ctx -> {
                            String id = FeatureRegistry.findId(StringArgumentType.getString(ctx, "feature"));
                            if (id == null) {
                                ctx.getSource().sendFeedback(Text.literal("§cUnknown NEF feature."));
                                return 0;
                            }
                            boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
                            FeatureRegistry.setEnabled(id, enabled);
                            ctx.getSource().sendFeedback(Text.literal(
                                "§b[NEF] §f" + id + " §7→ " + (enabled ? "§aON" : "§cOFF")));
                            return 1;
                        }))));

            root.then(ClientCommandManager.literal("list")
                .executes(ctx -> {
                    for (FeatureDefinition feature : FeatureRegistry.definitions()) {
                        ctx.getSource().sendFeedback(Text.literal(
                            "§7[" + (FeatureRegistry.isEnabled(feature.id()) ? "§aON" : "§cOFF")
                            + "§7] §f" + feature.id() + " §8— §7" + feature.displayName()));
                    }
                    return 1;
                }));

            var waypoint = ClientCommandManager.literal("waypoint");

            waypoint.then(ClientCommandManager.literal("add")
                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                    .then(ClientCommandManager.argument("x", DoubleArgumentType.doubleArg())
                        .then(ClientCommandManager.argument("y", DoubleArgumentType.doubleArg())
                            .then(ClientCommandManager.argument("z", DoubleArgumentType.doubleArg())
                                .executes(ctx -> {
                                    MinecraftClient client = MinecraftClient.getInstance();
                                    if (client.player == null) return 0;

                                    WaypointStore.add(
                                        client,
                                        StringArgumentType.getString(ctx, "name"),
                                        DoubleArgumentType.getDouble(ctx, "x"),
                                        DoubleArgumentType.getDouble(ctx, "y"),
                                        DoubleArgumentType.getDouble(ctx, "z")
                                    );
                                    ctx.getSource().sendFeedback(Text.literal("§b[NEF] §aWaypoint saved."));
                                    return 1;
                                })))))

            waypoint.then(ClientCommandManager.literal("remove")
                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                    .executes(ctx -> {
                        boolean removed = WaypointStore.remove(StringArgumentType.getString(ctx, "name"));
                        ctx.getSource().sendFeedback(Text.literal(
                            removed ? "§b[NEF] §aWaypoint removed." : "§cWaypoint not found."));
                        return removed ? 1 : 0;
                    })));

            waypoint.then(ClientCommandManager.literal("list")
                .executes(ctx -> {
                    for (var waypointEntry : WaypointStore.all()) {
                        ctx.getSource().sendFeedback(Text.literal(String.format(
                            "§b[NEF] §f%s §7(%.1f, %.1f, %.1f) §8[%s]",
                            waypointEntry.name(),
                            waypointEntry.x(),
                            waypointEntry.y(),
                            waypointEntry.z(),
                            waypointEntry.world()
                        )));
                    }
                    return 1;
                }));

            root.then(waypoint);

            root.executes(ctx -> {
                ctx.getSource().sendFeedback(Text.literal(
                    "§b[NEF] §f/nef toggle <feature> §7| §f/nef set <feature> <true|false> §7| §f/nef list"));
                ctx.getSource().sendFeedback(Text.literal(
                    "§b[NEF] §f/nef waypoint add \"name\" <x> <y> <z> §7| §f/nef waypoint remove \"name\""));
                return 1;
            });

            dispatcher.register(root);
        });
    }
}
