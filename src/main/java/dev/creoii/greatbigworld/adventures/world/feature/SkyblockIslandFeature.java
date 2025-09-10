package dev.creoii.greatbigworld.adventures.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SkyblockIslandFeature extends Feature<DefaultFeatureConfig> {
    public SkyblockIslandFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos.Mutable posA = new BlockPos(0, 62, 0).mutableCopy();
        BlockPos.Mutable posB = new BlockPos(3, 62, 0).mutableCopy();
        BlockPos.Mutable posC = new BlockPos(0, 62, 3).mutableCopy();

        BlockPos.Mutable[] sections = new BlockPos.Mutable[]{posA, posB, posC};

        for (BlockPos.Mutable mutable : sections) {
            int baseX = mutable.getX();
            int baseY = mutable.getY();
            int baseZ = mutable.getZ();

            for (int z = -1; z <= 1; ++z) {
                for (int y = -1; y <= 1; ++y) {
                    for (int x = -1; x <= 1; ++x) {
                        BlockPos pos = new BlockPos(baseX + x, baseY + y, baseZ + z);
                        BlockState state = y == 1 ? Blocks.GRASS_BLOCK.getDefaultState() : Blocks.DIRT.getDefaultState();

                        context.getWorld().setBlockState(pos, state, 2);
                    }
                }
            }
        }

        return true;
    }
}
