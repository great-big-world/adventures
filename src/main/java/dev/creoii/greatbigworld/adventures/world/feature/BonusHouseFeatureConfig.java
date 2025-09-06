package dev.creoii.greatbigworld.adventures.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.FeatureConfig;

public record BonusHouseFeatureConfig(boolean hasChest) implements FeatureConfig {
    public static final Codec<BonusHouseFeatureConfig> CODEC = Codec.BOOL.fieldOf("chest").codec().xmap(BonusHouseFeatureConfig::new, BonusHouseFeatureConfig::hasChest);
}
