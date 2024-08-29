package dev.creoii.greatbigworld.adventures;

import dev.creoii.creoapi.api.item.CreoItemApi;
import dev.creoii.greatbigworld.adventures.component.JournalContentComponent;
import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class Adventures implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";
    public static final DataComponentType<JournalContentComponent> JOURNAL_CONTENT = DataComponentType.<JournalContentComponent>builder().codec(JournalContentComponent.CODEC).packetCodec(JournalContentComponent.PACKET_CODEC).cache().build();

    @Override
    public void onInitialize() {
        AdventuresItems.register();
        AdventuresGameRules.register();
        Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(CreoItemApi.NAMESPACE, "journal_content"), JOURNAL_CONTENT);

        DefaultItemComponentEvents.MODIFY.register(context -> {
            context.modify(AdventuresItems.JOURNAL, builder -> {
                builder.add(JOURNAL_CONTENT, new JournalContentComponent(new ArrayList<>()));
            });
        });
    }
}
