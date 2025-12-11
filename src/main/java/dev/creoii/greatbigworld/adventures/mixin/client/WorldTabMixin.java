package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.client.gui.OptionSliderWidget;
import dev.creoii.greatbigworld.adventures.util.*;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
    @Unique private static final Component START_WEATHER_TEXT = Component.translatable("selectWorld.startWeather");
    @Unique private static final Component WORLD_SIZE_TOOLTIP_TEXT = Component.translatable("selectWorld.worldSize.description");

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$addNewWorldOptions(CreateWorldScreen createWorldScreen, CallbackInfo ci, @Local GridLayout.RowHelper adder) {
        adder.addChild(CycleButton.builder(WorldStartWeather::getTranslatableName, ((ExtendedWorldCreator) createWorldScreen.getUiState()).gbw$getStartWeather()).withValues(WorldStartWeather.values()).create(0, 0, 150, 20, START_WEATHER_TEXT, (button, weather) -> {
            ((ExtendedWorldCreator) createWorldScreen.getUiState()).gbw$setStartWeather(weather);
        }));
        adder.addChild(new OptionSliderWidget<>(0, 0, 150, 20, WorldStartTime.MORNING, true, value -> {
            ((ExtendedWorldCreator) createWorldScreen.getUiState()).gbw$setStartTime(value.getTime());
        }, Arrays.asList(WorldStartTime.values())) {
            @Override
            protected void updateMessage() {
                setMessage(Component.translatable("selectWorld.startTime", tValue.getTranslatableName()));
            }
        });
        OptionSliderWidget<WorldSize> worldSizeWidget = createWorldSizeWidget(createWorldScreen);
        adder.addChild(worldSizeWidget);
        adder.addChild(new OptionSliderWidget<>(0, 0, 150, 20, Season.SUMMER, true, value -> {
            ((ExtendedWorldCreator) createWorldScreen.getUiState()).gbw$setStartSeason(value.ordinal());
        }, Arrays.asList(Season.values())) {
            @Override
            protected void updateMessage() {
                setMessage(Component.translatable("selectWorld.startSeason", Component.translatable(tValue.getTranslationKey())));
            }
        });
    }

    @Unique
    @NotNull
    private static OptionSliderWidget<WorldSize> createWorldSizeWidget(CreateWorldScreen createWorldScreen) {
        return new OptionSliderWidget<>(0, 0, 150, 20, WorldSize.INFINITE, true, value -> {
            ((ExtendedWorldCreator) createWorldScreen.getUiState()).gbw$setWorldSize(value.getSize() / 2);
        }, Arrays.asList(WorldSize.values())) {
            @Override
            protected void updateMessage() {
                setMessage(Component.translatable("selectWorld.worldSize", tValue.getTranslatableName(tValue.getSize())));
                setTooltip(Tooltip.create(MutableComponent.create(WORLD_SIZE_TOOLTIP_TEXT.getContents()).append("\n").append(Component.translatable("selectWorld.worldSize.tooltip.sizeInBlocks", tValue.getTranslatableName(tValue.getSize() * 16)).withStyle(ChatFormatting.GRAY))));
            }
        };
    }
}
