package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.server.world.ServerWorld;

public interface WorldAwareNoiseConfig {
    ServerWorld gbw$getWorld();

    void gbw$setWorld(ServerWorld serverWorld);
}