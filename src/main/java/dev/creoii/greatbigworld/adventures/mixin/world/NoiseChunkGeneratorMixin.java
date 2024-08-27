package dev.creoii.greatbigworld.adventures.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.creoapi.impl.worldgen.util.WorldAwareNoiseConfig;
import dev.creoii.greatbigworld.adventures.util.ExtendedChunkGenerator;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.BiomeSupplier;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.*;
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
            if (ExtendedChunkGenerator.isWithinWorld(extendedChunkGenerator, chunk.getPos().x, chunk.getPos().z)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "populateNoise(Lnet/minecraft/world/gen/chunk/Blender;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/chunk/Chunk;II)Lnet/minecraft/world/chunk/Chunk;", at = @At("HEAD"), cancellable = true)
    private void gbw$limitWorldSize(Blender blender, StructureAccessor structureAccessor, NoiseConfig noiseConfig, Chunk chunk, int minimumCellY, int cellHeight, CallbackInfoReturnable<Chunk> cir) {
        if (this instanceof ExtendedChunkGenerator extendedChunkGenerator) {
            if (ExtendedChunkGenerator.isWithinWorld(extendedChunkGenerator, chunk.getPos().x, chunk.getPos().z)) {
                cir.setReturnValue(chunk);
            }
        }
    }

    @Inject(method = "buildSurface(Lnet/minecraft/world/ChunkRegion;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/chunk/Chunk;)V", at = @At("HEAD"), cancellable = true)
    private void gbw$limitBuildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk, CallbackInfo ci) {
        if (this instanceof ExtendedChunkGenerator extendedChunkGenerator) {
            if (ExtendedChunkGenerator.isWithinWorld(extendedChunkGenerator, chunk.getPos().x, chunk.getPos().z)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "populateBiomes(Lnet/minecraft/world/gen/chunk/Blender;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/chunk/Chunk;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;populateBiomes(Lnet/minecraft/world/biome/source/BiomeSupplier;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$MultiNoiseSampler;)V"), cancellable = true)
    private void gbw$limitBiomePopulation(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk, CallbackInfo ci, @Local ChunkNoiseSampler chunkNoiseSampler, @Local BiomeSupplier biomeSupplier) {
        if (this instanceof ExtendedChunkGenerator extendedChunkGenerator) {
            if (ExtendedChunkGenerator.isWithinWorld(extendedChunkGenerator, chunk.getPos().x, chunk.getPos().z)) {
                if (noiseConfig != null && ((WorldAwareNoiseConfig) noiseConfig).creo$getWorld() != null) {
                    chunk.populateBiomes((x1, y, z1, noise) -> {
                        return ((WorldAwareNoiseConfig) noiseConfig).creo$getWorld().getRegistryManager().get(RegistryKeys.BIOME).entryOf(BiomeKeys.THE_VOID);
                    }, noiseConfig.getMultiNoiseSampler());
                    ci.cancel();
                }
            }
        }
    }
}
