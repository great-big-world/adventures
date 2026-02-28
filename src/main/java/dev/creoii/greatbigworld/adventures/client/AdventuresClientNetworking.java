package dev.creoii.greatbigworld.adventures.client;

import dev.creoii.greatbigworld.adventures.util.AllowDebugHud;
import dev.creoii.greatbigworld.adventures.util.ShowDeathCoordinates;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class AdventuresClientNetworking {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ShowDeathCoordinates.SyncS2C.PACKET_ID, (payload, context) -> {
            boolean value = payload.value();
            context.client().execute(() -> {
                if (context.client().level instanceof ShowDeathCoordinates showDeathCoordinates) {
                    showDeathCoordinates.gbw$setShowDeathCoordinates(value);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(AllowDebugHud.SyncS2C.PACKET_ID, (payload, context) -> {
            boolean value = payload.value();
            context.client().execute(() -> {
                if (context.client().level instanceof AllowDebugHud allowDebugHud) {
                    allowDebugHud.gbw$setAllowDebugHud(value);
                }
            });
        });
    }
}
