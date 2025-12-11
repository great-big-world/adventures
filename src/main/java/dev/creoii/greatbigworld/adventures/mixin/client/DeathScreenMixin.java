package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.adventures.util.ShowDeathCoordinates;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.TextAlignment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {
    @Shadow @Final private Component causeOfDeath;

    protected DeathScreenMixin(Component title) {
        super(title);
    }

    @Redirect(method = "visitText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ActiveTextCollector;accept(Lnet/minecraft/client/gui/TextAlignment;IILnet/minecraft/network/chat/Component;)V", ordinal = 1))
    private void gbw$renderDeathCoordinatesMessage(ActiveTextCollector instance, TextAlignment textAlignment, int i, int j, Component component) {
        if (minecraft.player != null && minecraft.level != null && ((ShowDeathCoordinates) minecraft.level).gbw$shouldShowDeathCoordinates()) {
            instance.accept(textAlignment, i, j, ComponentUtils.formatList(List.of(component, Component.translatable("death.showCoordinates", minecraft.player.blockPosition().toShortString())), Component.literal(" ")));
        } else instance.accept(textAlignment, i, j, component);
    }

    @Redirect(method = "visitText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ActiveTextCollector;accept(Lnet/minecraft/client/gui/TextAlignment;IILnet/minecraft/network/chat/Component;)V", ordinal = 2))
    private void gbw$renderDeathCoordinates(ActiveTextCollector instance, TextAlignment textAlignment, int i, int j, Component component) {
        if (minecraft.player != null && minecraft.level != null && causeOfDeath == null && ((ShowDeathCoordinates) minecraft.level).gbw$shouldShowDeathCoordinates()) {
            instance.accept(textAlignment, i, 85, Component.literal(minecraft.player.blockPosition().toShortString()));
        }
    }
}
