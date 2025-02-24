package dev.creoii.greatbigworld.adventures.mixin.entity;

import com.mojang.authlib.GameProfile;
import dev.creoii.greatbigworld.adventures.util.DifficultyHungerManager;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Shadow protected HungerManager hungerManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$changeStartHungerForDifficulty(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
        int startHunger = switch (world.getDifficulty()) {
            case PEACEFUL, EASY -> 20;
            case NORMAL -> 16;
            case HARD -> 12;
        };
        hungerManager = new DifficultyHungerManager(startHunger);
    }
}
