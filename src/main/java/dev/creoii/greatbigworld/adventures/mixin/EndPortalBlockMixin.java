package dev.creoii.greatbigworld.adventures.mixin;

import dev.creoii.greatbigworld.adventures.Adventures;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {
    @Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tryUsePortal(Lnet/minecraft/block/Portal;Lnet/minecraft/util/math/BlockPos;)V"))
    private void gbw$syncNetherDestination(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof ServerPlayerEntity serverPlayer && world.getDimensionEntry().getKey().isPresent()) {
            ServerPlayNetworking.send(serverPlayer, new Adventures.TeleportDestinationS2C(world.getRegistryKey() == World.END ? DimensionTypes.OVERWORLD : DimensionTypes.THE_END));
        }
    }
}
