package dev.creoii.greatbigworld.adventures.mixin.world;

import com.mojang.datafixers.DataFixer;
import dev.creoii.greatbigworld.adventures.util.WorldAwareNoiseConfig;
import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerChunkLoadingManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.thread.ThreadExecutor;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.ChunkStatusChangeListener;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin(ServerChunkLoadingManager.class)
public abstract class ThreadedAnvilChunkStorageMixin {
    @Shadow protected abstract ChunkGenerator getChunkGenerator();
    @Shadow @Final private NoiseConfig noiseConfig;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$makeNoiseConfigAware(ServerWorld world, LevelStorage.Session session, DataFixer dataFixer, StructureTemplateManager structureTemplateManager, Executor executor, ThreadExecutor<Runnable> mainThreadExecutor, ChunkProvider chunkProvider, ChunkGenerator chunkGenerator, WorldGenerationProgressListener worldGenerationProgressListener, ChunkStatusChangeListener chunkStatusChangeListener, Supplier<PersistentStateManager> persistentStateManagerFactory, int viewDistance, boolean dsync, CallbackInfo ci) {
        ((WorldAwareNoiseConfig) noiseConfig).gbw$setWorld(world);
    }

    @Inject(method = "shouldTick", at = @At("HEAD"), cancellable = true)
    private void gbw$stopTickChunksOutOfWorld(ChunkPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (getChunkGenerator() instanceof WorldSizeHolder worldSizeHolder) {
            if (WorldSizeHolder.isOutsideWorld(worldSizeHolder, pos.x, pos.z)) {
                cir.setReturnValue(false);
            }
        }
    }
}
