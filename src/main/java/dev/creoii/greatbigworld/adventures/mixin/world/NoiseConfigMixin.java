package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.util.WorldAwareNoiseConfig;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NoiseConfig.class)
public class NoiseConfigMixin implements WorldAwareNoiseConfig {
    @Unique
    private ServerWorld gbw$serverWorld;

    @Override
    public ServerWorld gbw$getWorld() {
        return gbw$serverWorld;
    }

    @Override
    public void gbw$setWorld(ServerWorld serverWorld) {
        gbw$serverWorld = serverWorld;
    }
}