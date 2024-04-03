package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.util.ExtendedChunkGenerator;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin extends ChunkGenerator {
    public NoiseChunkGeneratorMixin(BiomeSource biomeSource) {
        super(biomeSource);
    }

    @Inject(method = "carve", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/chunk/ChunkNoiseSampler;getAquiferSampler()Lnet/minecraft/world/gen/chunk/AquiferSampler;"), cancellable = true)
    private void gbw$limitCarvers(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep, CallbackInfo ci) {
        if (this instanceof ExtendedChunkGenerator extendedChunkGenerator) {
            int x = chunk.getPos().x;
            int z = chunk.getPos().z;
            if (extendedChunkGenerator.gbw$getWorldSize() > 0 && (x >= extendedChunkGenerator.gbw$getWorldSize() || x < -extendedChunkGenerator.gbw$getWorldSize() || z >= extendedChunkGenerator.gbw$getWorldSize() || z < -extendedChunkGenerator.gbw$getWorldSize())) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "populateNoise(Lnet/minecraft/world/gen/chunk/Blender;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/chunk/Chunk;II)Lnet/minecraft/world/chunk/Chunk;", at = @At("HEAD"), cancellable = true)
    private void gbw$limitWorldSize(Blender blender, StructureAccessor structureAccessor, NoiseConfig noiseConfig, Chunk chunk, int minimumCellY, int cellHeight, CallbackInfoReturnable<Chunk> cir) {
        if (this instanceof ExtendedChunkGenerator extendedChunkGenerator) {
            int x = chunk.getPos().x;
            int z = chunk.getPos().z;
            if (extendedChunkGenerator.gbw$getWorldSize() > 0 && (x >= extendedChunkGenerator.gbw$getWorldSize() || x < -extendedChunkGenerator.gbw$getWorldSize() || z >= extendedChunkGenerator.gbw$getWorldSize() || z < -extendedChunkGenerator.gbw$getWorldSize())) {
                cir.setReturnValue(chunk);
            }
        }
    }

    @Inject(method = "buildSurface(Lnet/minecraft/world/ChunkRegion;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/chunk/Chunk;)V", at = @At("HEAD"), cancellable = true)
    private void gbw$limitBuildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk, CallbackInfo ci) {
        if (this instanceof ExtendedChunkGenerator extendedChunkGenerator) {
            int x = chunk.getPos().x;
            int z = chunk.getPos().z;
            if (extendedChunkGenerator.gbw$getWorldSize() > 0 && (x >= extendedChunkGenerator.gbw$getWorldSize() || x < -extendedChunkGenerator.gbw$getWorldSize() || z >= extendedChunkGenerator.gbw$getWorldSize() || z < -extendedChunkGenerator.gbw$getWorldSize())) {
                ci.cancel();
            }
        }
    }
}
