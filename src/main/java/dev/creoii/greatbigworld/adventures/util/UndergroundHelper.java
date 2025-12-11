package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

public final class UndergroundHelper {
    /**
     * @return A float from 0-1 determining how much skylight is around the center position.
     */
    public static float sampleLight(Level world, BlockPos center, LightLayer lightType) {
        int total = 0;
        int lightSum = 0;

        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-1, -1, -1), center.offset(1, 2, 1))) {
            BlockState state = world.getBlockState(pos);

            int opacity = state.getLightBlock();

            if (opacity >= 15)
                continue;

            int sample = world.getBrightness(lightType, pos);
            if (lightType == LightLayer.SKY) {
                sample += Math.clamp(15 - opacity, 0, 15);
            }

            lightSum += sample;
            total++;
        }

        return total > 0 ? (lightSum / (float) total) / 15f : 0f;
    }
}
