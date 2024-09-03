package dev.creoii.greatbigworld.adventures.mixin.server;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.util.ExtendedDedicatedServer;
import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "createWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SaveProperties;isDebugWorld()Z"))
    private void gbw$applyWorldStartServerProperties(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci, @Local ServerWorldProperties serverWorldProperties) {
        if (serverWorldProperties instanceof WorldSizeHolder worldSizeHolder && ((MinecraftServer) (Object) this) instanceof ExtendedDedicatedServer dedicatedServer) {
            dedicatedServer.gbw$loadServerProperties(worldSizeHolder);
            int worldSize = worldSizeHolder.gbw$getWorldSize();
            if (worldSize > 0)
                serverWorldProperties.getWorldBorder().size = (worldSize * 2d * 16d) - .5d;
        }
    }
}
