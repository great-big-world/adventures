package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.world.LevelLoadingScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"))
    private void gbw$addDimensionToLoadingText(DrawContext instance, TextRenderer textRenderer, Text text, int centerX, int y, int color) {
        Identifier dimensionId = GreatBigWorldClient.getToDimension();
        if (dimensionId != null)
            instance.drawCenteredTextWithShadow(textRenderer, Text.translatable("multiplayer.downloadingTerrain.targeted", Text.translatable("dimension_type." + dimensionId.getNamespace() + "." + dimensionId.getPath())), centerX, y, color);
        else instance.drawCenteredTextWithShadow(textRenderer, text, centerX, y, color);
    }
}
