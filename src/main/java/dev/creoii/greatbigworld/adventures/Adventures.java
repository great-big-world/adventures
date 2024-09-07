package dev.creoii.greatbigworld.adventures;

import dev.creoii.creoapi.api.item.CreoItemApi;
import dev.creoii.greatbigworld.adventures.component.JournalContentComponent;
import dev.creoii.greatbigworld.adventures.registry.AdventuresBlocks;
import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.component.DataComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;

public class Adventures implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";
    public static final DataComponentType<JournalContentComponent> JOURNAL_CONTENT = DataComponentType.<JournalContentComponent>builder().codec(JournalContentComponent.CODEC).packetCodec(JournalContentComponent.PACKET_CODEC).cache().build();

    @Override
    public void onInitialize() {
        AdventuresBlocks.register();
        AdventuresItems.register();
        AdventuresGameRules.register();
        Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(CreoItemApi.NAMESPACE, "journal_content"), JOURNAL_CONTENT);

        PayloadTypeRegistry.playS2C().register(TeleportDestination.PACKET_ID, TeleportDestination.PACKET_CODEC);

        DefaultItemComponentEvents.MODIFY.register(context -> {
            context.modify(AdventuresItems.JOURNAL, builder -> {
                builder.add(JOURNAL_CONTENT, new JournalContentComponent(new ArrayList<>()));
            });
        });
    }

    public record TeleportDestination(RegistryKey<DimensionType> destinationDimension) implements CustomPayload {
        public static final CustomPayload.Id<TeleportDestination> PACKET_ID = new CustomPayload.Id<>(new Identifier(Adventures.NAMESPACE, "teleport_destination"));
        public static final PacketCodec<RegistryByteBuf, TeleportDestination> PACKET_CODEC = PacketCodec.of(TeleportDestination::write, TeleportDestination::new);

        public TeleportDestination(RegistryByteBuf buf) {
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
