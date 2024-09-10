package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.*;

public final class UndergroundHelper {
    private static final BlockPos.Mutable MUTABLE = new BlockPos.Mutable();
    private static final Set<Integer> YS = new HashSet<>();

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
        for (BlockPos pos : BlockPos.iterate(center.add(-1, -1, -1), center.add(1, 2, 1))) {
            if (!world.getBlockState(pos).isOpaqueFullCube(world, pos)) {
                add = world.getLightLevel(lightType, pos);

                if (add == 15) {
                    light += 15;
                    ++total;
                    continue;
                }

                MUTABLE.set(world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos));
                if (!world.getBlockState(MUTABLE).isOpaqueFullCube(world, MUTABLE) && isNonOpaqueBetween(world, pos)) {
                    light += 15;
                } else light += add;

                // only count blocks that accept light
                ++total;
            }
        }
        
        // average of all light values / 15f to normalize
        return total > 0 ? ((float) light / total) / 15f : 0f;
    }

    public static boolean isNonOpaqueBetween(World world, BlockPos bottom) {
        if (MUTABLE.getY() - bottom.getY() <= 1)
            return world.getBlockState(bottom).isOpaqueFullCube(world, bottom);

        YS.clear();
        for (int y = MUTABLE.getY(); y >= bottom.getY(); y -= y < world.getSeaLevel() ? 2 : 1) {
            YS.add(y);
        }

        for (int y : YS) {
            MUTABLE.setY(y);
            BlockState state = world.getBlockState(MUTABLE);
            if (state.isAir())
                continue;
            if (state.isOpaqueFullCube(world, MUTABLE))
                return false;
        }
        return true;
    }
}
