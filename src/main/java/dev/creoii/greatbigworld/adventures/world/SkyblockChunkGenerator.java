package dev.creoii.greatbigworld.adventures.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SkyblockChunkGenerator extends ChunkGenerator {
    public static final MapCodec<SkyblockChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(RegistryOps.retrieveElement(Biomes.PLAINS)).apply(instance, instance.stable(SkyblockChunkGenerator::new)));

    public SkyblockChunkGenerator(Holder.Reference<Biome> biomeEntry) {
        super(new FixedBiomeSource(biomeEntry));
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel world, ChunkAccess chunk, StructureManager structureAccessor) {
        ChunkPos chunkPos = chunk.getPos();
        if (SharedConstants.debugVoidTerrain(chunkPos))
            return;

        WorldgenRandom chunkRandom = new WorldgenRandom(new XoroshiroRandomSource(RandomSupport.generateUniqueSeed()));
        BlockPos blockPos = SectionPos.of(chunkPos, world.getMinSectionY()).origin();

        world.registryAccess().lookup(Registries.PLACED_FEATURE).ifPresent(features -> {
            PlacedFeature tree = features.getValue(ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "skyblock_oak")));
            if (tree != null)
                tree.placeWithBiomeCheck(world, this, chunkRandom, blockPos);

            PlacedFeature chest = features.getValue(ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "skyblock_chest")));
            if (chest != null)
                chest.placeWithBiomeCheck(world, this, chunkRandom, blockPos);

            PlacedFeature island = features.getValue(ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "skyblock_island")));
            if (island != null)
                island.placeWithBiomeCheck(world, this, chunkRandom, blockPos);
        });
    }

    @Override
    public void applyCarvers(WorldGenRegion chunkRegion, long seed, RandomState noiseConfig, BiomeManager biomeAccess, StructureManager structureAccessor, ChunkAccess chunk) {
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structures, RandomState noiseConfig, ChunkAccess chunk) {
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
    }

    @Override
    public int getGenDepth() {
        return 384;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState noiseConfig, StructureManager structureAccessor, ChunkAccess chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getSeaLevel() {
        return 63;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightmap, LevelHeightAccessor world, RandomState noiseConfig) {
        return 0;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor world, RandomState noiseConfig) {
        return new NoiseColumn(0, new BlockState[0]);
    }

    @Override
    public void addDebugScreenInfo(List<String> text, RandomState noiseConfig, BlockPos pos) {
    }
}
