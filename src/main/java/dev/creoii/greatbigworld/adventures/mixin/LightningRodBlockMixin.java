package dev.creoii.greatbigworld.adventures.mixin;

import net.minecraft.core.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningRodBlock.class)
public class LightningRodBlockMixin {
    @Inject(method = "onLightningStrike", at = @At("TAIL"))
    private void gbw$breakLeadsOnLightning(BlockState blockState, Level level, BlockPos blockPos, CallbackInfo ci) {
        for (LeashFenceKnotEntity entity : level.getEntitiesOfClass(LeashFenceKnotEntity.class, BlockBox.of(blockPos).aabb())) {
            entity.discard();
        }
    }
}
