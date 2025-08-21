package dev.creoii.greatbigworld.adventures.client;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.Adventures;
import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import dev.creoii.greatbigworld.adventures.util.AdventuresTags;
import dev.creoii.greatbigworld.adventures.util.ExtendedHudPlayer;
import dev.creoii.greatbigworld.adventures.util.ItemInfoHud;
import dev.creoii.greatbigworld.adventures.util.ShowDeathCoordinates;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AdventuresClient implements ClientModInitializer {
    @Nullable
    private static RegistryKey<DimensionType> destinationDimension = null;

    @Override
    public void onInitializeClient() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient && !player.isSpectator() && player instanceof ExtendedHudPlayer extendedHudPlayer) {
                ItemStack stack = player.getStackInHand(hand);
                if (stack.isIn(AdventuresTags.INFO_HUD_ITEMS)) {
                    ExtendedHudPlayer.Type type = ExtendedHudPlayer.Type.COMPASS;
                    if (stack.isOf(Items.RECOVERY_COMPASS)) {
                        type = ExtendedHudPlayer.Type.RECOVERY_COMPASS;
                    } else if (stack.isOf(Items.CLOCK)) {
                        type = ExtendedHudPlayer.Type.CLOCK;
                    } else if (stack.isOf(AdventuresItems.ASTROLABE)) {
                        type = ExtendedHudPlayer.Type.ASTROLABE;
                    }
                    extendedHudPlayer.gbw$getItemInfoHuds().get(type).invert();
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });

        HudElementRegistry.attachElementBefore(VanillaHudElements.STATUS_EFFECTS, Identifier.of(GreatBigWorld.NAMESPACE, "item_info_huds"), (context, tickCounter) -> {
            if (!context.client.options.hudHidden) {
                ClientPlayerEntity clientPlayer = context.client.player;
                if (clientPlayer != null && context.client.world != null && clientPlayer instanceof ExtendedHudPlayer extendedHudPlayer) {
                    List<Identifier> sprites = new ArrayList<>();
                    List<Text> texts = new ArrayList<>();
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
                        context.getMatrices().pushMatrix();
                        context.getMatrices().scale(1.5f, 1.5f);
                        for (int i = 0; i < sprites.size(); ++i) {
                            Identifier sprite = sprites.get(i);
                            context.drawTexture(RenderPipelines.GUI_TEXTURED, sprite.withPrefixedPath("textures/gui/hud/icon/").withSuffixedPath(".png"), 2, 2 + (i * 8), 0f, 0f, 7, 7, 7, 7);
                        }
                        context.getMatrices().popMatrix();

                        for (int i = 0; i < texts.size(); ++i) {
                            Text text = texts.get(i);
                            context.drawTextWithShadow(context.client.textRenderer, text, 17, 5 + (i * 12), 0xffffff);
                        }
                    }
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(Adventures.TeleportDestinationS2C.PACKET_ID, (payload, context) -> {
            RegistryKey<DimensionType> registryKey = payload.destinationDimension();
            context.client().execute(() -> {
                destinationDimension = registryKey;
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ShowDeathCoordinates.SyncS2C.PACKET_ID, (payload, context) -> {
            boolean value = payload.value();
            context.client().execute(() -> {
                if (context.client().world instanceof ShowDeathCoordinates showDeathCoordinates) {
                    showDeathCoordinates.gbw$setShowDeathCoordinates(value);
                }
            });
        });
    }

    public static @Nullable RegistryKey<DimensionType> getDestinationDimension() {
        return destinationDimension;
    }

    public static String getDisplayTime(ClientPlayerEntity clientPlayer) {
        if (clientPlayer.clientWorld != null) {
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
        return "";
    }
}
