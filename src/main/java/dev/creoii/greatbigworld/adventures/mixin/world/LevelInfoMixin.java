package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.util.BonusHouseHolder;
import dev.creoii.greatbigworld.adventures.util.ExtendedWorldCreator;
import dev.creoii.greatbigworld.adventures.util.WorldStartWeather;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelSettings.class)
public class LevelInfoMixin implements ExtendedWorldCreator, BonusHouseHolder {
    @Unique private long gbw$startTime;
    @Unique private int gbw$startSeason;
    @Unique private WorldStartWeather gbw$startWeather;
    @Unique private int gbw$worldSize;
    @Unique private boolean bonusHouseEnabled = false;

    @Override
    public void gbw$setStartTime(long startTime) {
        gbw$startTime = startTime;
    }

    @Override
    public void gbw$setStartSeason(int season) {
        gbw$startSeason = season;
    }

    @Override
    public void gbw$setStartWeather(WorldStartWeather startWeather) {
        gbw$startWeather = startWeather;
    }

    @Override
    public long gbw$getStartTime() {
        return gbw$startTime;
    }

    @Override
    public int gbw$getStartSeason() {
        return gbw$startSeason;
    }

    @Override
    public WorldStartWeather gbw$getStartWeather() {
        return gbw$startWeather;
    }

    @Override
    public void gbw$setWorldSize(int worldSize) {
        gbw$worldSize = worldSize;
    }

    @Override
    public int gbw$getWorldSize() {
        return gbw$worldSize;
    }

    @Override
    public void gbw$setBonusHouseEnabled(boolean bonusHouseEnabled) {
        this.bonusHouseEnabled = bonusHouseEnabled;
    }

    @Override
    public boolean gbw$isBonusHouseEnabled() {
        return bonusHouseEnabled;
    }

    @Inject(method = "withDifficulty", at = @At("RETURN"))
    private void gbw$fixWithDifficulty(Difficulty difficulty, CallbackInfoReturnable<LevelSettings> cir) {
        ExtendedWorldCreator extendedWorldCreator = (ExtendedWorldCreator) (Object) cir.getReturnValue();
        extendedWorldCreator.gbw$setWorldSize(gbw$worldSize);
        extendedWorldCreator.gbw$setStartTime(gbw$startTime);
        extendedWorldCreator.gbw$setStartWeather(gbw$startWeather);
        extendedWorldCreator.gbw$setStartSeason(gbw$startSeason);

        BonusHouseHolder bonusHouseHolder = (BonusHouseHolder) (Object) cir.getReturnValue();
        bonusHouseHolder.gbw$setBonusHouseEnabled(bonusHouseEnabled);
    }

    @Inject(method = "withGameType", at = @At("RETURN"))
    private void gbw$fixWithGameMode(GameType mode, CallbackInfoReturnable<LevelSettings> cir) {
        ExtendedWorldCreator extendedWorldCreator = (ExtendedWorldCreator) (Object) cir.getReturnValue();
        extendedWorldCreator.gbw$setWorldSize(gbw$worldSize);
        extendedWorldCreator.gbw$setStartTime(gbw$startTime);
        extendedWorldCreator.gbw$setStartWeather(gbw$startWeather);
        extendedWorldCreator.gbw$setStartSeason(gbw$startSeason);

        BonusHouseHolder bonusHouseHolder = (BonusHouseHolder) (Object) cir.getReturnValue();
        bonusHouseHolder.gbw$setBonusHouseEnabled(bonusHouseEnabled);
    }

    @Inject(method = "copy", at = @At("RETURN"))
    private void gbw$fixWithCopiedGameRules(CallbackInfoReturnable<LevelSettings> cir) {
        ExtendedWorldCreator extendedWorldCreator = (ExtendedWorldCreator) (Object) cir.getReturnValue();
        extendedWorldCreator.gbw$setWorldSize(gbw$worldSize);
        extendedWorldCreator.gbw$setStartTime(gbw$startTime);
        extendedWorldCreator.gbw$setStartWeather(gbw$startWeather);
        extendedWorldCreator.gbw$setStartSeason(gbw$startSeason);

        BonusHouseHolder bonusHouseHolder = (BonusHouseHolder) (Object) cir.getReturnValue();
        bonusHouseHolder.gbw$setBonusHouseEnabled(bonusHouseEnabled);
    }

    @Inject(method = "withDataConfiguration", at = @At("RETURN"))
    private void gbw$fixWithDataConfiguration(WorldDataConfiguration dataConfiguration, CallbackInfoReturnable<LevelSettings> cir) {
        ExtendedWorldCreator extendedWorldCreator = (ExtendedWorldCreator) (Object) cir.getReturnValue();
        extendedWorldCreator.gbw$setWorldSize(gbw$worldSize);
        extendedWorldCreator.gbw$setStartTime(gbw$startTime);
        extendedWorldCreator.gbw$setStartWeather(gbw$startWeather);
        extendedWorldCreator.gbw$setStartSeason(gbw$startSeason);

        BonusHouseHolder bonusHouseHolder = (BonusHouseHolder) (Object) cir.getReturnValue();
        bonusHouseHolder.gbw$setBonusHouseEnabled(bonusHouseEnabled);
    }
}
