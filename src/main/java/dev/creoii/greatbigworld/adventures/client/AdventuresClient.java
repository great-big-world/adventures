package dev.creoii.greatbigworld.adventures.client;

import dev.creoii.greatbigworld.adventures.util.ExtendedHudPlayer;
import dev.creoii.greatbigworld.adventures.util.ItemInfoHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;

public class AdventuresClient implements ClientModInitializer {
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (!CLIENT.options.hudHidden) {
                ClientPlayerEntity clientPlayer = CLIENT.player;
                if (clientPlayer != null && CLIENT.world != null && clientPlayer instanceof ExtendedHudPlayer extendedHudPlayer) {
                    List<Identifier> sprites = new ArrayList<>();
                    List<Text> texts = new ArrayList<>();
                    PlayerInventory inventory = clientPlayer.getInventory();
                    Map<Item, ItemInfoHud> itemInfoHuds = extendedHudPlayer.gbw$getItemInfoHuds();

                    itemInfoHuds.forEach((item, itemInfoHud) -> {
                        if (itemInfoHud.canRender(inventory)) {
                            if (item == Items.RECOVERY_COMPASS && !texts.isEmpty()) {
                                sprites.set(0, itemInfoHud.getIconId(clientPlayer));
                                texts.set(0, itemInfoHud.getText(clientPlayer));
                            } else {
                                sprites.add(itemInfoHud.getIconId(clientPlayer));
                                texts.add(itemInfoHud.getText(clientPlayer));
                            }
                        }
                    });

                    drawContext.getMatrices().push();
                    drawContext.getMatrices().scale(1.5f, 1.5f, 1.5f);
                    for (int i = 0; i < sprites.size(); ++i) {
                        Identifier sprite = sprites.get(i);
                        drawContext.drawTexture(sprite.withPrefixedPath("textures/gui/hud/icon/").withSuffixedPath(".png"), 2, 2 + (i * 8), 0f, 0f, 7, 7, 7, 7);
                    }
                    drawContext.getMatrices().pop();

                    for (int i = 0; i < texts.size(); ++i) {
                        Text text = texts.get(i);
                        drawContext.drawTextWithShadow(CLIENT.textRenderer, text, 17, 5 + (i * 12), 0xffffff);
                    }
                }
            }
        });
    }

    public static String getGameTime(ClientPlayerEntity clientPlayer) {
        long time = (clientPlayer.clientWorld.getTimeOfDay() + 6000L) % 24000L;
        long hours = (time / 1000L) % 24L;
        if (hours == 0L) {
            hours = 12L;
        } else if (hours > 12L) {
            hours -= 12L;
        }

        return String.format("%02d:%02d %s", hours, (time % 1000L) * 60L / 1000L, time < 12000L ? "PM" : "AM");
    }
}
