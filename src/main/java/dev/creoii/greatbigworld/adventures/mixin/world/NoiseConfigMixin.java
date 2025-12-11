package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.util.WorldAwareNoiseConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.RandomState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RandomState.class)
public class NoiseConfigMixin implements WorldAwareNoiseConfig {
    @Unique
    private ServerLevel gbw$serverWorld;

    @Override
    public ServerLevel gbw$getWorld() {
        return gbw$serverWorld;
    }

    @Override
    public void gbw$setWorld(ServerLevel serverWorld) {
        gbw$serverWorld = serverWorld;
    }
}