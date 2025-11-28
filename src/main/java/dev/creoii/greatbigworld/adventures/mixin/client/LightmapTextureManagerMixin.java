package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.adventures.client.AdventuresClient;
import dev.creoii.greatbigworld.adventures.util.UndergroundHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Shadow @Final private MinecraftClient client;
    @Unique private final float[] MOON_PHASE_BRIGHTNESS = {0f, -.035f, -.07f, -.105f, -.15f, -.105f, -.07f, -.035f};

    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/LightmapTextureManager;getDarkness(Lnet/minecraft/entity/LivingEntity;FF)F"))
    private float gbw$modifyDarknessForMoonPhase(LightmapTextureManager instance, LivingEntity entity, float factor, float delta, Operation<Float> original) {
        float darkness = original.call(instance, entity, factor, delta);
        if (client.world != null && client.player != null && !client.player.isSpectator()) {
            int moonPhase = client.world.getMoonPhase();
            if (moonPhase == 3 || moonPhase == 5) {
                return -.105f * getTimeInfluence();
            }

            float abovegroundDarkness = darkness - (MOON_PHASE_BRIGHTNESS[moonPhase] * getTimeInfluence());
            float undergroundDarkness = darkness + .105f;

            return MathHelper.lerp(UndergroundHelper.sampleLight(client.world, entity.getBlockPos(), LightType.SKY), undergroundDarkness, abovegroundDarkness);
        }
        return darkness;
    }

    @Unique
    private float getTimeInfluence() {
        long dayLength = AdventuresClient.dayLength;
        long nightLength = AdventuresClient.nightLength;
        long total = dayLength + nightLength;

        long timeOfDay = client.world.getTimeOfDay() % total;
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
