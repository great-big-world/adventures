package dev.creoii.greatbigworld.adventures.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.item.AstrolabeItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public final class AdventuresItems {
    public static Item ASTROLABE;

    public static void register() {
        ASTROLABE = RegistryHelper.registerItem(Identifier.of(GreatBigWorld.NAMESPACE, "astrolabe"), AstrolabeItem::new, new Item.Settings());
    }
}
