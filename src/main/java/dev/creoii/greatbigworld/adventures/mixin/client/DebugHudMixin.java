package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.adventures.util.AllowDebugHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "shouldShowDebugHud", at = @At("HEAD"), cancellable = true)
    private void gbw$allowDebugHud(CallbackInfoReturnable<Boolean> cir) {
        if (client.world instanceof AllowDebugHud allowDebugHud && !allowDebugHud.gbw$shouldAllowDebugHud())
            cir.setReturnValue(false);
    }
}
