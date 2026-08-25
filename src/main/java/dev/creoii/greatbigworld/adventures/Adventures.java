package dev.creoii.greatbigworld.adventures;

import dev.creoii.greatbigworld.adventures.registry.AdventuresChunkGenerators;
import dev.creoii.greatbigworld.adventures.registry.AdventuresEvents;
import dev.creoii.greatbigworld.adventures.registry.AdventuresFeatures;
import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import net.fabricmc.api.ModInitializer;

public class Adventures implements ModInitializer {
    @Override
    public void onInitialize() {
        AdventuresFeatures.register();
        AdventuresGameRules.register();
        AdventuresChunkGenerators.register();
        AdventuresNetworking.register();
        AdventuresEvents.register();
    }
}
