package dev.creoii.greatbigworld.adventures.registry;

import dev.creoii.creoapi.api.item.CreoItemSettings;
import dev.creoii.greatbigworld.adventures.Adventures;
import dev.creoii.greatbigworld.adventures.item.AstrolabeItem;
import dev.creoii.greatbigworld.adventures.item.JournalItem;
import net.minecraft.item.BedItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class AdventuresItems {
    public static final Item ASTROLABE = new AstrolabeItem(new CreoItemSettings());
    public static final Item JOURNAL = new JournalItem(new CreoItemSettings());

    public static final Item BEDFRAME = new BedItem(AdventuresBlocks.BEDFRAME, new CreoItemSettings());

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(Adventures.NAMESPACE, "astrolabe"), ASTROLABE);
        Registry.register(Registries.ITEM, new Identifier(Adventures.NAMESPACE, "journal"), JOURNAL);

        Registry.register(Registries.ITEM, new Identifier(Adventures.NAMESPACE, "bedframe"), BEDFRAME);
    }
}
