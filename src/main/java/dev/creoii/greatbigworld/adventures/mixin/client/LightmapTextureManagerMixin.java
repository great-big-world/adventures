package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.adventures.util.UndergroundHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightTexture.class)
public class LightmapTextureManagerMixin {
    @Shadow @Final private Minecraft minecraft;
    @Unique private final float[] MOON_PHASE_BRIGHTNESS = {0f, -.035f, -.07f, -.105f, -.15f, -.105f, -.07f, -.035f};

    @WrapOperation(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LightTexture;calculateDarknessScale(Lnet/minecraft/world/entity/LivingEntity;FF)F"))
    private float gbw$modifyDarknessForMoonPhase(LightTexture instance, LivingEntity entity, float factor, float delta, Operation<Float> original) {
        float darkness = original.call(instance, entity, factor, delta);
        if (minecraft.level != null && minecraft.player != null && !minecraft.player.isSpectator()) {
            int moonPhase = minecraft.level.environmentAttributes().getDimensionValue(EnvironmentAttributes.MOON_PHASE).index();
            if (moonPhase == 3 || moonPhase == 5) {
                return -.105f * getTimeInfluence();
            }

            float abovegroundDarkness = darkness - (MOON_PHASE_BRIGHTNESS[moonPhase] * getTimeInfluence());
            float undergroundDarkness = darkness + .105f;

            return Mth.lerp(UndergroundHelper.sampleLight(minecraft.level, entity.blockPosition(), LightLayer.SKY), undergroundDarkness, abovegroundDarkness);
        }
        return darkness;
    }

    @Unique
    private float getTimeInfluence() {
        long dayLength = 12000L;
        long nightLength = 10000L;
        long total = dayLength + nightLength;

        long timeOfDay = minecraft.level.getDayTime() % total;
        if (timeOfDay >= dayLength && timeOfDay < dayLength + (nightLength * .3d)) {
            return (float) (timeOfDay - dayLength) / 3000f;
        } else if (timeOfDay >= (nightLength * .3d) && timeOfDay < (total - 1000L)) {
            return 1f;
        } else if (timeOfDay >= (total - 2000L) || timeOfDay < 1000L) {
            if (timeOfDay >= (total - 2000L)) {
                return (float) (total - timeOfDay) / 3000f;
            } else return (float) (1000L - timeOfDay) / 3000f;
        }
        return 0f;
    }
}
