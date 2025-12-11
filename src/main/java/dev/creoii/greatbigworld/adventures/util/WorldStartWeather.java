package dev.creoii.greatbigworld.adventures.util;

import java.util.function.BiConsumer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.storage.PrimaryLevelData;

public enum WorldStartWeather {
    CLEAR((levelProperties, random) -> {}),
    RAIN((levelProperties, random) -> {
        levelProperties.setRaining(true);
        levelProperties.setRainTime(random.nextIntBetweenInclusive(9000, 180000));
    }),
    THUNDER((levelProperties, random) -> {
        int duration = random.nextIntBetweenInclusive(9000, 180000);
        levelProperties.setRaining(true);
        levelProperties.setRainTime(duration);
        levelProperties.setThundering(true);
        levelProperties.setThunderTime(duration);
    }),
    RANDOM((levelProperties, random) -> {
        switch (random.nextInt(3)) {
            case 0 -> RAIN.apply(levelProperties, random);
            case 1 -> THUNDER.apply(levelProperties, random);
        }
    });

    private final BiConsumer<PrimaryLevelData, RandomSource> weather;

    WorldStartWeather(BiConsumer<PrimaryLevelData, RandomSource> weather) {
        this.weather = weather;
    }

    public Component getTranslatableName() {
        return Component.translatable("weather." + name().toLowerCase());
    }

    public void apply(PrimaryLevelData levelProperties, RandomSource random) {
        weather.accept(levelProperties, random);
    }
}
