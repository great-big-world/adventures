package dev.creoii.greatbigworld.adventures.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.client.gui.OptionSliderWidget;
import dev.creoii.greatbigworld.adventures.util.ExtendedWorldCreator;
import dev.creoii.greatbigworld.adventures.util.WorldStartTime;
import dev.creoii.greatbigworld.adventures.util.WorldStartWeather;
import dev.creoii.greatbigworld.adventures.util.WorldSize;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(CreateWorldScreen.WorldTab.class)
public class WorldTabMixin {
    @Unique
    private static final Text START_WEATHER_TEXT = Text.translatable("selectWorld.startWeather");
    @Unique
    private static final Text START_TIME_TEXT = Text.translatable("selectWorld.startTime");
    @Unique
    private static final Text WORLD_SIZE_TEXT = Text.translatable("selectWorld.worldSize");

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$addNewWorldOptions(CreateWorldScreen createWorldScreen, CallbackInfo ci, @Local GridWidget.Adder adder) {
        adder.add(CyclingButtonWidget.builder(WorldStartWeather::getTranslatableName).values(WorldStartWeather.values()).build(0, 0, 150, 20, START_WEATHER_TEXT, (button, weather) -> {
            ((ExtendedWorldCreator) createWorldScreen.getWorldCreator()).gbw$setStartWeather(weather);
        }));
        adder.add(new OptionSliderWidget<WorldStartTime>(0, 0, 150, 20, WorldStartTime.MORNING, value -> {
            ((ExtendedWorldCreator) createWorldScreen.getWorldCreator()).gbw$setStartTime(value.getTime());
        }, WorldStartTime.values()) {
            @Override
            protected void updateMessage() {
                setMessage(MutableText.of(START_TIME_TEXT.getContent()).append(": ").append(tValue.getTranslatableName()));
            }
        });
        OptionSliderWidget<WorldSize> worldSizeWidget = createWorldSizeWidget(createWorldScreen);
        adder.add(worldSizeWidget);
    }

    @Unique
    @NotNull
    private static OptionSliderWidget<WorldSize> createWorldSizeWidget(CreateWorldScreen createWorldScreen) {
        OptionSliderWidget<WorldSize> worldSizeWidget = new OptionSliderWidget<>(0, 0, 150, 20, WorldSize.INFINITE, value -> {
            ((ExtendedWorldCreator) createWorldScreen.getWorldCreator()).gbw$setWorldSize(value.getSize() / 2);
        }, WorldSize.values()) {
            @Override
            protected void updateMessage() {
                setMessage(MutableText.of(WORLD_SIZE_TEXT.getContent()).append(": ").append(tValue.getTranslatableName()));
            }
        };
        worldSizeWidget.setTooltip(Tooltip.of(Text.translatable("selectWorld.worldSize.description")));
        return worldSizeWidget;
    }
}
