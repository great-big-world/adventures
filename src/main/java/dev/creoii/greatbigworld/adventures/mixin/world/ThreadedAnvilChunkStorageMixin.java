package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.util.ExtendedChunkGenerator;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin {
    @Shadow protected abstract ChunkGenerator getChunkGenerator();

    @Inject(method = "shouldTick", at = @At("HEAD"), cancellable = true)
    private void gbw$stopTickChunksOutOfWorld(ChunkPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (getChunkGenerator() instanceof ExtendedChunkGenerator extendedChunkGenerator) {
            if (ExtendedChunkGenerator.isWithinWorld(extendedChunkGenerator, pos.x, pos.z)) {
                cir.setReturnValue(false);
            }
        }
    }
}
