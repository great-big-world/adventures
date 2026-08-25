package dev.creoii.greatbigworld.adventures.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract void heal(float amount);
    @Shadow public abstract float getMaxHealth();

    @Inject(method = "stopSleeping", at = @At("TAIL"))
    private void gbw$healOnWakeUp(CallbackInfo ci) {
        heal(getMaxHealth() / 5f);
    }

    @ModifyExpressionValue(method = "handleOnClimbable", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D"))
    private double gbw$handleClimbSprinting(double g) {
        LivingEntity living = (LivingEntity) (Object) this;

        Direction moveDir = g < 0d ? Direction.DOWN : Direction.UP;
        if (living.isSprinting() && moveDir == Direction.getFacingAxis(living, Direction.Axis.Y)) {
            g += moveDir == Direction.DOWN ? -.1d : .1d;
        }

        return g;
    }
}
