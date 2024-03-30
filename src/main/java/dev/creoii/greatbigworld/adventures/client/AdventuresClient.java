package dev.creoii.greatbigworld.adventures.client;

import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import dev.creoii.greatbigworld.adventures.util.ExtendedHudPlayer;
import dev.creoii.greatbigworld.adventures.util.ItemInfoHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class AdventuresClient implements ClientModInitializer {
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (!CLIENT.options.hudHidden) {
                ClientPlayerEntity clientPlayer = CLIENT.player;
                if (clientPlayer != null && CLIENT.world != null && clientPlayer instanceof ExtendedHudPlayer extendedHudPlayer) {
                    List<String> texts = new ArrayList<>();
                    PlayerInventory inventory = clientPlayer.getInventory();
                    Map<Item, ItemInfoHud> itemInfoHuds = extendedHudPlayer.gbw$getItemInfoHuds();
                    if (inventory.containsAny(stack -> stack.isOf(Items.COMPASS)) && itemInfoHuds.get(Items.COMPASS).isActive()) {
                        String text = clientPlayer.getBlockX() + ", " + clientPlayer.getBlockY() + ", " + clientPlayer.getBlockZ();
                        texts.add("\uD83E\uDDED " + text);
                    }
                    if (inventory.containsAny(stack -> stack.isOf(Items.RECOVERY_COMPASS)) && itemInfoHuds.get(Items.RECOVERY_COMPASS).isActive()) {
                        Optional<GlobalPos> deathPos = clientPlayer.getLastDeathPos();
                        if (deathPos.isPresent()) {
                            BlockPos pos = deathPos.get().getPos();
                            String text = pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
                            if (!texts.isEmpty())
                                texts.set(0, "\uD83E\uDEA6 " + text);
                            else texts.add("\uD83E\uDEA6 " + text);
                        }
                    }
                    if (inventory.containsAny(stack -> stack.isOf(AdventuresItems.ASTROLABE)) && itemInfoHuds.get(AdventuresItems.ASTROLABE).isActive()) {
                        texts.add(getGlobeEmoji() + " " + StringUtils.capitalize(clientPlayer.getHorizontalFacing().getName()));
                    }
                    if (inventory.containsAny(stack -> stack.isOf(Items.CLOCK)) && itemInfoHuds.get(Items.CLOCK).isActive()) {
                        /*long time = CLIENT.world.getTimeOfDay() * 50;
                        Date date = new Date(time);
                        texts.add("\uD83D\uDD50 " + new SimpleDateFormat("HH:mm").format(date));*/
                        texts.add("\uD83D\uDD50 " + getGameTime());
                    }

                    for (int i = 0; i < texts.size(); ++i) {
                        String text = texts.get(i);
                        drawContext.drawTextWithShadow(CLIENT.textRenderer, text, 5, 5 + (i * 10), 0xffffff);
                    }
                }
            }
        });
    }

    private static String getGlobeEmoji() {
        Locale locale = Locale.getDefault();
        if (locale == Locale.JAPAN || locale == Locale.JAPANESE || locale == Locale.CHINA || locale == Locale.CHINESE || locale == Locale.SIMPLIFIED_CHINESE || locale == Locale.TRADITIONAL_CHINESE || locale == Locale.KOREA || locale == Locale.KOREAN || locale == Locale.TAIWAN) {
            return "\uD83C\uDF0F";
        } else if (locale == Locale.ITALIAN || locale == Locale.ITALY || locale == Locale.GERMAN || locale == Locale.GERMANY || locale == Locale.FRANCE || locale == Locale.FRENCH || locale == Locale.UK) {
            return "\uD83C\uDF0D";
        }
        return "🌎";
    }

    /**
     * Modify this to be within my conventions
     * Perhaps modify how it works so that 1 minute isnt 1 second?
     */
    private static String getGameTime() {
        int time;
        int gametime = (int)CLIENT.world.getTime();

        while (gametime >= 24000) {
            gametime-=24000;
        }

        if (gametime >= 18000) {
            time = gametime-18000;
        }
        else {
            time = 6000+gametime;
        }

        String suffix = "";
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
