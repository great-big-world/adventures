package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Shadow @Final private MinecraftClient client;

    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/LightmapTextureManager;getDarkness(Lnet/minecraft/entity/LivingEntity;FF)F"))
    private float gbw$modifyDarknessForMoonPhase(LightmapTextureManager instance, LivingEntity entity, float factor, float delta, Operation<Float> original) {
        float darkness = original.call(instance, entity, factor, delta);
        if (client.world != null) {
            float[] brightness = {0f, -.0375f, -.075f, -.1125f, -.15f, -.1125f, -.075f, -.0375f};
            return darkness - brightness[client.world.getMoonPhase()] * getTimeInfluence();
        }
        return darkness;
    }

    @Unique
    private float getTimeInfluence() {
        long timeOfDay = client.world.getTimeOfDay() % 24000L;

        if (timeOfDay >= 12000L && timeOfDay < 15000L) {
            return (float) (timeOfDay - 12000L) / 3000f;
        } else if (timeOfDay >= 15000L && timeOfDay < 23000L) {
            return 1f;
        } else if (timeOfDay >= 22000L || timeOfDay < 1000L) {
            if (timeOfDay >= 22000L) {
                return (float) (24000L - timeOfDay) / 3000f;
            } else return (float) (1000L - timeOfDay) / 3000f;
        }
        return 0f;
    }
}
