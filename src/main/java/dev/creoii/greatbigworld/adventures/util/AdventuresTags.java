package dev.creoii.greatbigworld.adventures.util;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class AdventuresTags {
    public static final TagKey<Item> INFO_HUD_ITEMS = TagKey.of(RegistryKeys.ITEM, Identifier.of(GreatBigWorld.NAMESPACE, "info_hud_items"));
}
