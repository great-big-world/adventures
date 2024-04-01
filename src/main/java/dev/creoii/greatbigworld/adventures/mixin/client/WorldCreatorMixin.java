package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.adventures.util.ExtendedWorldCreator;
import dev.creoii.greatbigworld.adventures.util.WorldStartWeather;
import net.minecraft.client.gui.screen.world.WorldCreator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WorldCreator.class)
public class WorldCreatorMixin implements ExtendedWorldCreator {
    @Unique private int worldSize = -1;
    @Unique private long startTime = 0L;
    @Unique private WorldStartWeather startWeather = WorldStartWeather.CLEAR;

    @Override
    public void gbw$setWorldSize(int worldSize) {
        this.worldSize = worldSize;
    }

    @Override
    public void gbw$setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void gbw$setStartWeather(WorldStartWeather startWeather) {
        this.startWeather = startWeather;
    }

    @Override
    public int gbw$getWorldSize() {
        return worldSize;
    }

    @Override
    public long gbw$getStartTime() {
        return startTime;
    }

    @Override
    public WorldStartWeather gbw$getStartWeather() {
        return startWeather;
    }
}
