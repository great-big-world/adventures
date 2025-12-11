package dev.creoii.greatbigworld.adventures.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SpringFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpringFeature.class)
public class SpringFeatureMixin {
    @Inject(method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 3), cancellable = true)
    private void gbw$fixSpringsOnWorldEdge(FeaturePlaceContext<SpringConfiguration> context, CallbackInfoReturnable<Boolean> cir, @Local WorldGenLevel structureWorldAccess, @Local BlockPos blockPos) {
        if (structureWorldAccess.getLevelData() instanceof WorldSizeHolder worldSizeHolder && worldSizeHolder.gbw$getWorldSize() > 0) {
            int x = blockPos.getX() / 16;
            int z = blockPos.getZ() / 16;
            int size = worldSizeHolder.gbw$getWorldSize();
            if (x >= size - 1 || x < -size + 1 || z >= size - 1 || z < -size + 1)
                cir.setReturnValue(false);
        }
    }
}
