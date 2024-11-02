package dev.creoii.greatbigworld.adventures.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.Adventures;
import dev.creoii.greatbigworld.adventures.item.AstrolabeItem;
import dev.creoii.greatbigworld.adventures.item.JournalItem;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.BedItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class AdventuresItems {
    public static final Item ASTROLABE = new AstrolabeItem(new Item.Settings());
    public static final Item JOURNAL = new JournalItem(new Item.Settings());

    public static final Item BEDFRAME = new BedItem(AdventuresBlocks.BEDFRAME, new Item.Settings());

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(GreatBigWorld.NAMESPACE, "astrolabe"), ASTROLABE);
        Registry.register(Registries.ITEM, new Identifier(GreatBigWorld.NAMESPACE, "journal"), JOURNAL);

        Registry.register(Registries.ITEM, new Identifier(GreatBigWorld.NAMESPACE, "bedframe"), BEDFRAME);

        FuelRegistry.INSTANCE.add(BEDFRAME, 900);
    }
}
