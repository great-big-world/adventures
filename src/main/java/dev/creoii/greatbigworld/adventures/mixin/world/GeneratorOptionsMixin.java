package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.util.BonusHouseHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.OptionalLong;
import net.minecraft.world.level.levelgen.WorldOptions;

@Mixin(WorldOptions.class)
public class GeneratorOptionsMixin implements BonusHouseHolder {
    @Shadow @Final private long seed;
    @Shadow @Final private boolean generateStructures;
    @Shadow @Final private boolean generateBonusChest;
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

    @Inject(method = "withBonusChest", at = @At("RETURN"))
    private void gbw$fixWithBonusChest(boolean bonusChest, CallbackInfoReturnable<WorldOptions> cir) {
        ((BonusHouseHolder) cir.getReturnValue()).gbw$setBonusHouseEnabled(bonusHouseEnabled);
    }

    @Inject(method = "withSeed", at = @At("RETURN"))
    private void gbw$fixWithSeed(OptionalLong seed, CallbackInfoReturnable<WorldOptions> cir) {
        ((BonusHouseHolder) cir.getReturnValue()).gbw$setBonusHouseEnabled(bonusHouseEnabled);
    }

    @Inject(method = "withStructures", at = @At("RETURN"))
    private void gbw$fixWithStructures(boolean structures, CallbackInfoReturnable<WorldOptions> cir) {
        ((BonusHouseHolder) cir.getReturnValue()).gbw$setBonusHouseEnabled(bonusHouseEnabled);
    }

    @Override
    public @Nullable WorldOptions gbw$withBonusHouse(boolean bonusHouse) {
        WorldOptions generatorOptions = new WorldOptions(seed, generateStructures, generateBonusChest, legacyCustomOptions);
        ((BonusHouseHolder) generatorOptions).gbw$setBonusHouseEnabled(bonusHouse);
        return generatorOptions;
    }
}
