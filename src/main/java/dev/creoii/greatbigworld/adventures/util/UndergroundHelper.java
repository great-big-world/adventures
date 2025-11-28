package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public final class UndergroundHelper {
    /**
     * @return A float from 0-1 determining how much skylight is around the center position.
     */
    public static float sampleLight(World world, BlockPos center, LightType lightType) {
        int total = 0;
        int lightSum = 0;

        for (BlockPos pos : BlockPos.iterate(center.add(-1, -1, -1), center.add(1, 2, 1))) {
            BlockState state = world.getBlockState(pos);

            int opacity = state.getOpacity();

            if (opacity >= 15)
                continue;

            int sample = world.getLightLevel(lightType, pos);
            if (lightType == LightType.SKY) {
                sample += Math.clamp(15 - opacity, 0, 15);
            }

            lightSum += sample;
            total++;
        }

        return total > 0 ? (lightSum / (float) total) / 15f : 0f;
    }
}
