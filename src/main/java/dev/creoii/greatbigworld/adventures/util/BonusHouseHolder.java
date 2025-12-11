package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.world.level.levelgen.WorldOptions;
import org.jetbrains.annotations.Nullable;

public interface BonusHouseHolder {
    void gbw$setBonusHouseEnabled(boolean bonusHouseEnabled);

    boolean gbw$isBonusHouseEnabled();

    @Nullable
    default WorldOptions gbw$withBonusHouse(boolean bonusHouse) {
        return null;
    }
}
