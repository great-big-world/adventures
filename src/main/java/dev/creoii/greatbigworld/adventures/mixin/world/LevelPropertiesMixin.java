package dev.creoii.greatbigworld.adventures.mixin.world;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import dev.creoii.greatbigworld.adventures.util.ExtendedLevelProperties;
import dev.creoii.greatbigworld.adventures.util.WorldSize;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PrimaryLevelData.class)
public class LevelPropertiesMixin implements ExtendedLevelProperties {
    @Unique private int worldSize = WorldSize.INFINITE.getSize();
    @Unique private int startSeason = Season.SUMMER.ordinal();
    @Unique private boolean bonusHouse = false;

    @Override
    public void gbw$setWorldSize(int worldSize) {
        this.worldSize = worldSize;
    }

    @Override
    public void gbw$setStartSeason(int season) {
        startSeason = season;
    }

    @Override
    public void gbw$setBonusHouseEnabled(boolean bonusHouseEnabled) {
        bonusHouse = bonusHouseEnabled;
    }

    @Override
    public int gbw$getWorldSize() {
        return worldSize;
    }

    @Override
    public int gbw$getStartSeason() {
        return startSeason;
    }

    @Override
    public boolean gbw$isBonusHouseEnabled() {
        return bonusHouse;
    }

    @SuppressWarnings("deprecation")
    @Inject(method = "parse", at = @At("RETURN"))
    private static <T> void gbw$readExtendedProperties(Dynamic<T> dynamic, LevelSettings info, PrimaryLevelData.SpecialWorldProperty specialProperty, WorldOptions generatorOptions, Lifecycle lifecycle, CallbackInfoReturnable<PrimaryLevelData> cir) {
        ((ExtendedLevelProperties) cir.getReturnValue()).gbw$setWorldSize(dynamic.get("worldSize").asInt(WorldSize.INFINITE.getSize()));
        ((ExtendedLevelProperties) cir.getReturnValue()).gbw$setStartSeason(dynamic.get("startSeason").asInt(Season.SUMMER.ordinal()));
        ((ExtendedLevelProperties) cir.getReturnValue()).gbw$setBonusHouseEnabled(dynamic.get("bonusHouse").asBoolean(false));
    }

    @Inject(method = "setTagData", at = @At("TAIL"))
    private void gbw$updateExtendedProperties(RegistryAccess registryManager, CompoundTag levelNbt, CompoundTag playerNbt, CallbackInfo ci) {
        levelNbt.putInt("worldSize", worldSize);
        levelNbt.putInt("startSeason", startSeason);
        levelNbt.putBoolean("bonusHouse", bonusHouse);
    }
}
