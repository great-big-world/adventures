package dev.creoii.greatbigworld.adventures.client;

import dev.creoii.greatbigworld.adventures.util.AdventuresTags;
import dev.creoii.greatbigworld.adventures.util.AllowDebugHud;
import dev.creoii.greatbigworld.adventures.util.ExtendedHudPlayer;
import dev.creoii.greatbigworld.adventures.util.ShowDeathCoordinates;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class AdventuresClientEvents {
    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClientSide() && !player.isSpectator() && player instanceof ExtendedHudPlayer extendedHudPlayer) {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.is(AdventuresTags.INFO_HUD_ITEMS)) {
                    ExtendedHudPlayer.Type type = ExtendedHudPlayer.Type.COMPASS;
                    if (stack.is(Items.RECOVERY_COMPASS)) {
                        type = ExtendedHudPlayer.Type.RECOVERY_COMPASS;
                    } else if (stack.is(Items.CLOCK)) {
                        type = ExtendedHudPlayer.Type.CLOCK;
                    }
                    extendedHudPlayer.gbw$getItemInfoHuds().get(type).invert();
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });

        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, minecraftClient) -> {
            ClientPlayNetworking.send(new ShowDeathCoordinates.RequestC2S());
            ClientPlayNetworking.send(new AllowDebugHud.RequestC2S());
        });
    }
}
