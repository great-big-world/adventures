package dev.creoii.greatbigworld.adventures.util;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;

public enum DynamicDarknessQuality implements StringRepresentable {
    HIGH(3, "options.dynamicDarknessQuality.high"),
    NORMAL(2, "options.dynamicDarknessQuality.normal"),
    LOW(1, "options.dynamicDarknessQuality.low");

    public static final Codec<DynamicDarknessQuality> CODEC = StringRepresentable.fromEnum(DynamicDarknessQuality::values);
    public static final Component[] NAMES = Arrays.stream(values()).map(transitionQuality -> Component.translatable(transitionQuality.translationKey)).toArray(Component[]::new);
    private final int quality;
    private final String translationKey;

    DynamicDarknessQuality(int quality, String translationKey) {
        this.quality = quality;
        this.translationKey = translationKey;
    }

    public int getQuality() {
        return quality;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
