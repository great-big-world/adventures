package dev.creoii.greatbigworld.adventures;

import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import net.fabricmc.api.ModInitializer;

public class Adventures implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";

    @Override
    public void onInitialize() {
        AdventuresItems.register();
    }
}
