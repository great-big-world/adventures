package dev.creoii.greatbigworld.adventures.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Lifecycle;
import dev.creoii.greatbigworld.adventures.util.ExtendedLevelProperties;
import dev.creoii.greatbigworld.adventures.util.ExtendedWorldCreator;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
        if (saveProperties instanceof LevelProperties levelProperties) {
            final Random random = new LocalRandom(saveProperties.getGeneratorOptions().getSeed());
            ExtendedWorldCreator extendedWorldCreator = ((ExtendedWorldCreator) worldCreator);

            switch (extendedWorldCreator.gbw$getStartWeather()) {
                case RAIN -> setRain(levelProperties, random);
                case THUNDER -> setThunder(levelProperties, random);
                case RANDOM -> {
                    switch (random.nextInt(3)) {
                        case 0 -> setRain(levelProperties, random);
                        case 1 -> setThunder(levelProperties, random);
                    }
                }
            }

            levelProperties.setTimeOfDay(extendedWorldCreator.gbw$getStartTime());
            if (levelProperties instanceof ExtendedLevelProperties extendedLevelProperties) {
                int worldSize = extendedWorldCreator.gbw$getWorldSize();
                extendedLevelProperties.gbw$setWorldSize(worldSize);
                if (worldSize > 0)
                    levelProperties.getWorldBorder().size = worldSize * 2 * 16;
            }
        }
    }

    @Unique
    private void setRain(LevelProperties levelProperties, Random random) {
        levelProperties.setRaining(true);
        levelProperties.setRainTime(random.nextBetween(18000, 180000));
    }

    @Unique
    private void setThunder(LevelProperties levelProperties, Random random) {
        int duration = random.nextBetween(18000, 180000);
        levelProperties.setRaining(true);
        levelProperties.setRainTime(duration);
        levelProperties.setThundering(true);
        levelProperties.setThunderTime(duration);
    }
}
