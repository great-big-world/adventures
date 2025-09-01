package dev.creoii.greatbigworld.adventures;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import dev.creoii.greatbigworld.adventures.util.AllowDebugHud;
import dev.creoii.greatbigworld.adventures.util.ShowDeathCoordinates;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;

public class Adventures implements ModInitializer {
    @Override
    public void onInitialize() {
        AdventuresItems.register();
        AdventuresGameRules.register();

        PayloadTypeRegistry.playS2C().register(TeleportDestinationS2C.PACKET_ID, TeleportDestinationS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(ShowDeathCoordinates.SyncS2C.PACKET_ID, ShowDeathCoordinates.SyncS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(AllowDebugHud.SyncS2C.PACKET_ID, AllowDebugHud.SyncS2C.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(ShowDeathCoordinates.RequestC2S.PACKET_ID, ShowDeathCoordinates.RequestC2S.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(AllowDebugHud.RequestC2S.PACKET_ID, AllowDebugHud.RequestC2S.PACKET_CODEC);

        EntitySleepEvents.ALLOW_SLEEP_TIME.register((playerEntity, blockPos, b) -> {
            if (!playerEntity.getWorld().isClient && ((ServerWorld) playerEntity.getWorld()).getGameRules().getBoolean(AdventuresGameRules.SLEEP_DURING_DAY)) {
                return ActionResult.SUCCESS;
            } else return ActionResult.PASS;
        });

        ServerPlayNetworking.registerGlobalReceiver(ShowDeathCoordinates.RequestC2S.PACKET_ID, (requestC2S, context) -> {
            ServerPlayNetworking.send(context.player(), new ShowDeathCoordinates.SyncS2C(context.player().getWorld().getGameRules().getBoolean(AdventuresGameRules.SHOW_COORDINATES_ON_DEATH)));
        });

        ServerPlayNetworking.registerGlobalReceiver(AllowDebugHud.RequestC2S.PACKET_ID, (requestC2S, context) -> {
            ServerPlayNetworking.send(context.player(), new AllowDebugHud.SyncS2C(context.player().getWorld().getGameRules().getBoolean(AdventuresGameRules.ALLOW_DEBUG_HUD)));
        });
    }

    public record TeleportDestinationS2C(RegistryKey<DimensionType> destinationDimension) implements CustomPayload {
        public static final CustomPayload.Id<TeleportDestinationS2C> PACKET_ID = new CustomPayload.Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "teleport_destination"));
        public static final PacketCodec<RegistryByteBuf, TeleportDestinationS2C> PACKET_CODEC = PacketCodec.of(TeleportDestinationS2C::write, TeleportDestinationS2C::new);

        public TeleportDestinationS2C(RegistryByteBuf buf) {
            this(buf.readRegistryKey(RegistryKeys.DIMENSION_TYPE));
        }

        public void write(RegistryByteBuf buf) {
            buf.writeRegistryKey(destinationDimension);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }
}
