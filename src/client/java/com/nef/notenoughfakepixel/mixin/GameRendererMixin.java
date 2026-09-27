package com.nef.notenoughfakepixel.mixin;

import com.nef.notenoughfakepixel.feature.FeatureRegistry;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void nef$disableHurtCamera(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (FeatureRegistry.isEnabled("qol.no_hurt_camera")) {
            ci.cancel();
        }
    }
}
