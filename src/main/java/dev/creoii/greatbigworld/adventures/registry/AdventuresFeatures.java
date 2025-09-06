package dev.creoii.greatbigworld.adventures.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.world.feature.BonusHouseFeature;
import dev.creoii.greatbigworld.adventures.world.feature.BonusHouseFeatureConfig;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public final class AdventuresFeatures {
    public static final Feature<BonusHouseFeatureConfig> BONUS_HOUSE = new BonusHouseFeature(BonusHouseFeatureConfig.CODEC);

    public static void register() {
        Registry.register(Registries.FEATURE, Identifier.of(GreatBigWorld.NAMESPACE, "bonus_house"), BONUS_HOUSE);
    }
}
