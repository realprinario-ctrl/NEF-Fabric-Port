package com.nef.notenoughfakepixel.feature;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class FeatureRegistry {
    private static final Gson GSON = new Gson();
    private static final Path STATE_FILE = FabricLoader.getInstance().getConfigDir().resolve("notenoughfakepixel-features.json");
    private static final Map<String, FeatureDefinition> DEFINITIONS = new LinkedHashMap<>();
    private static final Map<String, Boolean> STATE = new HashMap<>();

    private FeatureRegistry() {}

    public static void load() {
        DEFINITIONS.clear();
        FeatureDefaults.register();
        register("qol.always_sprint", "Always Sprint", "Quality of Life", true);
        register("qol.fullbright", "Fullbright", "Quality of Life", false);
        register("qol.no_hurt_camera", "No Hurt Camera", "Quality of Life", true);
        register("qol.slot_locking", "Slot Locking", "Quality of Life", true);
        register("qol.scrollable_tooltips", "Scrollable Tooltips", "Quality of Life", true);
        register("qol.item_animations", "Item Animations", "Quality of Life", true);
        register("qol.hide_players_near_npcs", "Hide Players Near NPCs", "Quality of Life", false);
        register("qol.storage_overlay", "Storage Overlay", "Quality of Life", true);
        register("qol.equipment_overlay", "Equipment Overlay", "Quality of Life", true);
        register("qol.etherwarp_overlay", "Etherwarp Overlay", "Quality of Life", true);
        register("qol.end_nodes", "Show Ender Nodes", "Quality of Life", true);
        register("dungeons.solvers", "Dungeon Solvers", "Dungeons", true);
        register("dungeons.map", "Dungeon Map", "Dungeons", true);
        register("dungeons.score", "Score Overlay", "Dungeons", true);
        register("dungeons.secrets", "Secret Overlay", "Dungeons", true);
        register("dungeons.terminals", "Terminal Solvers", "Dungeons", true);
        register("dungeons.m7", "Master Mode 7 Helpers", "Dungeons", true);
        register("mining.overlay", "Mining Overlay", "Mining", true);
        register("mining.puzzler", "Puzzler Solver", "Mining", true);
        register("mining.crystal_hollows", "Crystal Hollows Helpers", "Mining", true);
        register("fishing.notifiers", "Fishing Notifiers", "Fishing", true);
        register("farming.crop_height", "Crop Height", "Farming", true);
        register("diana.helpers", "Diana Helpers", "Diana", true);
        register("crimson.helpers", "Crimson Isle Helpers", "Crimson Isle", true);
        register("slayers.helpers", "Slayer Helpers", "Slayers", true);
        register("chocolate.helpers", "Chocolate Factory Helpers", "Chocolate Factory", true);
        register("alerts", "Alerts", "General", true);
        register("waypoints", "Waypoints", "General", true);
        register("capes", "Capes", "General", true);
        register("cosmetics", "Cosmetics", "General", true);

        loadState();
    }

    public static void register(String id, String displayName, String category, boolean defaultEnabled) {
        DEFINITIONS.putIfAbsent(id, new FeatureDefinition(id, displayName, category, defaultEnabled));
        STATE.putIfAbsent(id, defaultEnabled);
    }

    public static Set<String> ids() {
        return Collections.unmodifiableSet(DEFINITIONS.keySet());
    }

    public static Collection<FeatureDefinition> definitions() {
        return Collections.unmodifiableCollection(DEFINITIONS.values());
    }

    public static boolean isEnabled(String id) {
        FeatureDefinition definition = DEFINITIONS.get(id);
        return definition != null && STATE.getOrDefault(id, definition.defaultEnabled());
    }

    public static void setEnabled(String id, boolean enabled) {
        if (!DEFINITIONS.containsKey(id)) return;
        STATE.put(id, enabled);
        saveState();
    }

    public static boolean toggle(String id) {
        boolean next = !isEnabled(id);
        setEnabled(id, next);
        return next;
    }

    public static String findId(String input) {
        String normalized = input.toLowerCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
        if (DEFINITIONS.containsKey(normalized)) return normalized;
        for (FeatureDefinition d : DEFINITIONS.values()) {
            if (d.displayName().toLowerCase(Locale.ROOT).replace(' ', '_').equals(normalized)) return d.id();
        }
        return null;
    }

    private static void loadState() {
        if (!Files.exists(STATE_FILE)) return;
        try {
            Type type = new TypeToken<Map<String, Boolean>>() {}.getType();
            Map<String, Boolean> loaded = GSON.fromJson(Files.readString(STATE_FILE), type);
            if (loaded != null) {
                for (var entry : loaded.entrySet()) {
                    if (DEFINITIONS.containsKey(entry.getKey())) STATE.put(entry.getKey(), Boolean.TRUE.equals(entry.getValue()));
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static void saveState() {
        try {
            Files.createDirectories(STATE_FILE.getParent());
            Files.writeString(STATE_FILE, GSON.toJson(STATE));
        } catch (IOException ignored) {
        }
    }
}
