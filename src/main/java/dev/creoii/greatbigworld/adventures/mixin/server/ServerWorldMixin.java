package dev.creoii.greatbigworld.adventures.mixin.server;

import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin extends Level {
    protected ServerWorldMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setChunkGeneratorWorldSize(MinecraftServer server, Executor workerExecutor, LevelStorageSource.LevelStorageAccess session, ServerLevelData properties, ResourceKey<Level> worldKey, LevelStem dimensionOptions, boolean debugWorld, long seed, List<CustomSpawner> spawners, boolean shouldTickTime, RandomSequences randomSequenceState, CallbackInfo ci) {
        if (dimensionOptions.generator() instanceof WorldSizeHolder worldSizeHolder && properties instanceof WorldSizeHolder worldSizeHolder1) {
            worldSizeHolder.gbw$setWorldSize(worldSizeHolder1.gbw$getWorldSize());
        }
    }

    @Redirect(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setDayTime(J)V"))
    private void gbw$sleepToNight(ServerLevel serverWorld, long timeOfDay){
        if (serverWorld.isBrightOutside()) {
            serverWorld.setDayTime(12000L + 1000L);
        } else serverWorld.setDayTime(timeOfDay);
    }
}
