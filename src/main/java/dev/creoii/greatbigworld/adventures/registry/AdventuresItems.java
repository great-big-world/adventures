package dev.creoii.greatbigworld.adventures.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.item.AstrolabeItem;
import dev.creoii.greatbigworld.adventures.item.JournalItem;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.minecraft.item.BedItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public final class AdventuresItems {
    public static Item ASTROLABE;
    public static Item JOURNAL;

    public static Item BEDFRAME;

    public static void register() {
        ASTROLABE = RegistryHelper.registerItem(Identifier.of(GreatBigWorld.NAMESPACE, "astrolabe"), AstrolabeItem::new, new Item.Settings());
        JOURNAL = RegistryHelper.registerItem(Identifier.of(GreatBigWorld.NAMESPACE, "journal"), JournalItem::new, new Item.Settings());

        BEDFRAME = RegistryHelper.registerItem(Identifier.of(GreatBigWorld.NAMESPACE, "bedframe"), settings -> new BedItem(AdventuresBlocks.BEDFRAME, settings), new Item.Settings());
    }
}
