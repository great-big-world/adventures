package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.*;

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

        int add;
        BlockPos.Mutable top = new BlockPos.Mutable();
        List<Integer> ys = new ArrayList<>();
        for (BlockPos pos : BlockPos.iterate(center.add(-1, -1, -1), center.add(1, 2, 1))) {
            if (!world.getBlockState(pos).isOpaqueFullCube(world, pos)) {
                add = world.getLightLevel(lightType, pos);

                if (add == 15) {
                    light += 15;
                    ++total;
                    continue;
                }

                top.set(world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos));
                if (!world.getBlockState(top).isOpaqueFullCube(world, top) && isNonOpaqueBetween(world, pos, top, ys)) {
                    light += 15;
                } else light += add;

                // only count blocks that accept light
                ++total;
            }
        }

        // average of all light values / 15f to normalize
        return total > 0 ? ((float) light / total) / 15f : 0f;
    }

    public static boolean isNonOpaqueBetween(World world, BlockPos bottom, BlockPos.Mutable top, List<Integer> ys) {
        if (top.getY() - bottom.getY() <= 1)
            return world.getBlockState(bottom).isOpaqueFullCube(world, bottom);

        ys.clear();
        for (int y = top.getY(); y >= bottom.getY(); y -= y < world.getSeaLevel() ? 2 : 1) {
            ys.add(y);
        }

        Collections.shuffle(ys);
        for (int y : ys) {
            top.setY(y);
            BlockState state = world.getBlockState(top);
            if (state.isAir())
                continue;
            if (state.isOpaqueFullCube(world, top))
                return false;
        }
        return true;
    }
}
