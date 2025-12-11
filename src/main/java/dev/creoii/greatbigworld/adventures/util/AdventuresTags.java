package dev.creoii.greatbigworld.adventures.util;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class AdventuresTags {
    public static final TagKey<Item> INFO_HUD_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "info_hud_items"));
}
