package dev.creoii.greatbigworld.adventures.registry;

import dev.creoii.greatbigworld.adventures.util.AllowDebugHud;
import dev.creoii.greatbigworld.adventures.util.ShowDeathCoordinates;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;

public final class AdventuresEvents {
    public static void register() {
        GameRuleEvents.changeCallback(AdventuresGameRules.SHOW_DEATH_COORDINATES).register((value, server) -> {
            for (ServerLevel world : server.getAllLevels()) {
                if (world instanceof ShowDeathCoordinates showDeathCoordinates) {
                    showDeathCoordinates.gbw$setShowDeathCoordinates(value);
                    PlayerLookup.world(world).forEach(serverPlayer -> ServerPlayNetworking.send(serverPlayer, new ShowDeathCoordinates.SyncS2C(value)));
                }
            }
        });
        GameRuleEvents.changeCallback(AdventuresGameRules.ALLOW_DEBUG_HUD).register((value, server) -> {
            for (ServerLevel world : server.getAllLevels()) {
                if (world instanceof AllowDebugHud allowDebugHud) {
                    allowDebugHud.gbw$setAllowDebugHud(value);
                    PlayerLookup.world(world).forEach(serverPlayer -> ServerPlayNetworking.send(serverPlayer, new AllowDebugHud.SyncS2C(value)));
                }
            }
        });

        EntitySleepEvents.ALLOW_BED.register((playerEntity, blockPos, state, b) -> {
            if (!playerEntity.level().isClientSide() && ((ServerLevel) playerEntity.level()).getGameRules().get(AdventuresGameRules.SLEEP_DURING_DAY)) {
                return InteractionResult.SUCCESS;
            } else return InteractionResult.PASS;
        });
    }
}
