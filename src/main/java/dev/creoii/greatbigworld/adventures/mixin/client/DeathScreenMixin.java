package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {
    protected DeathScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V", ordinal = 2))
    private void gbw$renderDeathCoordinates(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (client.player != null && client.world != null && client.world.getGameRules().getBoolean(AdventuresGameRules.SHOW_COORDINATES_ON_DEATH)) {
            context.drawCenteredTextWithShadow(textRenderer, Text.literal(client.player.getBlockPos().toShortString()), width / 2, 85, 16777215);
        }
    }
}
