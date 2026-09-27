package com.nef.notenoughfakepixel;

import com.nef.notenoughfakepixel.command.NefClientCommands;
import com.nef.notenoughfakepixel.feature.FeatureCatalogLoader;
import com.nef.notenoughfakepixel.feature.FeatureRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class NefFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NefConfig.load();
        FeatureRegistry.load();
        FeatureCatalogLoader.loadUpstreamInventory();
        NefClientCommands.register();

        ClientTickEvents.END_CLIENT_TICK.register(NefRuntime::tick);
    }
}
