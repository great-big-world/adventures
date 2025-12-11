package dev.creoii.greatbigworld.adventures.mixin;

import dev.creoii.greatbigworld.adventures.Adventures;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {
    @Inject(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setAsInsidePortal(Lnet/minecraft/world/level/block/Portal;Lnet/minecraft/core/BlockPos;)V"))
    private void gbw$syncNetherDestination(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl, CallbackInfo ci) {
        if (entity instanceof ServerPlayer serverPlayer && world.dimensionTypeRegistration().unwrapKey().isPresent()) {
            ServerPlayNetworking.send(serverPlayer, new Adventures.TeleportDestinationS2C(world.dimension() == Level.END ? BuiltinDimensionTypes.OVERWORLD : BuiltinDimensionTypes.END));
        }
    }
}
