package dev.creoii.greatbigworld.adventures.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record BonusHouseFeatureConfig(boolean hasChest) implements FeatureConfiguration {
    public static final Codec<BonusHouseFeatureConfig> CODEC = Codec.BOOL.fieldOf("chest").codec().xmap(BonusHouseFeatureConfig::new, BonusHouseFeatureConfig::hasChest);
}
