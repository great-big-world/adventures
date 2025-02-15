package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.util.ExtendedWorldCreator;
import dev.creoii.greatbigworld.adventures.util.WorldStartWeather;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.level.LevelInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelInfo.class)
public class LevelInfoMixin implements ExtendedWorldCreator {
    @Unique private long gbw$startTime;
    @Unique private int gbw$startSeason;
    @Unique private WorldStartWeather gbw$startWeather;
    @Unique private int gbw$worldSize;

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

    @Inject(method = "withDifficulty", at = @At("RETURN"))
    private void gbw$fixWithDifficulty(Difficulty difficulty, CallbackInfoReturnable<LevelInfo> cir) {
        ExtendedWorldCreator extendedWorldCreator = (ExtendedWorldCreator) (Object) cir.getReturnValue();
        extendedWorldCreator.gbw$setWorldSize(gbw$worldSize);
        extendedWorldCreator.gbw$setStartTime(gbw$startTime);
        extendedWorldCreator.gbw$setStartWeather(gbw$startWeather);
        extendedWorldCreator.gbw$setStartSeason(gbw$startSeason);
    }

    @Inject(method = "withGameMode", at = @At("RETURN"))
    private void gbw$fixWithGameMode(GameMode mode, CallbackInfoReturnable<LevelInfo> cir) {
        ExtendedWorldCreator extendedWorldCreator = (ExtendedWorldCreator) (Object) cir.getReturnValue();
        extendedWorldCreator.gbw$setWorldSize(gbw$worldSize);
        extendedWorldCreator.gbw$setStartTime(gbw$startTime);
        extendedWorldCreator.gbw$setStartWeather(gbw$startWeather);
        extendedWorldCreator.gbw$setStartSeason(gbw$startSeason);
    }

    @Inject(method = "withCopiedGameRules", at = @At("RETURN"))
    private void gbw$fixWithCopiedGameRules(CallbackInfoReturnable<LevelInfo> cir) {
        ExtendedWorldCreator extendedWorldCreator = (ExtendedWorldCreator) (Object) cir.getReturnValue();
        extendedWorldCreator.gbw$setWorldSize(gbw$worldSize);
        extendedWorldCreator.gbw$setStartTime(gbw$startTime);
        extendedWorldCreator.gbw$setStartWeather(gbw$startWeather);
        extendedWorldCreator.gbw$setStartSeason(gbw$startSeason);
    }

    @Inject(method = "withDataConfiguration", at = @At("RETURN"))
    private void gbw$fixWithDataConfiguration(DataConfiguration dataConfiguration, CallbackInfoReturnable<LevelInfo> cir) {
        ExtendedWorldCreator extendedWorldCreator = (ExtendedWorldCreator) (Object) cir.getReturnValue();
        extendedWorldCreator.gbw$setWorldSize(gbw$worldSize);
        extendedWorldCreator.gbw$setStartTime(gbw$startTime);
        extendedWorldCreator.gbw$setStartWeather(gbw$startWeather);
        extendedWorldCreator.gbw$setStartSeason(gbw$startSeason);
    }
}
