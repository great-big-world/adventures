package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {
    @Shadow @Final private Text message;

    protected DeathScreenMixin(Text title) {
        super(title);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V", ordinal = 1))
    private void gbw$renderDeathCoordinatesMessage(DrawContext instance, TextRenderer textRenderer, Text text, int centerX, int y, int color) {
        if (client.player != null && client.world != null && client.world.getGameRules().getBoolean(AdventuresGameRules.SHOW_COORDINATES_ON_DEATH)) {
            instance.drawCenteredTextWithShadow(textRenderer, Texts.join(List.of(text, Text.translatable("death.showCoordinates", client.player.getBlockPos().toShortString())), Text.literal(" ")), centerX, y, color);
        } else instance.drawCenteredTextWithShadow(textRenderer, text, centerX, y, color);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V", ordinal = 2))
    private void gbw$renderDeathCoordinates(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (client.player != null && client.world != null && message == null && client.world.getGameRules().getBoolean(AdventuresGameRules.SHOW_COORDINATES_ON_DEATH)) {
            context.drawCenteredTextWithShadow(textRenderer, Text.literal(client.player.getBlockPos().toShortString()), width / 2, 85, 16777215);
        }
    }
}
