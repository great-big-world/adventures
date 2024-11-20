package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Lifecycle;
import dev.creoii.greatbigworld.adventures.util.ExtendedLevelProperties;
import dev.creoii.greatbigworld.adventures.util.ExtendedWorldCreator;
import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @Shadow @Final
    WorldCreator worldCreator;

    @SuppressWarnings("deprecation")
    @Inject(method = "startServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;createIntegratedServerLoader()Lnet/minecraft/server/integrated/IntegratedServerLoader;"))
    private void gbw$applyWorldOptions(LevelProperties.SpecialProperty specialProperty, CombinedDynamicRegistries<ServerDynamicRegistryType> combinedDynamicRegistries, Lifecycle lifecycle, CallbackInfo ci, @Local SaveProperties saveProperties) {
        if (saveProperties instanceof LevelProperties levelProperties && worldCreator instanceof ExtendedWorldCreator extendedWorldCreator) {
            extendedWorldCreator.gbw$getStartWeather().apply(levelProperties, new LocalRandom(levelProperties.getGeneratorOptions().getSeed()));
            levelProperties.setTimeOfDay(extendedWorldCreator.gbw$getStartTime());

            if (levelProperties instanceof ExtendedLevelProperties extendedLevelProperties) {
                int worldSize = extendedWorldCreator.gbw$getWorldSize();
                extendedLevelProperties.gbw$setWorldSize(worldSize);
                if (worldSize > 0)
                    levelProperties.getWorldBorder().size = (worldSize * 2d * 16d) - .5d;

                extendedLevelProperties.gbw$setStartSeason(extendedWorldCreator.gbw$getStartSeason());
            }
        }
    }
}
