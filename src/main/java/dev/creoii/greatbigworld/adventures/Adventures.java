package dev.creoii.greatbigworld.adventures;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.registry.AdventuresChunkGenerators;
import dev.creoii.greatbigworld.adventures.registry.AdventuresFeatures;
import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import dev.creoii.greatbigworld.adventures.util.AllowDebugHud;
import dev.creoii.greatbigworld.adventures.util.ShowDeathCoordinates;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.dimension.DimensionType;

public class Adventures implements ModInitializer {
    @Override
    public void onInitialize() {
        AdventuresItems.register();
        AdventuresFeatures.register();
        AdventuresGameRules.register();
        AdventuresChunkGenerators.register();

        PayloadTypeRegistry.playS2C().register(TeleportDestinationS2C.PACKET_ID, TeleportDestinationS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(ShowDeathCoordinates.SyncS2C.PACKET_ID, ShowDeathCoordinates.SyncS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(AllowDebugHud.SyncS2C.PACKET_ID, AllowDebugHud.SyncS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncDayLengthS2C.PACKET_ID, SyncDayLengthS2C.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(ShowDeathCoordinates.RequestC2S.PACKET_ID, ShowDeathCoordinates.RequestC2S.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(AllowDebugHud.RequestC2S.PACKET_ID, AllowDebugHud.RequestC2S.PACKET_CODEC);

        GameRuleEvents.changeCallback(AdventuresGameRules.SHOW_COORDINATES_ON_DEATH).register((value, server) -> {
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

        ServerPlayNetworking.registerGlobalReceiver(ShowDeathCoordinates.RequestC2S.PACKET_ID, (requestC2S, context) -> {
            ServerPlayNetworking.send(context.player(), new ShowDeathCoordinates.SyncS2C(context.player().level().getGameRules().get(AdventuresGameRules.SHOW_COORDINATES_ON_DEATH)));
        });

        ServerPlayNetworking.registerGlobalReceiver(AllowDebugHud.RequestC2S.PACKET_ID, (requestC2S, context) -> {
            ServerPlayNetworking.send(context.player(), new AllowDebugHud.SyncS2C(context.player().level().getGameRules().get(AdventuresGameRules.ALLOW_DEBUG_HUD)));
        });
    }

    public record TeleportDestinationS2C(ResourceKey<DimensionType> destinationDimension) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<TeleportDestinationS2C> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "teleport_destination"));
        public static final StreamCodec<RegistryFriendlyByteBuf, TeleportDestinationS2C> PACKET_CODEC = StreamCodec.ofMember(TeleportDestinationS2C::write, TeleportDestinationS2C::new);

        public TeleportDestinationS2C(RegistryFriendlyByteBuf buf) {
            this(buf.readResourceKey(Registries.DIMENSION_TYPE));
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeResourceKey(destinationDimension);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    public record SyncDayLengthS2C(long length, boolean day) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<SyncDayLengthS2C> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "sync_day_length"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SyncDayLengthS2C> PACKET_CODEC = StreamCodec.ofMember(SyncDayLengthS2C::write, SyncDayLengthS2C::new);

        public SyncDayLengthS2C(RegistryFriendlyByteBuf buf) {
            this(buf.readLong(), buf.readBoolean());
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeLong(length);
            buf.writeBoolean(day);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
}
