package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getNightVisionStrength", at = @At("RETURN"), cancellable = true)
    private static void gbw$amplifierAffectsNightVision(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> cir, @Local StatusEffectInstance statusEffectInstance) {
        float a = Math.min(.2f * (entity.getStatusEffect(StatusEffects.NIGHT_VISION).getAmplifier() + 1), 5f);
        cir.setReturnValue(!statusEffectInstance.isDurationBelow(200) ? a : (a * .7f) + MathHelper.sin((float) ((statusEffectInstance.getDuration() - tickDelta) * Math.PI * .2f)) * .3f);
    }
}
