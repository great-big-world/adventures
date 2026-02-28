package dev.creoii.greatbigworld.adventures;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.registry.AdventuresChunkGenerators;
import dev.creoii.greatbigworld.adventures.registry.AdventuresEvents;
import dev.creoii.greatbigworld.adventures.registry.AdventuresFeatures;
import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import net.fabricmc.api.ModInitializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class Adventures implements ModInitializer {
    @Override
    public void onInitialize() {
        AdventuresFeatures.register();
        AdventuresGameRules.register();
        AdventuresChunkGenerators.register();
        AdventuresNetworking.register();
        AdventuresEvents.register();
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
