package dev.creoii.greatbigworld.adventures.util;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public interface ShowDeathCoordinates {
    void gbw$setShowDeathCoordinates(boolean showDeathCoordinates);

    boolean gbw$shouldShowDeathCoordinates();

    record SyncS2C(boolean value) implements CustomPayload {
        public static final CustomPayload.Id<SyncS2C> PACKET_ID = new CustomPayload.Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "show_death_coordinates"));
        public static final PacketCodec<RegistryByteBuf, SyncS2C> PACKET_CODEC = PacketCodec.of(SyncS2C::write, SyncS2C::new);

        public SyncS2C(RegistryByteBuf buf) {
            this(buf.readBoolean());
        }

        public void write(RegistryByteBuf buf) {
            buf.writeBoolean(value);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }

    record RequestC2S() implements CustomPayload {
        public static final Id<RequestC2S> PACKET_ID = new Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "request_show_death_coordinates"));
        public static final PacketCodec<RegistryByteBuf, RequestC2S> PACKET_CODEC = PacketCodec.of(RequestC2S::write, RequestC2S::new);

        public RequestC2S(RegistryByteBuf buf) {
            this();
        }

        public void write(RegistryByteBuf buf) {}

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }
}
