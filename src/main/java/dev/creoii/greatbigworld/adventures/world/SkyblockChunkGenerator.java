package dev.creoii.greatbigworld.adventures.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.RandomSeed;
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SkyblockChunkGenerator extends ChunkGenerator {
    public static final MapCodec<SkyblockChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(RegistryOps.getEntryCodec(BiomeKeys.THE_VOID)).apply(instance, instance.stable(SkyblockChunkGenerator::new)));

    public SkyblockChunkGenerator(RegistryEntry.Reference<Biome> biomeEntry) {
        super(new FixedBiomeSource(biomeEntry));
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor) {
        ChunkPos chunkPos = chunk.getPos();
        if (SharedConstants.isOutsideGenerationArea(chunkPos))
            return;

        ChunkRandom chunkRandom = new ChunkRandom(new Xoroshiro128PlusPlusRandom(RandomSeed.getSeed()));
        ChunkSectionPos chunkSectionPos = ChunkSectionPos.from(chunkPos, world.getBottomSectionCoord());
        BlockPos blockPos = chunkSectionPos.getMinPos();

        world.getRegistryManager().getOptional(RegistryKeys.PLACED_FEATURE).ifPresent(features -> {
            PlacedFeature feature = features.get(RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(GreatBigWorld.NAMESPACE, "skyblock_oak")));
            if (feature != null)
                feature.generate(world, this, chunkRandom, blockPos);
        });

        world.getRegistryManager().getOptional(RegistryKeys.PLACED_FEATURE).ifPresent(features -> {
            PlacedFeature feature = features.get(RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(GreatBigWorld.NAMESPACE, "skyblock_chest")));
            if (feature != null)
                feature.generate(world, this, chunkRandom, blockPos);
        });

        BlockPos.Mutable posA = new BlockPos(0, 62, 0).mutableCopy();
        BlockPos.Mutable posB = new BlockPos(3, 62, 0).mutableCopy();
        BlockPos.Mutable posC = new BlockPos(0, 62, 3).mutableCopy();

        BlockPos.Mutable[] sections = new BlockPos.Mutable[]{posA, posB, posC};

        for (BlockPos.Mutable mutable : sections) {
            int baseX = mutable.getX();
            int baseY = mutable.getY();
            int baseZ = mutable.getZ();

            for (int z = -1; z <= 1; ++z) {
                for (int y = -1; y <= 1; ++y) {
                    for (int x = -1; x <= 1; ++x) {
                        BlockPos pos = new BlockPos(baseX + x, baseY + y, baseZ + z);
                        BlockState state = y == 1 ? Blocks.GRASS_BLOCK.getDefaultState() : Blocks.DIRT.getDefaultState();

                        world.setBlockState(pos, state, 2);
                    }
                }
            }
        }
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk) {
    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
    }

    @Override
    public void populateEntities(ChunkRegion region) {
    }

    @Override
    public int getWorldHeight() {
        return 384;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getSeaLevel() {
        return 63;
    }

    @Override
    public int getMinimumY() {
        return 0;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        return 0;
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        return new VerticalBlockSample(0, new BlockState[0]);
    }

    @Override
    public void appendDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {
    }
}
