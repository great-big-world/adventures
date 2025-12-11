package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.server.level.ServerLevel;

public interface WorldAwareNoiseConfig {
    ServerLevel gbw$getWorld();

    void gbw$setWorld(ServerLevel serverWorld);
}