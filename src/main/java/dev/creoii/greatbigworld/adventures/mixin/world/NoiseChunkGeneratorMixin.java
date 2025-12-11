package dev.creoii.greatbigworld.adventures.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.util.WorldAwareNoiseConfig;
import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin extends ChunkGenerator {
    public NoiseChunkGeneratorMixin(BiomeSource biomeSource) {
        super(biomeSource);
    }

    @Inject(method = "applyCarvers", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseChunk;aquifer()Lnet/minecraft/world/level/levelgen/Aquifer;"), cancellable = true)
    private void gbw$limitCarvers(WorldGenRegion chunkRegion, long seed, RandomState noiseConfig, BiomeManager biomeAccess, StructureManager structureAccessor, ChunkAccess chunk, CallbackInfo ci) {
        if (this instanceof WorldSizeHolder worldSizeHolder) {
            if (WorldSizeHolder.isOutsideWorld(worldSizeHolder, chunk.getPos().x, chunk.getPos().z)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "doFill(Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;II)Lnet/minecraft/world/level/chunk/ChunkAccess;", at = @At("HEAD"), cancellable = true)
    private void gbw$limitWorldSize(Blender blender, StructureManager structureAccessor, RandomState noiseConfig, ChunkAccess chunk, int minimumCellY, int cellHeight, CallbackInfoReturnable<ChunkAccess> cir) {
        if (this instanceof WorldSizeHolder worldSizeHolder) {
            if (WorldSizeHolder.isOutsideWorld(worldSizeHolder, chunk.getPos().x, chunk.getPos().z)) {
                cir.setReturnValue(chunk);
            }
        }
    }

    @Inject(method = "buildSurface(Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;)V", at = @At("HEAD"), cancellable = true)
    private void gbw$limitBuildSurface(WorldGenRegion region, StructureManager structures, RandomState noiseConfig, ChunkAccess chunk, CallbackInfo ci) {
        if (this instanceof WorldSizeHolder worldSizeHolder) {
            if (WorldSizeHolder.isOutsideWorld(worldSizeHolder, chunk.getPos().x, chunk.getPos().z)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "doCreateBiomes(Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkAccess;fillBiomesFromNoise(Lnet/minecraft/world/level/biome/BiomeResolver;Lnet/minecraft/world/level/biome/Climate$Sampler;)V"), cancellable = true)
    private void gbw$limitBiomePopulation(Blender blender, RandomState noiseConfig, StructureManager structureAccessor, ChunkAccess chunk, CallbackInfo ci, @Local NoiseChunk chunkNoiseSampler, @Local BiomeResolver biomeSupplier) {
        if (this instanceof WorldSizeHolder worldSizeHolder) {
            if (WorldSizeHolder.isOutsideWorld(worldSizeHolder, chunk.getPos().x, chunk.getPos().z)) {
                if (noiseConfig != null && ((WorldAwareNoiseConfig) noiseConfig).gbw$getWorld() != null) {
                    chunk.fillBiomesFromNoise((x1, y, z1, noise) -> {
                        return ((WorldAwareNoiseConfig) noiseConfig).gbw$getWorld().registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.THE_VOID);
                    }, noiseConfig.sampler());
                    ci.cancel();
                }
            }
        }
    }
}
