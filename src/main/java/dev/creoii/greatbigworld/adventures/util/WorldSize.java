package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.network.chat.Component;

public enum WorldSize {
    ONE(1),
    TWO(2),
    FOUR(4),
    EIGHT(8),
    SIXTEEN(16),
    THIRTY_TWO(32),
    SIXTY_FOUR(64),
    ONE_TWENTY_EIGHT(128),
    TWO_FIFTY_SIX(256),
    FIVE_HUNDRED_TWELVE(512),
    ONE_THOUSAND_TWENTY_FOUR(1024),
    TWO_THOUSAND_FORTY_EIGHT(2048),
    FOUR_THOUSAND_NINETY_SIX(4096),
    EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO(8192),
    SIXTEEN_THOUSAND_THREE_HUNDRED_SIXTY_EIGHT(16368),
    THIRTY_TWO_THOUSAND_SEVEN_HUNDRED_THIRTY_SIX(32736),
    INFINITE(-1);

    private final int size;

    WorldSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public Component getTranslatableName(int size) {
        return this == INFINITE || size < 0 ? Component.translatable("worldSize.infinite") : Component.translatable("worldSize.tooltip", size, size);
    }
}
