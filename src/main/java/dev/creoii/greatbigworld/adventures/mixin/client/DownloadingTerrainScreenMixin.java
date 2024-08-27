package dev.creoii.greatbigworld.adventures.mixin.client;

import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DownloadingTerrainScreen.class)
public abstract class DownloadingTerrainScreenMixin extends Screen {
    protected DownloadingTerrainScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"), cancellable = true)
    private void gbw$changeLoadingTerrainText(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (client != null && client.world != null) {
            context.drawCenteredTextWithShadow(textRenderer, Text.translatable("multiplayer.downloadingTerrain", StringUtil.capitalize(client.world.getDimensionEntry().getKey().get().getValue().getPath())), width / 2, height / 2 - 50, 16777215);
        }
        ci.cancel();
    }
}
