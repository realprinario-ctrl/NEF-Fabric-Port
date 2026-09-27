package com.nef.notenoughfakepixel;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class NefFabricClient implements ClientModInitializer {
    public static final String MOD_ID = "notenoughfakepixel";

    private KeyBinding fullbrightKey;
    private KeyBinding sprintKey;
    private KeyBinding hurtCamKey;

    @Override
    public void onInitializeClient() {
        NefConfig.load();
        fullbrightKey = register("toggle_fullbright", GLFW.GLFW_KEY_B);
        sprintKey = register("toggle_always_sprint", GLFW.GLFW_KEY_UNKNOWN);
        hurtCamKey = register("toggle_no_hurt_camera", GLFW.GLFW_KEY_UNKNOWN);
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    private KeyBinding register(String id, int key) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.notenoughfakepixel." + id,
            InputUtil.Type.KEYSYM, key,
            "category.notenoughfakepixel"
        ));
    }

    private void tick(MinecraftClient client) {
        while (fullbrightKey.wasPressed()) {
            NefConfig.fullbright = !NefConfig.fullbright;
            NefConfig.save();
        }
        while (sprintKey.wasPressed()) {
            NefConfig.alwaysSprint = !NefConfig.alwaysSprint;
            NefConfig.save();
        }
        while (hurtCamKey.wasPressed()) {
            NefConfig.noHurtCamera = !NefConfig.noHurtCamera;
            NefConfig.save();
        }

        if (client.player == null) return;

        if (NefConfig.alwaysSprint
                && client.player.input.hasForwardMovement()
                && !client.player.isSneaking()) {
            client.player.setSprinting(true);
        }

        if (NefConfig.fullbright) {
            client.options.getGamma().setValue(16.0);
        }
    }
}
