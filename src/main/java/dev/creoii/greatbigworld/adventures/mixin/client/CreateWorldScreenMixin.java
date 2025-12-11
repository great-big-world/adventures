package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.adventures.util.ExtendedLevelProperties;
import dev.creoii.greatbigworld.adventures.util.ExtendedWorldCreator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.WorldData;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @Shadow @Final
    WorldCreationUiState uiState;

    @Inject(method = "createNewWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;createWorldOpenFlows()Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows;"))
    private void gbw$applyWorldOptions(LayeredRegistryAccess<RegistryLayer> combinedDynamicRegistries, WorldData saveProperties, CallbackInfoReturnable<Boolean> cir) {
        if (saveProperties instanceof PrimaryLevelData levelProperties && uiState instanceof ExtendedWorldCreator extendedWorldCreator) {
            extendedWorldCreator.gbw$getStartWeather().apply(levelProperties, new SingleThreadedRandomSource(levelProperties.worldGenOptions().seed()));
            levelProperties.setDayTime(extendedWorldCreator.gbw$getStartTime());

            if (levelProperties instanceof ExtendedLevelProperties extendedLevelProperties) {
                int worldSize = extendedWorldCreator.gbw$getWorldSize();
                extendedLevelProperties.gbw$setWorldSize(worldSize);
                if (worldSize > 0 && levelProperties.getLegacyWorldBorderSettings().isPresent())
                    levelProperties.getLegacyWorldBorderSettings().get().size = (worldSize * 2d * 16d) - .5d;
                else if (worldSize == 0 && levelProperties.getLegacyWorldBorderSettings().isPresent())
                    levelProperties.getLegacyWorldBorderSettings().get().size = 15.5d;

                extendedLevelProperties.gbw$setStartSeason(extendedWorldCreator.gbw$getStartSeason());
            }
        }
    }

    /*@Inject(method = "create(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/world/level/LevelInfo;Lnet/minecraft/client/world/GeneratorOptionsHolder;Ljava/nio/file/Path;)Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;", at = @At("RETURN"))
    private static void gbw$copyStartOptionsForWorldRecreation(MinecraftClient client, Screen parent, LevelInfo levelInfo, GeneratorOptionsHolder generatorOptionsHolder, Path dataPackTempDir, CallbackInfoReturnable<CreateWorldScreen> cir, @Local CreateWorldScreen createWorldScreen) {
        ExtendedWorldCreator extendedWorldCreator = (ExtendedWorldCreator) (Object) levelInfo;
        if (createWorldScreen.getWorldCreator() instanceof ExtendedWorldCreator extendedWorldCreator1) {
            System.out.println("2 start season: " + extendedWorldCreator.gbw$getStartSeason());
            System.out.println("2 start time: " + extendedWorldCreator.gbw$getStartTime());
            System.out.println("2 start weather: " + extendedWorldCreator.gbw$getStartWeather());
            System.out.println("2 world size: " + extendedWorldCreator.gbw$getWorldSize());
            extendedWorldCreator1.gbw$setStartSeason(extendedWorldCreator.gbw$getStartSeason());
            extendedWorldCreator1.gbw$setWorldSize(extendedWorldCreator.gbw$getWorldSize());
            extendedWorldCreator1.gbw$setStartTime(extendedWorldCreator.gbw$getStartTime());
            extendedWorldCreator1.gbw$setStartWeather(extendedWorldCreator.gbw$getStartWeather());
            System.out.println("3 start season: " + extendedWorldCreator1.gbw$getStartSeason());
            System.out.println("3 start time: " + extendedWorldCreator1.gbw$getStartTime());
            System.out.println("3 start weather: " + extendedWorldCreator1.gbw$getStartWeather());
            System.out.println("3 world size: " + extendedWorldCreator1.gbw$getWorldSize());
        }
    }

    @Inject(method = "createLevelInfo", at = @At(value = "RETURN", ordinal = 1))
    private void gbw$copyStartOptionsForWorldRecreation(boolean debugWorld, CallbackInfoReturnable<LevelInfo> cir) {
        System.out.println("createLevelInfo mixin");
        LevelInfo levelInfo = cir.getReturnValue();
        ExtendedWorldCreator extendedWorldCreator = (ExtendedWorldCreator) (Object) levelInfo;
        if (worldCreator instanceof ExtendedWorldCreator extendedWorldCreator1) {
            System.out.println("4 start season: " + extendedWorldCreator.gbw$getStartSeason());
            System.out.println("4 start time: " + extendedWorldCreator.gbw$getStartTime());
            System.out.println("4 start weather: " + extendedWorldCreator.gbw$getStartWeather());
            System.out.println("4 world size: " + extendedWorldCreator.gbw$getWorldSize());
            extendedWorldCreator1.gbw$setStartSeason(extendedWorldCreator.gbw$getStartSeason());
            extendedWorldCreator1.gbw$setWorldSize(extendedWorldCreator.gbw$getWorldSize());
            extendedWorldCreator1.gbw$setStartTime(extendedWorldCreator.gbw$getStartTime());
            extendedWorldCreator1.gbw$setStartWeather(extendedWorldCreator.gbw$getStartWeather());
            System.out.println("5 start season: " + extendedWorldCreator1.gbw$getStartSeason());
            System.out.println("5 start time: " + extendedWorldCreator1.gbw$getStartTime());
            System.out.println("5 start weather: " + extendedWorldCreator1.gbw$getStartWeather());
            System.out.println("5 world size: " + extendedWorldCreator1.gbw$getWorldSize());
        }
    }*/
}
