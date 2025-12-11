package dev.creoii.greatbigworld.adventures.mixin.entity;

import dev.creoii.greatbigworld.adventures.Adventures;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "handlePortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/world/entity/Entity;"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void gbw$syncPortalDestination(CallbackInfo ci, ServerLevel serverWorld, ProfilerFiller profiler, TeleportTransition teleportTarget) {
        if (serverWorld != null && teleportTarget.newLevel() != null) {
            if ((Entity) (Object) this instanceof ServerPlayer serverPlayer && serverWorld.dimensionTypeRegistration().unwrapKey().isPresent()) {
                ServerPlayNetworking.send(serverPlayer, new Adventures.TeleportDestinationS2C(teleportTarget.newLevel().dimensionTypeRegistration().unwrapKey().get()));
            }
        }
    }
}
