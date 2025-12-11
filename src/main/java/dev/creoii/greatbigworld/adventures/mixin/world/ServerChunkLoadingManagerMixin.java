package dev.creoii.greatbigworld.adventures.mixin.world;

import com.mojang.datafixers.DataFixer;
import dev.creoii.greatbigworld.adventures.util.WorldAwareNoiseConfig;
import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.Executor;
import java.util.function.Supplier;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.TicketStorage;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.LevelStorageSource;

@Mixin(ChunkMap.class)
public abstract class ServerChunkLoadingManagerMixin {
    @Shadow protected abstract ChunkGenerator generator();
    @Shadow @Final private RandomState randomState;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$makeNoiseConfigAware(ServerLevel world, LevelStorageSource.LevelStorageAccess session, DataFixer dataFixer, StructureTemplateManager structureTemplateManager, Executor executor, BlockableEventLoop mainThreadExecutor, LightChunkGetter chunkProvider, ChunkGenerator chunkGenerator, ChunkStatusUpdateListener chunkStatusChangeListener, Supplier persistentStateManagerFactory, TicketStorage ticketManager, int viewDistance, boolean dsync, CallbackInfo ci) {
        ((WorldAwareNoiseConfig) randomState).gbw$setWorld(world);
    }

    @Inject(method = "anyPlayerCloseEnoughForSpawning", at = @At("HEAD"), cancellable = true)
    private void gbw$stopTickChunksOutOfWorld(ChunkPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (generator() instanceof WorldSizeHolder worldSizeHolder) {
            if (WorldSizeHolder.isOutsideWorld(worldSizeHolder, pos.x, pos.z)) {
                cir.setReturnValue(false);
            }
        }
    }
}
