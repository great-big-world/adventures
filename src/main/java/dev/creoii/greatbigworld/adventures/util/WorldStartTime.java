package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.text.Text;

public enum WorldStartTime {
    SUNRISE("sunrise", 23000L),
    MORNING("morning", 0L),
    DAY("day", 6000L),
    SUNSET("sunset", 12000L),
    NIGHT("night", 13000L),
    MIDNIGHT("midnight", 18000L);

    private final String name;
    private final long time;

    WorldStartTime(String name, long time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    public Text getTranslatableName() {
        return Text.translatable("time." + getName());
    }
}
