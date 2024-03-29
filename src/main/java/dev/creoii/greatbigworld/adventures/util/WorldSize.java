package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.text.Text;

public enum WorldSize {
    TWO(2),
    FOUR(4),
    EIGHT(8),
    SIXTEEN(16),
    THIRTY_TWO(32),
    FORTY_EIGHT(48),
    SIXTY_FOUR(64),
    ONE_TWENTY_EIGHT(128),
    TWO_FIFTY_SIX(256),
    FIVE_HUNDRED_TWELVE(512),
    ONE_THOUSAND_TWENTY_EIGHT(1028),
    TWO_THOUSAND_FIFTY_SIX(2056),
    FOUR_THOUSAND_NINETY_TWO(4092),
    EIGHT_THOUSAND_ONE_HUNDRED_EIGHTY_FOUR(8184),
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

    public Text getTranslatableName() {
        return this == INFINITE ? Text.translatable("worldSize.infinite") : Text.translatable("worldSize." + getSize() + "x");
    }
}
