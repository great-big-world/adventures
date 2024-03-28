package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.text.Text;

public enum Weather {
    CLEAR("clear"),
    RAIN("rain"),
    THUNDER("thunder"),
    RANDOM("random");

    private final String name;

    Weather(String name) {
        this.name = name;
    }

    public Text getTranslatableName() {
        return Text.translatable("weather." + name);
    }
}
