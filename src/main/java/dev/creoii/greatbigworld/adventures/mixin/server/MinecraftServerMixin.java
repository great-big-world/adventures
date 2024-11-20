package dev.creoii.greatbigworld.adventures.mixin.server;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.util.ExtendedDedicatedServer;
import dev.creoii.greatbigworld.adventures.util.ExtendedLevelProperties;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.level.ServerWorldProperties;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
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
            SeasonManager seasonManager = SeasonManager.getInstance(server);
            seasonManager.setCurrentSeason(getWorld(World.OVERWORLD), Season.values()[extendedLevelProperties.gbw$getStartSeason()], true);
        }
    }
}
