package dev.creoii.greatbigworld.adventures.mixin.server;

import dev.creoii.greatbigworld.adventures.util.ExtendedServerProperties;
import net.minecraft.server.dedicated.AbstractPropertiesHandler;
import net.minecraft.server.dedicated.ServerPropertiesHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Properties;

@Mixin(ServerPropertiesHandler.class)
public abstract class ServerPropertiesHandlerMixin extends AbstractPropertiesHandler<ServerPropertiesHandler> implements ExtendedServerProperties {
    @Unique private int gbw$startSize;

    public ServerPropertiesHandlerMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$parseWorldStartServerProperties(Properties properties, CallbackInfo ci) {
        gbw$startSize = getInt("start-size", -1);
    }

    @Override
    public int gbw$getStartSize() {
        return gbw$startSize;
    }
}
