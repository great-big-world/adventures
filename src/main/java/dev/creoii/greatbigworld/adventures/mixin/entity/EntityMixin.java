package dev.creoii.greatbigworld.adventures.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.Adventures;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "tickPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getWorld(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/server/world/ServerWorld;"))
    private void gbw$syncNetherDestination(CallbackInfo ci, @Local MinecraftServer minecraftServer, @Local RegistryKey<World> registryKey) {
        ServerWorld serverWorld = minecraftServer.getWorld(registryKey);
        if (serverWorld != null) {
            if ((Entity) (Object) this instanceof ServerPlayerEntity serverPlayer && serverWorld.getDimensionEntry().getKey().isPresent()) {
                ServerPlayNetworking.send(serverPlayer, new Adventures.TeleportDestination(serverWorld.getDimensionEntry().getKey().get()));
            }
        }
    }
}
