package dev.creoii.greatbigworld.adventures;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.registry.AdventuresChunkGenerators;
import dev.creoii.greatbigworld.adventures.registry.AdventuresFeatures;
import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import dev.creoii.greatbigworld.adventures.util.AllowDebugHud;
import dev.creoii.greatbigworld.adventures.util.ShowDeathCoordinates;
import dev.creoii.greatbigworld.event.GameRuleEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.dimension.DimensionType;

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

        GameRuleEvents.SET.register((key, world) -> {
            if (key == AdventuresGameRules.LENGTH_OF_DAY) {
                PlayerLookup.world(world).forEach(serverPlayer -> ServerPlayNetworking.send(serverPlayer, new SyncDayLengthS2C(world.getGameRules().getInt(AdventuresGameRules.LENGTH_OF_DAY), true)));
            } else if (key == AdventuresGameRules.LENGTH_OF_NIGHT) {
                PlayerLookup.world(world).forEach(serverPlayer -> ServerPlayNetworking.send(serverPlayer, new SyncDayLengthS2C(world.getGameRules().getInt(AdventuresGameRules.LENGTH_OF_NIGHT), false)));
            } else if (world instanceof ShowDeathCoordinates showDeathCoordinates && key == AdventuresGameRules.SHOW_COORDINATES_ON_DEATH) {
                boolean value = world.getGameRules().getBoolean(AdventuresGameRules.SHOW_COORDINATES_ON_DEATH);
                showDeathCoordinates.gbw$setShowDeathCoordinates(value);
                PlayerLookup.world(world).forEach(serverPlayer -> ServerPlayNetworking.send(serverPlayer, new ShowDeathCoordinates.SyncS2C(value)));
            } else if (world instanceof AllowDebugHud allowDebugHud && key == AdventuresGameRules.ALLOW_DEBUG_HUD) {
                boolean value = world.getGameRules().getBoolean(AdventuresGameRules.ALLOW_DEBUG_HUD);
                allowDebugHud.gbw$setAllowDebugHud(value);
                PlayerLookup.world(world).forEach(serverPlayer -> ServerPlayNetworking.send(serverPlayer, new AllowDebugHud.SyncS2C(value)));
            }
        });

        ServerPlayerEvents.JOIN.register(serverPlayer -> {
            ServerPlayNetworking.send(serverPlayer, new SyncDayLengthS2C(serverPlayer.getEntityWorld().getGameRules().getInt(AdventuresGameRules.LENGTH_OF_DAY), true));
            ServerPlayNetworking.send(serverPlayer, new SyncDayLengthS2C(serverPlayer.getEntityWorld().getGameRules().getInt(AdventuresGameRules.LENGTH_OF_NIGHT), false));
        });

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerWorld world : server.getWorlds()) {
                if (!world.getDimension().hasFixedTime()) {
                    GameRules gameRules = world.getGameRules();
                    int dayLength = gameRules.getInt(AdventuresGameRules.LENGTH_OF_DAY);
                    int nightLength = gameRules.getInt(AdventuresGameRules.LENGTH_OF_NIGHT);
                    int total = dayLength + nightLength;

                    long time = world.getTimeOfDay() % total;
                    float multiplier;

                    if (time < dayLength) {
                        multiplier = (total / 2f) / dayLength;
                    } else {
                        multiplier = (total / 2f) / nightLength;
                    }

                    world.setTimeOfDay(world.getTimeOfDay() + (long) multiplier);
                }
            }
        });

        EntitySleepEvents.ALLOW_SLEEP_TIME.register((playerEntity, blockPos, b) -> {
            if (!playerEntity.getEntityWorld().isClient() && ((ServerWorld) playerEntity.getEntityWorld()).getGameRules().getBoolean(AdventuresGameRules.SLEEP_DURING_DAY)) {
                return ActionResult.SUCCESS;
            } else return ActionResult.PASS;
        });

        ServerPlayNetworking.registerGlobalReceiver(ShowDeathCoordinates.RequestC2S.PACKET_ID, (requestC2S, context) -> {
            ServerPlayNetworking.send(context.player(), new ShowDeathCoordinates.SyncS2C(context.player().getEntityWorld().getGameRules().getBoolean(AdventuresGameRules.SHOW_COORDINATES_ON_DEATH)));
        });

        ServerPlayNetworking.registerGlobalReceiver(AllowDebugHud.RequestC2S.PACKET_ID, (requestC2S, context) -> {
            ServerPlayNetworking.send(context.player(), new AllowDebugHud.SyncS2C(context.player().getEntityWorld().getGameRules().getBoolean(AdventuresGameRules.ALLOW_DEBUG_HUD)));
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

    public record SyncDayLengthS2C(long length, boolean day) implements CustomPayload {
        public static final CustomPayload.Id<SyncDayLengthS2C> PACKET_ID = new CustomPayload.Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "sync_day_length"));
        public static final PacketCodec<RegistryByteBuf, SyncDayLengthS2C> PACKET_CODEC = PacketCodec.of(SyncDayLengthS2C::write, SyncDayLengthS2C::new);

        public SyncDayLengthS2C(RegistryByteBuf buf) {
            this(buf.readLong(), buf.readBoolean());
        }

        public void write(RegistryByteBuf buf) {
            buf.writeLong(length);
            buf.writeBoolean(day);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }
}
