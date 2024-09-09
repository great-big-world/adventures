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

    /**
     * TODO: Lerp between nearby block positions based on player offset on center pos
     */
    public static float sampleLightAtIgnoreLeaves(World world, BlockPos center, LightType lightType) {
        int total = 0;
        int light = 0;

        for (BlockPos pos : BlockPos.iterate(center.add(-1, -1, -1), center.add(1, 2, 1))) {
            if (!world.getBlockState(pos).isOpaque()) {
                // get first non-opaque pos from top
                BlockPos y1 = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos);

                // get first non-opaque pos from pos
                int y2 = getLeavesYAbove(world, pos);

                // if they are the same, we can see sky and should add the light there
                // further ensure that we are below the top pos, and that the top pos is non-opaque
                if (y1.getY() == y2 && y1.getY() >= pos.getY() && !world.getBlockState(y1.down()).isOpaque()) {
                    light += world.getLightLevel(lightType, y1);
                } else light += world.getLightLevel(lightType, pos);

                ++total;
            }
        }

        // divide by 15 to get average
        return ((float) light / total) / 15f;
    }

    private static int getLeavesYAbove(World world, BlockPos pos) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        for (int i = mutable.getY(); i <= world.getHeight(); ++i) {
            mutable.setY(i);
            if (!world.getBlockState(mutable).isOpaque()) {
                return mutable.getY();
            }
        }
        return -1;
    }
}
