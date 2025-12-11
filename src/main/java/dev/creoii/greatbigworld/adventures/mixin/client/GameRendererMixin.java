package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getNightVisionScale", at = @At("RETURN"), cancellable = true)
    private static void gbw$amplifierAffectsNightVision(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> cir, @Local MobEffectInstance statusEffectInstance) {
        if (!entity.isSpectator()) {
            float a = Math.min(.2f * (entity.getEffect(MobEffects.NIGHT_VISION).getAmplifier() + 1), 5f);
            cir.setReturnValue(!statusEffectInstance.endsWithin(200) ? a : (a * .7f) + Mth.sin((float) ((statusEffectInstance.getDuration() - tickDelta) * Math.PI * .2f)) * .3f);
        }
    }
}
