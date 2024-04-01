package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.level.LevelProperties;

import java.util.function.BiConsumer;

public enum WorldStartWeather {
    CLEAR((levelProperties, random) -> {}),
    RAIN((levelProperties, random) -> {
        levelProperties.setRaining(true);
        levelProperties.setRainTime(random.nextBetween(9000, 180000));
    }),
    THUNDER((levelProperties, random) -> {
        int duration = random.nextBetween(9000, 180000);
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

    private final BiConsumer<LevelProperties, Random> weather;

    WorldStartWeather(BiConsumer<LevelProperties, Random> weather) {
        this.weather = weather;
    }

    public Text getTranslatableName() {
        return Text.translatable("weather." + name().toLowerCase());
    }

    public void apply(LevelProperties levelProperties, Random random) {
        weather.accept(levelProperties, random);
    }
}
