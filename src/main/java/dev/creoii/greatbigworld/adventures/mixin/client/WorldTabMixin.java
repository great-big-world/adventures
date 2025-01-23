package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.client.gui.OptionSliderWidget;
import dev.creoii.greatbigworld.adventures.util.*;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
@Mixin(CreateWorldScreen.WorldTab.class)
public class WorldTabMixin {
    @Unique private static final Text START_WEATHER_TEXT = Text.translatable("selectWorld.startWeather");
    @Unique private static final Text WORLD_SIZE_TOOLTIP_TEXT = Text.translatable("selectWorld.worldSize.description");

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$addNewWorldOptions(CreateWorldScreen createWorldScreen, CallbackInfo ci, @Local GridWidget.Adder adder) {
        adder.add(CyclingButtonWidget.builder(WorldStartWeather::getTranslatableName).values(WorldStartWeather.values()).build(0, 0, 150, 20, START_WEATHER_TEXT, (button, weather) -> {
            ((ExtendedWorldCreator) createWorldScreen.getWorldCreator()).gbw$setStartWeather(weather);
        }));
        adder.add(new OptionSliderWidget<>(0, 0, 150, 20, WorldStartTime.MORNING, true, value -> {
            ((ExtendedWorldCreator) createWorldScreen.getWorldCreator()).gbw$setStartTime(value.getTime());
        }, Arrays.asList(WorldStartTime.values())) {
            @Override
            protected void updateMessage() {
                setMessage(Text.translatable("selectWorld.startTime", tValue.getTranslatableName()));
            }
        });
        OptionSliderWidget<WorldSize> worldSizeWidget = createWorldSizeWidget(createWorldScreen);
        adder.add(worldSizeWidget);
        adder.add(new OptionSliderWidget<>(0, 0, 150, 20, Season.SUMMER, true, value -> {
            ((ExtendedWorldCreator) createWorldScreen.getWorldCreator()).gbw$setStartSeason(value.ordinal());
        }, Arrays.asList(Season.values())) {
            @Override
            protected void updateMessage() {
                setMessage(Text.translatable("selectWorld.startSeason", Text.translatable(tValue.getTranslationKey())));
            }
        });
    }

    @Unique
    @NotNull
    private static OptionSliderWidget<WorldSize> createWorldSizeWidget(CreateWorldScreen createWorldScreen) {
        return new OptionSliderWidget<>(0, 0, 150, 20, WorldSize.INFINITE, true, value -> {
            ((ExtendedWorldCreator) createWorldScreen.getWorldCreator()).gbw$setWorldSize(value.getSize() / 2);
        }, Arrays.asList(WorldSize.values())) {
            @Override
            protected void updateMessage() {
                setMessage(Text.translatable("selectWorld.worldSize", tValue.getTranslatableName(tValue.getSize())));
                setTooltip(Tooltip.of(MutableText.of(WORLD_SIZE_TOOLTIP_TEXT.getContent()).append("\n").append(Text.translatable("selectWorld.worldSize.tooltip.sizeInBlocks", tValue.getTranslatableName(tValue.getSize() * 16)).formatted(Formatting.GRAY))));
            }
        };
    }
}
