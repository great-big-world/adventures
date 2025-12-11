package dev.creoii.greatbigworld.adventures.util;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public interface ShowDeathCoordinates {
    void gbw$setShowDeathCoordinates(boolean showDeathCoordinates);

    boolean gbw$shouldShowDeathCoordinates();

    record SyncS2C(boolean value) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<SyncS2C> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "show_death_coordinates"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SyncS2C> PACKET_CODEC = StreamCodec.ofMember(SyncS2C::write, SyncS2C::new);

        public SyncS2C(RegistryFriendlyByteBuf buf) {
            this(buf.readBoolean());
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeBoolean(value);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    record RequestC2S() implements CustomPacketPayload {
        public static final Type<RequestC2S> PACKET_ID = new Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "request_show_death_coordinates"));
        public static final StreamCodec<RegistryFriendlyByteBuf, RequestC2S> PACKET_CODEC = StreamCodec.ofMember(RequestC2S::write, RequestC2S::new);

        public RequestC2S(RegistryFriendlyByteBuf buf) {
            this();
        }

        public void write(RegistryFriendlyByteBuf buf) {}

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
}
