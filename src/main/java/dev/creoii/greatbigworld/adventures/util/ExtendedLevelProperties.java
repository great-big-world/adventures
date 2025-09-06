package dev.creoii.greatbigworld.adventures.util;

public interface ExtendedLevelProperties extends WorldSizeHolder, BonusHouseHolder {
    void gbw$setStartSeason(int season);

    int gbw$getStartSeason();
}
