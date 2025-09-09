package dev.creoii.greatbigworld.adventures.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.LootableInventory;
import net.minecraft.loot.LootTables;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SkyblockChestFeature extends Feature<DefaultFeatureConfig> {
    public SkyblockChestFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        context.getWorld().setBlockState(context.getOrigin(), Blocks.CHEST.getDefaultState(), 2);
        LootableInventory.setLootTable(context.getWorld(), context.getRandom(), context.getOrigin(), LootTables.SPAWN_BONUS_CHEST);
        return true;
    }
}
