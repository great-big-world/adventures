package dev.creoii.greatbigworld.adventures.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdventuresClient implements ClientModInitializer {
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (!CLIENT.options.hudHidden) {
                ClientPlayerEntity clientPlayer = CLIENT.player;
                if (clientPlayer != null && CLIENT.world != null) {
                    List<String> texts = new ArrayList<>();
                    if (clientPlayer.getInventory().containsAny(stack -> stack.isOf(Items.COMPASS))) {
                        String text = clientPlayer.getBlockX() + ", " + clientPlayer.getBlockY() + ", " + clientPlayer.getBlockZ();
                        texts.add(clientPlayer.getHorizontalFacing().name().charAt(0) + ": " + text);
                    }
                    if (clientPlayer.getInventory().containsAny(stack -> stack.isOf(Items.RECOVERY_COMPASS))) {
                        Optional<GlobalPos> deathPos = clientPlayer.getLastDeathPos();
                        if (deathPos.isPresent()) {
                            BlockPos pos = deathPos.get().getPos();
                            String text = pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
                            texts.set(0, "\uD83E\uDEA6 " + text);
                        }
                    }
                    if (clientPlayer.getInventory().containsAny(stack -> stack.isOf(Items.CLOCK))) {
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
