package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.text.Text;

public enum WorldSize {
    SIXTEEN(16),
    THIRTY_TWO(32),
    SIXTY_FOUR(64),
    ONE_TWENTY_EIGHT(128),
    TWO_FIFTY_SIX(256),
    FIVE_HUNDRED_TWELVE(512),
    ONE_THOUSAND_TWENTY_EIGHT(1028),
    TWO_THOUSAND_FIFTY_SIX(2056),
    FOUR_THOUSAND_NINETY_TWO(4092),
    SIXTEEN_THOUSAND_THREE_HUNDRED_SIXTY_EIGHT(16368),
    INFINITE(-1);

    private final int size;

    WorldSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public Text getTranslatableName() {
        if (this == INFINITE) {
            return Text.translatable("worldSize.infinite");
        }
        return Text.translatable("worldSize." + getSize() + "x");
    }
}
