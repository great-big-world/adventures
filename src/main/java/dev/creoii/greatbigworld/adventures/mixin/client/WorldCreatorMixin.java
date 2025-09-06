package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.adventures.util.BonusHouseHolder;
import dev.creoii.greatbigworld.adventures.util.ExtendedWorldCreator;
import dev.creoii.greatbigworld.adventures.util.WorldSize;
import dev.creoii.greatbigworld.adventures.util.WorldStartWeather;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.world.GeneratorOptionsHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldCreator.class)
public abstract class WorldCreatorMixin implements ExtendedWorldCreator, BonusHouseHolder {
    @Shadow public abstract void update();

    @Shadow private GeneratorOptionsHolder generatorOptionsHolder;
    @Unique private int worldSize = WorldSize.INFINITE.getSize();
    @Unique private long startTime = 0L;
    @Unique private int startSeason = Season.SUMMER.ordinal();
    @Unique private WorldStartWeather startWeather = WorldStartWeather.CLEAR;
    @Unique private boolean bonusHouseEnabled = false;

    @Inject(method = "update", at = @At("HEAD"))
    private void gbw$updateForBonusHouse(CallbackInfo ci) {
        if ((Object) generatorOptionsHolder instanceof BonusHouseHolder bonusHouseHolder) {
            if (bonusHouseEnabled != bonusHouseHolder.gbw$isBonusHouseEnabled()) {
                generatorOptionsHolder = generatorOptionsHolder.apply(options -> ((BonusHouseHolder) options).gbw$withBonusHouse(bonusHouseEnabled));
            }
        }
    }

    @Override
    public void gbw$setWorldSize(int worldSize) {
        this.worldSize = worldSize;
        update();
    }

    @Override
    public void gbw$setStartTime(long startTime) {
        this.startTime = startTime;
        update();
    }

    @Override
    public void gbw$setStartSeason(int startSeason) {
        this.startSeason = startSeason;
        update();
    }

    @Override
    public void gbw$setStartWeather(WorldStartWeather startWeather) {
        this.startWeather = startWeather;
        update();
    }

    @Override
    public void gbw$setBonusHouseEnabled(boolean bonusHouseEnabled) {
        this.bonusHouseEnabled = bonusHouseEnabled;
        update();
    }

    @Override
    public int gbw$getWorldSize() {
        return worldSize;
    }

    @Override
    public long gbw$getStartTime() {
        return startTime;
    }

    @Override
    public int gbw$getStartSeason() {
        return startSeason;
    }

    @Override
    public WorldStartWeather gbw$getStartWeather() {
        return startWeather;
    }

    @Override
    public boolean gbw$isBonusHouseEnabled() {
        return bonusHouseEnabled;
    }
}
