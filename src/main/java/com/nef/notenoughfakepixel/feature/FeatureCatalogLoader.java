package com.nef.notenoughfakepixel.feature;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class FeatureCatalogLoader {
    private FeatureCatalogLoader() {}

    public static void loadUpstreamInventory() {
        try {
            Path installed = FabricLoader.getInstance().getModContainer("notenoughfakepixel")
                    .map(c -> c.findPath("notenoughfakepixel-features.json").orElse(null)).orElse(null);
            if (installed == null) return;
            try (Reader reader = Files.newBufferedReader(installed)) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                for (JsonElement element : root.getAsJsonArray("features")) {
                    String id = element.getAsString()
                            .replace("features.", "")
                            .replace('.', '_')
                            .toLowerCase();
                    FeatureRegistry.register(id, id.replace('_', ' '), "Upstream", true);
                }
            }
        } catch (Throwable ignored) {
        }
    }
}
