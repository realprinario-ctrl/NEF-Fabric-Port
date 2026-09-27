package com.nef.notenoughfakepixel;

import com.nef.notenoughfakepixel.command.NefClientCommands;
import com.nef.notenoughfakepixel.feature.FeatureCatalogLoader;
import com.nef.notenoughfakepixel.feature.FeatureRegistry;
import com.nef.notenoughfakepixel.message.NefMessageEngine;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

public final class NefFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NefConfig.load();
        FeatureRegistry.load();
        FeatureCatalogLoader.loadUpstreamInventory();
        NefClientCommands.register();

        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) ->
            NefMessageEngine.allowChat(message)
        );

        ClientReceiveMessageEvents.ALLOW_GAME.register(NefMessageEngine::allowGame);

        ClientTickEvents.END_CLIENT_TICK.register(NefRuntime::tick);
    }
}
