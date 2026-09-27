package com.nef.notenoughfakepixel.waypoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class WaypointStore {
    public record Waypoint(String name, String world, double x, double y, double z) {}

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("notenoughfakepixel-waypoints.json");
    private static final List<Waypoint> WAYPOINTS = new ArrayList<>();
    private static boolean loaded;

    private WaypointStore() {}

    public static List<Waypoint> all() {
        load();
        return List.copyOf(WAYPOINTS);
    }

    public static void add(MinecraftClient client, String name, double x, double y, double z) {
        load();
        String world = client.world == null ? "unknown" : client.world.getRegistryKey().getValue().toString();
        WAYPOINTS.removeIf(w -> w.name().equalsIgnoreCase(name) && w.world().equals(world));
        WAYPOINTS.add(new Waypoint(name, world, x, y, z));
        save();
    }

    public static boolean remove(String name) {
        load();
        boolean changed = WAYPOINTS.removeIf(w -> w.name().equalsIgnoreCase(name));
        if (changed) save();
        return changed;
    }

    private static void load() {
        if (loaded) return;
        loaded = true;
        if (!Files.exists(FILE)) return;
        try {
            var type = new TypeToken<List<Waypoint>>() {}.getType();
            List<Waypoint> parsed = GSON.fromJson(Files.readString(FILE), type);
            if (parsed != null) WAYPOINTS.addAll(parsed);
        } catch (Exception ignored) {
        }
    }

    private static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(WAYPOINTS));
        } catch (IOException ignored) {
        }
    }
}
