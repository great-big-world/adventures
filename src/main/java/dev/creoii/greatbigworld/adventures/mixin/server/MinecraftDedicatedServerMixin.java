package dev.creoii.greatbigworld.adventures.mixin.server;

import dev.creoii.greatbigworld.adventures.util.ExtendedDedicatedServer;
import dev.creoii.greatbigworld.adventures.util.ExtendedLevelProperties;
import dev.creoii.greatbigworld.adventures.util.ExtendedServerProperties;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.ServerPropertiesHandler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftDedicatedServer.class)
public abstract class MinecraftDedicatedServerMixin implements ExtendedDedicatedServer {
    @Shadow public abstract ServerPropertiesHandler getProperties();
    @Shadow @Final static Logger LOGGER;

    @Override
    public void gbw$loadServerProperties(ExtendedLevelProperties extendedLevelProperties) {
        if (getProperties() instanceof ExtendedServerProperties extendedServerProperties) {
            int size = extendedServerProperties.gbw$getStartSize();
            if (size > 0) {
                LOGGER.info("Setting world size to {}", size);
                extendedLevelProperties.gbw$setWorldSize(size);
            }
        }
    }
}
