package dev.creoii.greatbigworld.adventures.util;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.util.OptionsAPI;
import net.minecraft.client.OptionInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public final class UndergroundHelper {
    private static final int SEARCH_RADIUS = 1;
    private static final Identifier DYNAMIC_DARKNESS_OPTION_ID = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "dynamic_darkness_quality");

    /**
     * @return A float from 0-1 determining how much skylight is around the center position.
     */
    public static float sampleLight(Level world, BlockPos center, LightLayer lightType) {
        int total = 0;
        int lightSum = 0;

        int searchRadius = SEARCH_RADIUS;
        if (world.isClientSide()) {
            @SuppressWarnings("unchecked")
            OptionInstance<DynamicDarknessQuality> optionInstance = (OptionInstance<DynamicDarknessQuality>) OptionsAPI.getOption(DYNAMIC_DARKNESS_OPTION_ID);
            searchRadius = optionInstance.get().getQuality();
        }

        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-searchRadius, -searchRadius, -searchRadius), center.offset(searchRadius, searchRadius, searchRadius))) {
            int opacity = world.getBlockState(pos).getLightBlock();

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
