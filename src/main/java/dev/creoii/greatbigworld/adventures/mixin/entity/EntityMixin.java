package dev.creoii.greatbigworld.adventures.mixin.entity;

import dev.creoii.greatbigworld.adventures.Adventures;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.TeleportTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "tickPortalTeleportation", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/entity/Entity;"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void gbw$syncPortalDestination(CallbackInfo ci, ServerWorld serverWorld, Profiler profiler, TeleportTarget teleportTarget) {
        if (serverWorld != null && teleportTarget.world() != null) {
            if ((Entity) (Object) this instanceof ServerPlayerEntity serverPlayer && serverWorld.getDimensionEntry().getKey().isPresent()) {
                ServerPlayNetworking.send(serverPlayer, new Adventures.TeleportDestinationS2C(teleportTarget.world().getDimensionEntry().getKey().get()));
            }
        }
    }
}
