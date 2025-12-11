package dev.creoii.greatbigworld.adventures.mixin.server;

import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Properties;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.server.dedicated.Settings;

@Mixin(DedicatedServerProperties.class)
public abstract class ServerPropertiesHandlerMixin extends Settings<DedicatedServerProperties> implements WorldSizeHolder {
    @Unique private int gbw$worldSize;

    public ServerPropertiesHandlerMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$parseWorldStartServerProperties(Properties properties, CallbackInfo ci) {
        gbw$worldSize = get("world-size", -1);
    }

    @Override
    public int gbw$getWorldSize() {
        return gbw$worldSize;
    }

    @Override
    public void gbw$setWorldSize(int worldSize) {
        gbw$worldSize = worldSize;
    }
}
