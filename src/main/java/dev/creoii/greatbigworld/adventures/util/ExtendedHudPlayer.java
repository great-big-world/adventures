package dev.creoii.greatbigworld.adventures.util;

import com.google.common.collect.ImmutableMap;
import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.Map;

public interface ExtendedHudPlayer {
    Map<Item, ItemInfoHud> DEFAULT = ImmutableMap.<Item, ItemInfoHud>builder()
            .put(Items.COMPASS, new ItemInfoHud())
            .put(Items.RECOVERY_COMPASS, new ItemInfoHud())
            .put(Items.CLOCK, new ItemInfoHud())
            .put(AdventuresItems.ASTROLABE, new ItemInfoHud())
            .build();

    Map<Item, ItemInfoHud> gbw$getItemInfoHuds();
}
