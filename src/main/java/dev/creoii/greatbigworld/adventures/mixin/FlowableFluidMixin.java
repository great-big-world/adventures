package dev.creoii.greatbigworld.adventures.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.util.ExtendedChunkGenerator;
import dev.creoii.greatbigworld.adventures.util.ExtendedLevelProperties;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FlowableFluid.class)
public class FlowableFluidMixin {
    @ModifyExpressionValue(method = "flowToSides", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FlowableFluid;canFlow(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;Lnet/minecraft/fluid/Fluid;)Z"))
    private boolean gbw$blockFluidFlowForWorldSize(boolean original, @Local(argsOnly = true) World world, @Local(ordinal = 1) BlockPos blockPos) {
        if (world.getLevelProperties() instanceof ExtendedLevelProperties extendedLevelProperties && extendedLevelProperties.gbw$getWorldSize() > 0) {
            int x = blockPos.getX() / 16;
            int z = blockPos.getZ() / 16;
            return ExtendedChunkGenerator.isWithinWorld(extendedLevelProperties, x, z);
        }
        return original;
    }
}
