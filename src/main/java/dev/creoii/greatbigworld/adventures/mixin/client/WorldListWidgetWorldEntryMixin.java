package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.util.ExtendedWorldCreator;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldListWidget.WorldEntry.class)
public class WorldListWidgetWorldEntryMixin {
    @Shadow @Final LevelSummary level;

    /*@Inject(method = "recreate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/GeneratorOptionsHolder;initializeIndexedFeaturesLists()V"))
    private void gbw$copyStartOptionsForWorldRecreation(CallbackInfo ci, @Local LevelInfo levelInfo) {
        ExtendedWorldCreator extendedWorldCreator = (ExtendedWorldCreator) (Object) levelInfo;
        ExtendedWorldCreator extendedWorldCreator1 = (ExtendedWorldCreator) (Object) level.getLevelInfo();

        System.out.println("0 start season: " + extendedWorldCreator.gbw$getStartSeason());
        System.out.println("0 start time: " + extendedWorldCreator.gbw$getStartTime());
        System.out.println("0 start weather: " + extendedWorldCreator.gbw$getStartWeather());
        System.out.println("0 world size: " + extendedWorldCreator.gbw$getWorldSize());

        extendedWorldCreator1.gbw$setStartSeason(extendedWorldCreator.gbw$getStartSeason());
        extendedWorldCreator1.gbw$setWorldSize(extendedWorldCreator.gbw$getWorldSize());
        extendedWorldCreator1.gbw$setStartTime(extendedWorldCreator.gbw$getStartTime());
        extendedWorldCreator1.gbw$setStartWeather(extendedWorldCreator.gbw$getStartWeather());

        System.out.println("1 start season: " + extendedWorldCreator1.gbw$getStartSeason());
        System.out.println("1 start time: " + extendedWorldCreator1.gbw$getStartTime());
        System.out.println("1 start weather: " + extendedWorldCreator1.gbw$getStartWeather());
        System.out.println("1 world size: " + extendedWorldCreator1.gbw$getWorldSize());
    }*/
}
