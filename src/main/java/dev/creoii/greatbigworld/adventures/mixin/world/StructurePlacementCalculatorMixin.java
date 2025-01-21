package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.util.WorldAwareNoiseConfig;
import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureSet;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructurePlacementCalculator.class)
public abstract class StructurePlacementCalculatorMixin {
    @Shadow public abstract NoiseConfig getNoiseConfig();

    @Inject(method = "canGenerate", at = @At("HEAD"), cancellable = true)
    private void gbw$preventStructureGenerationOutOfWorld(RegistryEntry<StructureSet> structureSetEntry, int centerChunkX, int centerChunkZ, int chunkCount, CallbackInfoReturnable<Boolean> cir) {
        if (WorldSizeHolder.isOutsideWorld((WorldSizeHolder) ((WorldAwareNoiseConfig) getNoiseConfig()).gbw$getWorld().getChunkManager().getChunkGenerator(), centerChunkX, centerChunkZ)) {
            cir.setReturnValue(false);
        }
    }
}
