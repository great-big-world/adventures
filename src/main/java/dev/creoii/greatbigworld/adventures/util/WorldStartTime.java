package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.text.Text;

public enum WorldStartTime {
    SUNRISE(23000L),
    MORNING(0L),
    DAY(6000L),
    SUNSET(12000L),
    NIGHT(13000L),
    MIDNIGHT(18000L);

    private final long time;

    WorldStartTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public Text getTranslatableName() {
        return Text.translatable("time." + name().toLowerCase());
    }
}
