package dev.creoii.greatbigworld.adventures.mixin.entity;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract void heal(float amount);
    @Shadow public abstract float getMaxHealth();

    @Inject(method = "wakeUp", at = @At("TAIL"))
    private void gbw$healOnWakeUp(CallbackInfo ci) {
        heal(getMaxHealth() / 5f);
    }
}
