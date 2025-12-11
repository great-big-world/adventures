package dev.creoii.greatbigworld.adventures.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.world.feature.BonusHouseFeature;
import dev.creoii.greatbigworld.adventures.world.feature.BonusHouseFeatureConfig;
import dev.creoii.greatbigworld.adventures.world.feature.SkyblockChestFeature;
import dev.creoii.greatbigworld.adventures.world.feature.SkyblockIslandFeature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class AdventuresFeatures {
    public static final Feature<BonusHouseFeatureConfig> BONUS_HOUSE = new BonusHouseFeature(BonusHouseFeatureConfig.CODEC);
    public static final Feature<NoneFeatureConfiguration> SKYBLOCK_CHEST = new SkyblockChestFeature(NoneFeatureConfiguration.CODEC);
    public static final Feature<NoneFeatureConfiguration> SKYBLOCK_ISLAND = new SkyblockIslandFeature(NoneFeatureConfiguration.CODEC);

    public static void register() {
        Registry.register(BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "bonus_house"), BONUS_HOUSE);
        Registry.register(BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "skyblock_chest"), SKYBLOCK_CHEST);
        Registry.register(BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "skyblock_island"), SKYBLOCK_ISLAND);
    }
}
