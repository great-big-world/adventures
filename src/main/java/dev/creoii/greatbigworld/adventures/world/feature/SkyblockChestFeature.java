package dev.creoii.greatbigworld.adventures.world.feature;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SkyblockChestFeature extends Feature<NoneFeatureConfiguration> {
    public SkyblockChestFeature(Codec<NoneFeatureConfiguration> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        context.level().setBlock(context.origin(), Blocks.CHEST.defaultBlockState(), 2);
        RandomizableContainer.setBlockEntityLootTable(context.level(), context.random(), context.origin(), ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "chests/skyblock")));
        return true;
    }
}
