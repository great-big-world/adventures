package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.block.BlockState;
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

    public static float sampleLightAtIgnoreNonOpaque(World world, BlockPos center, LightType lightType) {
        int total = 0;
        int light = 0;

        for (BlockPos pos : BlockPos.iterate(center.add(-1, -1, -1), center.add(1, 2, 1))) {
            if (!world.getBlockState(pos).isOpaqueFullCube(world, pos)) {
                int add = world.getLightLevel(lightType, pos);

                if (add == 15) {
                    light += 15;
                    ++total;
                    continue;
                }

                BlockPos top = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos);
                if (!world.getBlockState(top).isOpaqueFullCube(world, top) && isNonOpaqueBetween(world, pos, top)) {
                    light += 15;
                } else light += add;

                // only count blocks that accept light
                ++total;
            }
        }

        // average of all light values / 15f to normalize
        return ((float) light / total) / 15f;
    }

    public static boolean isNonOpaqueBetween(World world, BlockPos bottom, BlockPos top) {
        if (top.getY() - bottom.getY() <= 1)
            return world.getBlockState(bottom).isOpaqueFullCube(world, bottom);
        BlockPos.Mutable mutable = bottom.mutableCopy();
        for (int y = bottom.getY(); y <= top.getY(); ++y) {
            mutable.setY(y);
            BlockState state = world.getBlockState(mutable);
            if (state.isAir())
                continue;
            if (state.isOpaqueFullCube(world, mutable))
                return false;
        }
        return true;
    }
}
