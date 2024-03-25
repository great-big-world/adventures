package dev.creoii.greatbigworld.adventures.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/SubtitlesHud;render(Lnet/minecraft/client/gui/DrawContext;)V"))
    private void gbw$renderCompassCoordsHud(DrawContext context, float tickDelta, CallbackInfo ci) {
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer != null && client.world != null) {
            List<String> texts = new ArrayList<>();
            if (clientPlayer.getInventory().contains(Items.COMPASS.getDefaultStack())){
                String text = clientPlayer.getBlockX() + ", " + clientPlayer.getBlockY() + ", " + clientPlayer.getBlockZ();
                texts.add("\uD83E\uDDED " + text);
            }
            if (clientPlayer.getInventory().contains(Items.CLOCK.getDefaultStack())) {
                long time = client.world.getTimeOfDay() * 50;
                Date date = new Date(time);
                texts.add("\uD83D\uDD50 " + new SimpleDateFormat("HH:mm").format(date));
            }

            for (int i = 0; i < texts.size(); ++i) {
                String text = texts.get(i);
                context.drawTextWithShadow(client.textRenderer, text, 5, 5 + (i * 10), 0xffffff);
            }
        }
    }
}
