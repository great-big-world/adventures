package dev.creoii.greatbigworld.adventures.client;

import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import dev.creoii.greatbigworld.adventures.util.ExtendedHudPlayer;
import dev.creoii.greatbigworld.adventures.util.ItemInfoHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;

import java.util.*;

public class AdventuresClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient && !player.isSpectator() && player instanceof ExtendedHudPlayer extendedHudPlayer) {
                ItemStack stack = player.getStackInHand(hand);
                ExtendedHudPlayer.Type type = ExtendedHudPlayer.Type.COMPASS;
                if (stack.isOf(Items.RECOVERY_COMPASS)) {
                    type = ExtendedHudPlayer.Type.RECOVERY_COMPASS;
                } else if (stack.isOf(Items.CLOCK)) {
                    type = ExtendedHudPlayer.Type.CLOCK;
                } else if (stack.isOf(AdventuresItems.ASTROLABE)) {
                    type = ExtendedHudPlayer.Type.ASTROLABE;
                }
                extendedHudPlayer.gbw$getItemInfoHuds().get(type).invert();
                return TypedActionResult.success(stack);
            }
            return TypedActionResult.pass(ItemStack.EMPTY);
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            final MinecraftClient client = MinecraftClient.getInstance();
            if (!client.options.hudHidden) {
                ClientPlayerEntity clientPlayer = client.player;
                if (clientPlayer != null && client.world != null && clientPlayer instanceof ExtendedHudPlayer extendedHudPlayer) {
                    List<Identifier> sprites = new ArrayList<>();
                    List<Text> texts = new ArrayList<>();
                    Map<ExtendedHudPlayer.Type, ItemInfoHud> itemInfoHuds = extendedHudPlayer.gbw$getItemInfoHuds();
                    itemInfoHuds.forEach((type, itemInfoHud) -> {
                        if (itemInfoHud.canRender(clientPlayer)) {
                            if (type == ExtendedHudPlayer.Type.RECOVERY_COMPASS && !texts.isEmpty()) {
                                sprites.set(0, itemInfoHud.getIconId(clientPlayer));
                                texts.set(0, itemInfoHud.getText(clientPlayer));
                            } else {
                                sprites.add(itemInfoHud.getIconId(clientPlayer));
                                texts.add(itemInfoHud.getText(clientPlayer));
                            }
                        }
                    });
                    if (!sprites.isEmpty()) {
                        drawContext.getMatrices().push();
                        drawContext.getMatrices().scale(1.5f, 1.5f, 1.5f);
                        for (int i = 0; i < sprites.size(); ++i) {
                            Identifier sprite = sprites.get(i);
                            drawContext.drawTexture(sprite.withPrefixedPath("textures/gui/hud/icon/").withSuffixedPath(".png"), 2, 2 + (i * 8), 0f, 0f, 7, 7, 7, 7);
                        }
                        drawContext.getMatrices().pop();

                        for (int i = 0; i < texts.size(); ++i) {
                            Text text = texts.get(i);
                            drawContext.drawTextWithShadow(client.textRenderer, text, 17, 5 + (i * 12), 0xffffff);
                        }
                    }
                }
            }
        });
    }

    public static String getDisplayTime(ClientPlayerEntity clientPlayer) {
        long time = (clientPlayer.clientWorld.getTimeOfDay() + 6000L) % 24000L;
        long hours = (time / 1000L) % 24L;
        long minutes = (time % 1000L) * 60L / 1000L;
        if (hours == 0L) {
            hours = 12L;
        } else if (hours > 12L) {
            hours -= 12L;
        }
        String displayTime = String.format("%02d:%02d %s", hours, minutes, time < 12000L ? "PM" : "AM");
        if (displayTime.startsWith("0"))
            displayTime = displayTime.substring(1);
        return displayTime;
    }
}
