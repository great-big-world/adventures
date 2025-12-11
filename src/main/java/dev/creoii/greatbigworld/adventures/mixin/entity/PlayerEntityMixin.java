package dev.creoii.greatbigworld.adventures.mixin.entity;

import com.mojang.authlib.GameProfile;
import dev.creoii.greatbigworld.adventures.util.DifficultyHungerManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerEntityMixin {
    @Shadow protected FoodData foodData;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$changeStartHungerForDifficulty(Level world, GameProfile profile, CallbackInfo ci) {
        int startHunger = switch (world.getDifficulty()) {
            case PEACEFUL, EASY -> 20;
            case NORMAL -> 16;
            case HARD -> 12;
        };
        foodData = new DifficultyHungerManager(startHunger);
    }
}
