package dev.creoii.greatbigworld.adventures.registry;

import dev.creoii.creoapi.api.item.CreoItemSettings;
import dev.creoii.greatbigworld.adventures.Adventures;
import dev.creoii.greatbigworld.adventures.item.AstrolabeItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class AdventuresItems {
    public static final Item ASTROLABE = new AstrolabeItem(new CreoItemSettings());

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(Adventures.NAMESPACE, "astrolabe"), ASTROLABE);
    }
}
