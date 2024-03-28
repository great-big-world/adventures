package dev.creoii.greatbigworld.adventures.util;

public interface ExtendedWorldCreator extends ExtendedChunkGenerator {
    void gbw$setStartTime(long startTime);

    void gbw$setStartWeather(Weather startWeather);

    long gbw$getStartTime();

    Weather gbw$getStartWeather();
}
