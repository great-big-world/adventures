package dev.creoii.greatbigworld.adventures.util;

public interface ExtendedWorldCreator extends ExtendedChunkGenerator {
    void gbw$setStartTime(long startTime);

    void gbw$setStartSeason(int season);

    void gbw$setStartWeather(WorldStartWeather startWeather);

    long gbw$getStartTime();

    int gbw$getStartSeason();

    WorldStartWeather gbw$getStartWeather();
}
