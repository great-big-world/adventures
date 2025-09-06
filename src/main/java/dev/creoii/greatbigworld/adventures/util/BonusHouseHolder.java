package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.world.gen.GeneratorOptions;
import org.jetbrains.annotations.Nullable;

public interface BonusHouseHolder {
    void gbw$setBonusHouseEnabled(boolean bonusHouseEnabled);

    boolean gbw$isBonusHouseEnabled();

    @Nullable
    default GeneratorOptions gbw$withBonusHouse(boolean bonusHouse) {
        return null;
    }
}
