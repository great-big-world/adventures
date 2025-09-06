package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.util.BonusHouseHolder;
import net.minecraft.world.gen.GeneratorOptions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(GeneratorOptions.class)
public class GeneratorOptionsMixin implements BonusHouseHolder {
    @Shadow @Final private long seed;
    @Shadow @Final private boolean generateStructures;
    @Shadow @Final private boolean bonusChest;
    @Shadow @Final private Optional<String> legacyCustomOptions;
    @Unique
    private boolean bonusHouseEnabled = false;

    @Override
    public void gbw$setBonusHouseEnabled(boolean bonusHouseEnabled) {
        this.bonusHouseEnabled = bonusHouseEnabled;
    }

    @Override
    public boolean gbw$isBonusHouseEnabled() {
        return bonusHouseEnabled;
    }

    @Override
    public @Nullable GeneratorOptions gbw$withBonusHouse(boolean bonusHouse) {
        GeneratorOptions generatorOptions = new GeneratorOptions(seed, generateStructures, bonusChest, legacyCustomOptions);
        ((BonusHouseHolder) generatorOptions).gbw$setBonusHouseEnabled(bonusHouse);
        return generatorOptions;
    }
}
