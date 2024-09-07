package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public final class UndergroundHelper {
    /**
     * @return A float from 0-1 determining how much skylight is around the center position.
     */
    public static float sampleLightAt(World world, BlockPos center, LightType lightType) {
        int total = 0;
        int light = 0;

        for (BlockPos pos : BlockPos.iterate(center.add(-1, -1, -1), center.add(1, 2, 1))) {
            if (!world.getBlockState(pos).isOpaque()) {
                light += world.getLightLevel(lightType, pos);
                ++total;
            }
        }

        return ((float) light / total) / 15f;
    }

    public static float sampleLightAtIgnoreLeaves(World world, BlockPos center, LightType lightType) {
        int total = 0;
        int light = 0;

        for (BlockPos pos : BlockPos.iterate(center.add(-1, -1, -1), center.add(1, 2, 1))) {
            if (!world.getBlockState(pos).isOpaque()) {
                BlockPos y = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos);
                if (y.getY() >= pos.getY() && world.getBlockState(y.down()).isIn(BlockTags.LEAVES)) {
                    light += world.getLightLevel(lightType, y);
                } else light += world.getLightLevel(lightType, pos);
                ++total;
            }
        }

        return ((float) light / total) / 15f;
    }
}
