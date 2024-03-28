package dev.creoii.greatbigworld.adventures.util;

public interface ExtendedWorldCreator extends ExtendedChunkGenerator {
    void gbw$setStartTime(long startTime);

    void gbw$setStartWeather(WorldStartWeather startWeather);

    long gbw$getStartTime();

    WorldStartWeather gbw$getStartWeather();
}
