package dev.creoii.greatbigworld.adventures.client;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.util.*;
import dev.creoii.greatbigworld.util.OptionsAPI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.*;

public class AdventuresClient implements ClientModInitializer {
    public static final ModelLayerLocation THIN_LEASH_KNOT = new ModelLayerLocation(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "thin_leash_knot"), "main");

    @Override
    public void onInitializeClient() {
        AdventuresClientNetworking.register();
        AdventuresClientEvents.register();

        EntityModelLayerRegistry.registerModelLayer(THIN_LEASH_KNOT, ThinLeashKnotModel::createBodyLayer);

        HudElementRegistry.attachElementBefore(VanillaHudElements.STATUS_EFFECTS, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "item_info_huds"), (context, tickCounter) -> {
            if (!context.minecraft.options.hideGui) {
                LocalPlayer clientPlayer = context.minecraft.player;
                if (clientPlayer != null && context.minecraft.level != null && clientPlayer instanceof ExtendedHudPlayer extendedHudPlayer) {
                    List<Identifier> sprites = new ArrayList<>();
                    List<Component> texts = new ArrayList<>();
                    Map<ExtendedHudPlayer.Type, ItemInfoHud> itemInfoHuds = extendedHudPlayer.gbw$getItemInfoHuds();
                    itemInfoHuds.forEach((type, itemInfoHud) -> {
                        if (itemInfoHud.isActive() && itemInfoHud.canRender(clientPlayer)) {
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
                        context.pose().pushMatrix();
                        context.pose().scale(1.5f, 1.5f);
                        for (int i = 0; i < sprites.size(); ++i) {
                            Identifier sprite = sprites.get(i).withPrefix("textures/gui/hud/icon/").withSuffix(".png");
                            context.blit(RenderPipelines.GUI_TEXTURED, sprite, 2, 2 + (i * 8), 0f, 0f, 7, 7, 7, 7);
                        }
                        context.pose().popMatrix();

                        for (int i = 0; i < texts.size(); ++i) {
                            Component text = texts.get(i);
                            context.drawString(context.minecraft.font, text, 17, 5 + (i * 12), 0xffffff);
                        }
                    }
                }
            }
        });

        OptionsAPI.registerVideoOption(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "dynamic_darkness_quality"), new OptionInstance<>(
                "options.dynamicDarknessQuality",
                OptionInstance.noTooltip(),
                (optionText, value) -> DynamicDarknessQuality.NAMES[value.ordinal()],
                new OptionInstance.Enum<>(Arrays.asList(DynamicDarknessQuality.values()), DynamicDarknessQuality.CODEC),
                DynamicDarknessQuality.NORMAL,
                value -> {}));
    }

    public static String getDisplayTime(LocalPlayer clientPlayer) {
        if (clientPlayer.level() != null) {
            long time = (clientPlayer.level().getDayTime() + 6000L) % 24000L;
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
        return "";
    }
}
