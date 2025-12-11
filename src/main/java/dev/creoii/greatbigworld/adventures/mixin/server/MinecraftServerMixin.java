package dev.creoii.greatbigworld.adventures.mixin.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.util.BonusHouseHolder;
import dev.creoii.greatbigworld.adventures.util.ExtendedDedicatedServer;
import dev.creoii.greatbigworld.adventures.util.ExtendedLevelProperties;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.PlayerSpawnFinder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.LevelLoadListener;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Unique private static final ResourceKey<ConfiguredFeature<?, ?>> BONUS_HOUSE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "bonus_house"));
    @Unique private static final ResourceKey<ConfiguredFeature<?, ?>> BONUS_HOUSE_CHEST = ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "bonus_house_chest"));

    @Shadow public abstract Iterable<ServerLevel> getAllLevels();

    @Inject(method = "createLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/WorldData;isDebugWorld()Z"))
    private void gbw$applyWorldStartServerProperties(CallbackInfo ci, @Local ServerLevelData serverWorldProperties) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        if (serverWorldProperties instanceof ExtendedLevelProperties extendedLevelProperties) {
            if (server instanceof ExtendedDedicatedServer dedicatedServer)
                dedicatedServer.gbw$loadServerProperties(extendedLevelProperties);
        }
    }

    @Inject(method = "createLevels", at = @At("TAIL"))
    private void gbw$applyWorldStartProperties(CallbackInfo ci, @Local ServerLevelData serverWorldProperties) {
        if (serverWorldProperties instanceof ExtendedLevelProperties extendedLevelProperties) {
            SeasonManager seasonManager = SeasonManager.getInstance((MinecraftServer) (Object) this);
            Season season = Season.values()[extendedLevelProperties.gbw$getStartSeason()];
            if (season != Season.SUMMER) {
                for (ServerLevel world : getAllLevels()) {
                    if (world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
                        seasonManager.setCurrentSeason(world, season, true);
                    }
                }
            }

            int worldSize = extendedLevelProperties.gbw$getWorldSize();
            if (worldSize > 0) {
                for (ServerLevel world : getAllLevels()) {
                    world.getWorldBorder().setSize((worldSize * 2d * 16d) - .5d);
                }
            }
        }
    }

    @WrapOperation(method = "createLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setInitialSpawn(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/storage/ServerLevelData;ZZLnet/minecraft/server/level/progress/LevelLoadListener;)V"))
    private void gbw$redirectSetupSpawn(ServerLevel world, ServerLevelData worldProperties, boolean bonusChest, boolean debugWorld, LevelLoadListener chunkLoadProgress, Operation<Void> original, @Local WorldOptions generatorOptions) {
        if (generatorOptions instanceof BonusHouseHolder bonusHouseHolder) {
            setupSpawn(world, worldProperties, bonusChest, bonusHouseHolder.gbw$isBonusHouseEnabled(), debugWorld, chunkLoadProgress);
        } else original.call(world, worldProperties, bonusChest, debugWorld, chunkLoadProgress);
    }

    @Unique
    private static void setupSpawn(ServerLevel world, ServerLevelData worldProperties, boolean bonusChest, boolean bonusHouse, boolean debugWorld, LevelLoadListener loadProgress) {
        if (debugWorld) {
            worldProperties.setSpawn(new LevelData.RespawnData(GlobalPos.of(Level.OVERWORLD, BlockPos.ZERO.above(80)), 0f, 0f));
        } else {
            ServerChunkCache serverChunkManager = world.getChunkSource();
            ChunkPos chunkPos = new ChunkPos(serverChunkManager.randomState().sampler().findSpawnPosition());
            loadProgress.start(LevelLoadListener.Stage.PREPARE_GLOBAL_SPAWN, 0);
            loadProgress.updateFocus(world.dimension(), chunkPos);
            int i = serverChunkManager.getGenerator().getSpawnHeight(world);
            if (i < world.getMinY()) {
                BlockPos blockPos = chunkPos.getWorldPosition();
                i = world.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX() + 8, blockPos.getZ() + 8);
            }

            worldProperties.setSpawn(LevelData.RespawnData.of(world.dimension(), chunkPos.getWorldPosition().offset(8, i, 8), 0.0F, 0.0F));
            int j = 0;
            int k = 0;
            int l = 0;
            int m = -1;

            for(int n = 0; n < Mth.square(11); ++n) {
                if (j >= -5 && j <= 5 && k >= -5 && k <= 5) {
                    BlockPos blockPos2 = PlayerSpawnFinder.getSpawnPosInChunk(world, new ChunkPos(chunkPos.x + j, chunkPos.z + k));
                    if (blockPos2 != null) {
                        worldProperties.setSpawn(LevelData.RespawnData.of(world.dimension(), blockPos2, 0.0F, 0.0F));
                        break;
                    }
                }

                if (j == k || j < 0 && j == -k || j > 0 && j == 1 - k) {
                    int o = l;
                    l = -m;
                    m = o;
                }

                j += l;
                k += m;
            }

            if (bonusHouse && bonusChest) {
                world.registryAccess().lookup(Registries.CONFIGURED_FEATURE).flatMap((featureRegistry) -> featureRegistry.get(BONUS_HOUSE_CHEST)).ifPresent(feature -> feature.value().place(world, serverChunkManager.getGenerator(), world.random, worldProperties.getRespawnData().pos()));
            } else if (bonusChest) {
                world.registryAccess().lookup(Registries.CONFIGURED_FEATURE).flatMap((featureRegistry) -> featureRegistry.get(MiscOverworldFeatures.BONUS_CHEST)).ifPresent(feature -> feature.value().place(world, serverChunkManager.getGenerator(), world.random, worldProperties.getRespawnData().pos()));
            } else if (bonusHouse) {
                world.registryAccess().lookup(Registries.CONFIGURED_FEATURE).flatMap((featureRegistry) -> featureRegistry.get(BONUS_HOUSE)).ifPresent(feature -> feature.value().place(world, serverChunkManager.getGenerator(), world.random, worldProperties.getRespawnData().pos()));
            }

            loadProgress.finish(LevelLoadListener.Stage.PREPARE_GLOBAL_SPAWN);
        }
    }
}
