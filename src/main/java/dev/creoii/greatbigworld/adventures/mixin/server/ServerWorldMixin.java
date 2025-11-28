package dev.creoii.greatbigworld.adventures.mixin.server;

import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.RandomSequencesState;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.SpecialSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    @Shadow
    public abstract GameRules getGameRules();

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setChunkGeneratorWorldSize(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> worldKey, DimensionOptions dimensionOptions, boolean debugWorld, long seed, List<SpecialSpawner> spawners, boolean shouldTickTime, RandomSequencesState randomSequenceState, CallbackInfo ci) {
        if (dimensionOptions.chunkGenerator() instanceof WorldSizeHolder worldSizeHolder && properties instanceof WorldSizeHolder worldSizeHolder1) {
            worldSizeHolder.gbw$setWorldSize(worldSizeHolder1.gbw$getWorldSize());
        }
    }

    @Redirect(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V"))
    private void gbw$sleepToNight(ServerWorld serverWorld, long timeOfDay){
        if (serverWorld.isDay()) {
            serverWorld.setTimeOfDay(getGameRules().getInt(AdventuresGameRules.LENGTH_OF_DAY) + 1000L);
        } else serverWorld.setTimeOfDay(timeOfDay);
    }

    @ModifyConstant(method = "tick", constant = @Constant(longValue = 24000L))
    private long gbw$modifyTotalDayLength(long constant) {
        GameRules gameRules = getGameRules();
        return gameRules.getInt(AdventuresGameRules.LENGTH_OF_DAY) + gameRules.getInt(AdventuresGameRules.LENGTH_OF_NIGHT);
    }

    @Override
    public boolean isNightAndNatural() {
        GameRules gameRules = getGameRules();
        long total = gameRules.getInt(AdventuresGameRules.LENGTH_OF_DAY) + gameRules.getInt(AdventuresGameRules.LENGTH_OF_NIGHT);
        if (!getDimension().natural()) {
            return false;
        } else {
            int i = (int)(getTimeOfDay() % total);
            return i >= gameRules.getInt(AdventuresGameRules.LENGTH_OF_DAY) + 600 && i <= total - 600;
        }
    }
}
