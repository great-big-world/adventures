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
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.network.SpawnLocating;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.MiscConfiguredFeatures;
import net.minecraft.world.level.ServerWorldProperties;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Unique private static final RegistryKey<ConfiguredFeature<?, ?>> BONUS_HOUSE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(GreatBigWorld.NAMESPACE, "bonus_house"));
    @Unique private static final RegistryKey<ConfiguredFeature<?, ?>> BONUS_HOUSE_CHEST = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(GreatBigWorld.NAMESPACE, "bonus_house_chest"));

    @Shadow @Nullable public abstract ServerWorld getWorld(RegistryKey<World> key);

    @Inject(method = "createWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SaveProperties;isDebugWorld()Z"))
    private void gbw$applyWorldStartServerProperties(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci, @Local ServerWorldProperties serverWorldProperties) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        if (serverWorldProperties instanceof ExtendedLevelProperties extendedLevelProperties) {
            if (server instanceof ExtendedDedicatedServer dedicatedServer)
                dedicatedServer.gbw$loadServerProperties(extendedLevelProperties);

            int worldSize = extendedLevelProperties.gbw$getWorldSize();
            if (worldSize > 0)
                serverWorldProperties.getWorldBorder().size = (worldSize * 2d * 16d) - .5d;
        }
    }

    @Inject(method = "createWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;initScoreboard(Lnet/minecraft/world/PersistentStateManager;)V"))
    private void gbw$applyWorldStartSeasonProperty(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci, @Local ServerWorldProperties serverWorldProperties) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        if (serverWorldProperties instanceof ExtendedLevelProperties extendedLevelProperties) {
            ServerWorld serverWorld = getWorld(GreatBigWorld.ALTERWORLD_KEY);
            if (serverWorld != null) {
                SeasonManager seasonManager = SeasonManager.getInstance(server);
                seasonManager.setCurrentSeason(serverWorld, Season.values()[extendedLevelProperties.gbw$getStartSeason()], true);
            }
        }
    }

    @WrapOperation(method = "createWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setupSpawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/level/ServerWorldProperties;ZZ)V"))
    private void gbw$redirectSetupSpawn(ServerWorld world, ServerWorldProperties worldProperties, boolean bonusChest, boolean debugWorld, Operation<Void> original, @Local GeneratorOptions generatorOptions) {
        if (generatorOptions instanceof BonusHouseHolder bonusHouseHolder) {
            setupSpawn(world, worldProperties, bonusChest, bonusHouseHolder.gbw$isBonusHouseEnabled(), debugWorld);
        } else original.call(world, worldProperties, bonusChest, debugWorld);
    }

    @Unique
    private static void setupSpawn(ServerWorld world, ServerWorldProperties worldProperties, boolean bonusChest, boolean bonusHouse, boolean debugWorld) {
        if (debugWorld) {
            worldProperties.setSpawnPos(BlockPos.ORIGIN.up(80), 0f);
        } else {
            ServerChunkManager serverChunkManager = world.getChunkManager();
            ChunkPos chunkPos = new ChunkPos(serverChunkManager.getNoiseConfig().getMultiNoiseSampler().findBestSpawnPosition());
            int i = serverChunkManager.getChunkGenerator().getSpawnHeight(world);
            if (i < world.getBottomY()) {
                BlockPos blockPos = chunkPos.getStartPos();
                i = world.getTopY(Heightmap.Type.WORLD_SURFACE, blockPos.getX() + 8, blockPos.getZ() + 8);
            }

            worldProperties.setSpawnPos(chunkPos.getStartPos().add(8, i, 8), 0f);
            int j = 0;
            int k = 0;
            int l = 0;
            int m = -1;

            for(int n = 0; n < MathHelper.square(11); ++n) {
                if (j >= -5 && j <= 5 && k >= -5 && k <= 5) {
                    BlockPos blockPos2 = SpawnLocating.findServerSpawnPoint(world, new ChunkPos(chunkPos.x + j, chunkPos.z + k));
                    if (blockPos2 != null) {
                        worldProperties.setSpawnPos(blockPos2, 0f);
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
                world.getRegistryManager().getOptional(RegistryKeys.CONFIGURED_FEATURE).flatMap((featureRegistry) -> featureRegistry.getOptional(BONUS_HOUSE_CHEST)).ifPresent(feature -> feature.value().generate(world, serverChunkManager.getChunkGenerator(), world.random, worldProperties.getSpawnPos()));
            } else if (bonusChest) {
                world.getRegistryManager().getOptional(RegistryKeys.CONFIGURED_FEATURE).flatMap((featureRegistry) -> featureRegistry.getOptional(MiscConfiguredFeatures.BONUS_CHEST)).ifPresent(feature -> feature.value().generate(world, serverChunkManager.getChunkGenerator(), world.random, worldProperties.getSpawnPos()));
            } else if (bonusHouse) {
                world.getRegistryManager().getOptional(RegistryKeys.CONFIGURED_FEATURE).flatMap((featureRegistry) -> featureRegistry.getOptional(BONUS_HOUSE)).ifPresent(feature -> feature.value().generate(world, serverChunkManager.getChunkGenerator(), world.random, worldProperties.getSpawnPos()));
            }
        }
    }
}
