package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.util.WorldAwareNoiseConfig;
import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGeneratorStructureState.class)
public abstract class StructurePlacementCalculatorMixin {
    @Shadow public abstract RandomState randomState();

    @Inject(method = "hasStructureChunkInRange", at = @At("HEAD"), cancellable = true)
    private void gbw$preventStructureGenerationOutOfWorld(Holder<StructureSet> structureSetEntry, int centerChunkX, int centerChunkZ, int chunkCount, CallbackInfoReturnable<Boolean> cir) {
        if (WorldSizeHolder.isOutsideWorld((WorldSizeHolder) ((WorldAwareNoiseConfig) randomState()).gbw$getWorld().getChunkSource().getGenerator(), centerChunkX, centerChunkZ)) {
            cir.setReturnValue(false);
        }
    }
}
