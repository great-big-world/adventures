package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.adventures.util.AllowDebugHud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DebugScreenOverlay.class)
public class DebugHudMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "showDebugScreen", at = @At("HEAD"), cancellable = true)
    private void gbw$allowDebugHud(CallbackInfoReturnable<Boolean> cir) {
        if (minecraft.level instanceof AllowDebugHud allowDebugHud && !allowDebugHud.gbw$shouldAllowDebugHud())
            cir.setReturnValue(false);
    }
}
