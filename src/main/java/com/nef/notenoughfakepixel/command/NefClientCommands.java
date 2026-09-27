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
    private NefClientCommands() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var root = ClientCommandManager.literal("nef");

            var toggle = ClientCommandManager.literal("toggle");
            var toggleFeature = ClientCommandManager.argument("feature", StringArgumentType.word());
            toggleFeature.executes(ctx -> {
                String id = FeatureRegistry.findId(StringArgumentType.getString(ctx, "feature"));
                if (id == null) {
                    ctx.getSource().sendFeedback(Text.literal("§cUnknown NEF feature."));
                    return 0;
                }

                boolean enabled = FeatureRegistry.toggle(id);
                ctx.getSource().sendFeedback(Text.literal(
                    "§b[NEF] §f" + id + " §7→ " + (enabled ? "§aON" : "§cOFF")));
                return 1;
            });
            toggle.then(toggleFeature);
            root.then(toggle);

            var set = ClientCommandManager.literal("set");
            var setFeature = ClientCommandManager.argument("feature", StringArgumentType.word());
            var setEnabled = ClientCommandManager.argument("enabled", BoolArgumentType.bool());
            setEnabled.executes(ctx -> {
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
            });
            setFeature.then(setEnabled);
            set.then(setFeature);
            root.then(set);

            var list = ClientCommandManager.literal("list");
            list.executes(ctx -> {
                for (FeatureDefinition feature : FeatureRegistry.definitions()) {
                    ctx.getSource().sendFeedback(Text.literal(
                        "§7[" + (FeatureRegistry.isEnabled(feature.id()) ? "§aON" : "§cOFF")
                        + "§7] §f" + feature.id() + " §8— §7" + feature.displayName()));
                }
                return 1;
            });
            root.then(list);

            var waypoint = ClientCommandManager.literal("waypoint");

            var add = ClientCommandManager.literal("add");
            var name = ClientCommandManager.argument("name", StringArgumentType.string());
            var x = ClientCommandManager.argument("x", DoubleArgumentType.doubleArg());
            var y = ClientCommandManager.argument("y", DoubleArgumentType.doubleArg());
            var z = ClientCommandManager.argument("z", DoubleArgumentType.doubleArg());

            z.executes(ctx -> {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player == null) {
                    ctx.getSource().sendFeedback(Text.literal("§cYou must be in a world to add a waypoint."));
                    return 0;
                }

                WaypointStore.add(
                    client,
                    StringArgumentType.getString(ctx, "name"),
                    DoubleArgumentType.getDouble(ctx, "x"),
                    DoubleArgumentType.getDouble(ctx, "y"),
                    DoubleArgumentType.getDouble(ctx, "z")
                );
                ctx.getSource().sendFeedback(Text.literal("§b[NEF] §aWaypoint saved."));
                return 1;
            });

            y.then(z);
            x.then(y);
            name.then(x);
            add.then(name);
            waypoint.then(add);

            var remove = ClientCommandManager.literal("remove");
            var removeName = ClientCommandManager.argument("name", StringArgumentType.string());
            removeName.executes(ctx -> {
                String nameValue = StringArgumentType.getString(ctx, "name");
                boolean removed = WaypointStore.remove(nameValue);
                ctx.getSource().sendFeedback(Text.literal(
                    removed ? "§b[NEF] §aWaypoint removed." : "§cWaypoint not found."));
                return removed ? 1 : 0;
            });
            remove.then(removeName);
            waypoint.then(remove);

            var waypointList = ClientCommandManager.literal("list");
            waypointList.executes(ctx -> {
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
            });
            waypoint.then(waypointList);

            root.then(waypoint);

            root.executes(ctx -> {
                ctx.getSource().sendFeedback(Text.literal(
                    "§b[NEF] §f/nef toggle <feature> §7| §f/nef set <feature> <true|false> §7| §f/nef list"));
                ctx.getSource().sendFeedback(Text.literal(
                    "§b[NEF] §f/nef waypoint add <name> <x> <y> <z> §7| §f/nef waypoint remove <name>"));
                return 1;
            });

            dispatcher.register(root);
        });
    }
}
