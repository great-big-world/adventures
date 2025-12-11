package dev.creoii.greatbigworld.adventures.mixin.server;

import dev.creoii.greatbigworld.adventures.util.ExtendedDedicatedServer;
import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DedicatedServer.class)
public abstract class MinecraftDedicatedServerMixin implements ExtendedDedicatedServer {
    @Shadow public abstract DedicatedServerProperties getProperties();
    @Shadow @Final static Logger LOGGER;

    @Override
    public void gbw$loadServerProperties(WorldSizeHolder worldSizeHolder) {
        if (getProperties() instanceof WorldSizeHolder worldSizeHolder1) {
            int size = worldSizeHolder1.gbw$getWorldSize();
            if (size > 0) {
                LOGGER.info("Setting world size to {}", size);
                worldSizeHolder.gbw$setWorldSize(size);
            }
        }
    }
}
