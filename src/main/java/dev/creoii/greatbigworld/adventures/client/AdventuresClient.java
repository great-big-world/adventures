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
                        drawContext.drawTextWithShadow(CLIENT.textRenderer, text, 15, 5 + (i * 12), 0xffffff);
                    }
                }
            }
        });
    }

    /**
     * Modify this to be within my conventions
     * Perhaps modify how it works so that 1 minute isnt 1 second?
     *
     * Creds: Serilum
     */
    public static String getGameTime() {
        int time;
        int gametime = (int) CLIENT.world.getTime();

        while (gametime >= 24000) {
            gametime-=24000;
        }

        if (gametime >= 18000) {
            time = gametime-18000;
        }
        else {
            time = 6000+gametime;
        }

        String suffix;
        if (time >= 13000) {
            time = time - 12000;
            suffix = " PM";
        }
        else {
            if (time >= 12000) {
                suffix = " PM";
            }
            else {
                suffix = " AM";
                if (time <= 999) {
                    time += 12000;
                }
            }
        }

        StringBuilder stringtime = new StringBuilder(time / 10 + "");
        for (int n = stringtime.length(); n < 4; n++) {
            stringtime.insert(0, "0");
        }

        String[] strsplit = stringtime.toString().split("");

        int minutes = (int)Math.floor(Double.parseDouble(strsplit[2] + strsplit[3])/100*60);
        String sm = minutes + "";
        if (minutes < 10) {
            sm = "0" + minutes;
        }

        if (strsplit[0].equals("0")) {
            stringtime = new StringBuilder(strsplit[1] + ":" + sm.charAt(0) + sm.charAt(1));
        }
        else {
            stringtime = new StringBuilder(strsplit[0] + strsplit[1] + ":" + sm.charAt(0) + sm.charAt(1));
        }

        return stringtime + suffix;
    }
}
