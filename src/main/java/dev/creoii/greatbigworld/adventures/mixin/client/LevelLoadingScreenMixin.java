package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawCenteredString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"))
    private void gbw$addDimensionToLoadingText(GuiGraphics instance, Font font, Component component, int i, int j, int k, Operation<Void> original) {
        Identifier dimensionId = GreatBigWorldClient.getToDimension();
        if (dimensionId != null)
            instance.drawCenteredString(font, Component.translatable("multiplayer.downloadingTerrain.targeted", Component.translatable("dimension_type." + dimensionId.getNamespace() + "." + dimensionId.getPath())), i, j, k);
        else instance.drawCenteredString(font, component, i, j, k);
    }
}
