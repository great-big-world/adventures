package dev.creoii.greatbigworld.adventures.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.Adventures;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {
    @Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;moveToWorld(Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;"))
    private void gbw$syncNetherDestination(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci, @Local ServerWorld serverWorld) {
        if (entity instanceof ServerPlayerEntity serverPlayer && serverWorld.getDimensionEntry().getKey().isPresent()) {
            ServerPlayNetworking.send(serverPlayer, new Adventures.TeleportDestination(serverWorld.getDimensionEntry().getKey().get()));
        }
    }
}
