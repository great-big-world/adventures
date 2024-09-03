package dev.creoii.greatbigworld.adventures.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.SpringFeature;
import net.minecraft.world.gen.feature.SpringFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpringFeature.class)
public class SpringFeatureMixin {
    @Inject(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/StructureWorldAccess;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal = 3), cancellable = true)
    private void gbw$fixSpringsOnWorldEdge(FeatureContext<SpringFeatureConfig> context, CallbackInfoReturnable<Boolean> cir, @Local StructureWorldAccess structureWorldAccess, @Local BlockPos blockPos) {
        if (structureWorldAccess.getLevelProperties() instanceof WorldSizeHolder worldSizeHolder && worldSizeHolder.gbw$getWorldSize() > 0) {
            int x = blockPos.getX() / 16;
            int z = blockPos.getZ() / 16;
            int size = worldSizeHolder.gbw$getWorldSize();
            if (x >= size - 1 || x < -size + 1 || z >= size - 1 || z < -size + 1)
                cir.setReturnValue(false);
        }
    }
}
