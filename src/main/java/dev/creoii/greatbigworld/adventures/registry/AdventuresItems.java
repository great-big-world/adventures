package dev.creoii.greatbigworld.adventures.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.item.AstrolabeItem;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public final class AdventuresItems {
    public static Item ASTROLABE;

    public static void register() {
        ASTROLABE = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "astrolabe"), AstrolabeItem::new, new Item.Properties());
    }
}
