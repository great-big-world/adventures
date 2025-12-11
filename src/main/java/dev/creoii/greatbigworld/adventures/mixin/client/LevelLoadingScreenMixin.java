package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawCenteredString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"))
    private void gbw$addDimensionToLoadingText(GuiGraphics instance, Font textRenderer, Component text, int centerX, int y, int color) {
        Identifier dimensionId = GreatBigWorldClient.getToDimension();
        if (dimensionId != null)
            instance.drawCenteredString(textRenderer, Component.translatable("multiplayer.downloadingTerrain.targeted", Component.translatable("dimension_type." + dimensionId.getNamespace() + "." + dimensionId.getPath())), centerX, y, color);
        else instance.drawCenteredString(textRenderer, text, centerX, y, color);
    }
}
