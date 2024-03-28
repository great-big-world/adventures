package dev.creoii.greatbigworld.adventures.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.util.ExtendedWorldCreator;
import dev.creoii.greatbigworld.adventures.util.Weather;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
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
        adder.add(CyclingButtonWidget.builder(Weather::getTranslatableName).values(Weather.values()).build(0, 0, 150, 20, START_WEATHER_TEXT, (button, weather) -> {
            ((ExtendedWorldCreator) createWorldScreen.getWorldCreator()).gbw$setStartWeather(weather);
        }));
        adder.add(new SliderWidget(0, 0, 150, 20, MutableText.of(START_TIME_TEXT.getContent()).append(": 0"), 0d) {
            @Override
            protected void updateMessage() {
                setMessage(MutableText.of(START_TIME_TEXT.getContent()).append(": " + (long) (value * 24000L)));
            }

            @Override
            protected void applyValue() {
                ((ExtendedWorldCreator) createWorldScreen.getWorldCreator()).gbw$setStartTime((long) (value * 24000L));
            }
        });
        adder.add(new SliderWidget(0, 0, 150, 20, MutableText.of(WORLD_SIZE_TEXT.getContent()).append(": Infinite"), 0d) {
            @Override
            protected void updateMessage() {
                if (value == 0d) {
                    setMessage(MutableText.of(WORLD_SIZE_TEXT.getContent()).append(": Infinite"));
                } else {
                    int worldSizeRadius = (int) (value * 4800);
                    setMessage(MutableText.of(WORLD_SIZE_TEXT.getContent()).append(": " + worldSizeRadius + " x " + worldSizeRadius));
                }
            }

            @Override
            protected void applyValue() {
                if (value == 0d) {
                    ((ExtendedWorldCreator) createWorldScreen.getWorldCreator()).gbw$setWorldSize(-1);
                } else {
                    ((ExtendedWorldCreator) createWorldScreen.getWorldCreator()).gbw$setWorldSize((int) ((value * 4800) / 2));
                }
            }
        });
    }
}
