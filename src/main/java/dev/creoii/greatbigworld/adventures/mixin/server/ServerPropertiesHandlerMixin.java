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
    @Unique private String gbw$startSeason;
    @Unique private int gbw$startSize;
    @Unique private long gbw$startTime;
    @Unique private String gbw$startWeather;

    public ServerPropertiesHandlerMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$parseWorldStartServerProperties(Properties properties, CallbackInfo ci) {
        gbw$startSeason = getString("start-season", "summer").toLowerCase();
        gbw$startSize = getInt("start-size", -1);
        gbw$startTime = parseLong("start-time", 0L);
        gbw$startWeather = getString("start-weather", "clear").toLowerCase();
    }

    @Override
    public String gbw$getStartSeason() {
        return gbw$startSeason;
    }

    @Override
    public int gbw$getStartSize() {
        return gbw$startSize;
    }

    @Override
    public long gbw$getStartTime() {
        return gbw$startTime;
    }

    @Override
    public String gbw$getStartWeather() {
        return gbw$startWeather;
    }
}
