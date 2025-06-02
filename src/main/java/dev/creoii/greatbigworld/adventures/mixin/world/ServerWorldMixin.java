package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.RandomSequencesState;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.SpecialSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setChunkGeneratorWorldSize(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> worldKey, DimensionOptions dimensionOptions, WorldGenerationProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List<SpecialSpawner> spawners, boolean shouldTickTime, RandomSequencesState randomSequencesState, CallbackInfo ci) {
        if (dimensionOptions.chunkGenerator() instanceof WorldSizeHolder worldSizeHolder && properties instanceof WorldSizeHolder worldSizeHolder1) {
            worldSizeHolder.gbw$setWorldSize(worldSizeHolder1.gbw$getWorldSize());
        }
    }

    @Redirect(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V"))
    private void gbw$sleepToNight(ServerWorld serverWorld, long timeOfDay){
        if (serverWorld.isDay()){
            long l = serverWorld.getLevelProperties().getTimeOfDay();
            serverWorld.setTimeOfDay(l + 13000L - l % 24000L);
        } else serverWorld.setTimeOfDay(timeOfDay);
    }
}
