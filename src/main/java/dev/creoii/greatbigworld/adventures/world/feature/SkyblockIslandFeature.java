package dev.creoii.greatbigworld.adventures.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SkyblockIslandFeature extends Feature<NoneFeatureConfiguration> {
    public SkyblockIslandFeature(Codec<NoneFeatureConfiguration> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos.MutableBlockPos posA = new BlockPos(0, 62, 0).mutable();
        BlockPos.MutableBlockPos posB = new BlockPos(3, 62, 0).mutable();
        BlockPos.MutableBlockPos posC = new BlockPos(0, 62, 3).mutable();

        BlockPos.MutableBlockPos[] sections = new BlockPos.MutableBlockPos[]{posA, posB, posC};

        for (BlockPos.MutableBlockPos mutable : sections) {
            int baseX = mutable.getX();
            int baseY = mutable.getY();
            int baseZ = mutable.getZ();

            for (int z = -1; z <= 1; ++z) {
                for (int y = -1; y <= 1; ++y) {
                    for (int x = -1; x <= 1; ++x) {
                        BlockPos pos = new BlockPos(baseX + x, baseY + y, baseZ + z);
                        BlockState state = y == 1 ? Blocks.GRASS_BLOCK.defaultBlockState() : Blocks.DIRT.defaultBlockState();

                        context.level().setBlock(pos, state, 2);
                    }
                }
            }
        }

        return true;
    }
}
