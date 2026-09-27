package com.nef.notenoughfakepixel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class NefConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("notenoughfakepixel.json");

    public static boolean fullbright;
    public static boolean alwaysSprint;
    public static boolean noHurtCamera;

    private NefConfig() {}

    public static void load() {
        if (!Files.exists(FILE)) return;
        try {
            Data d = GSON.fromJson(Files.readString(FILE), Data.class);
            if (d != null) {
                fullbright = d.fullbright;
                alwaysSprint = d.alwaysSprint;
                noHurtCamera = d.noHurtCamera;
            }
        } catch (Exception ignored) {}
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(new Data(fullbright, alwaysSprint, noHurtCamera)));
        } catch (IOException ignored) {}
    }

    private record Data(boolean fullbright, boolean alwaysSprint, boolean noHurtCamera) {}
}
