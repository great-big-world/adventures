package dev.creoii.greatbigworld.adventures.mixin.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Shadow public abstract ServerLevel level();

    @Unique private static final MutableComponent UNKNOWN_PLAYER_DIED_AT = Component.translatable("death.unknown");

    @SuppressWarnings("unchecked") // shut up
    @WrapOperation(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/gamerules/GameRules;get(Lnet/minecraft/world/level/gamerules/GameRule;)Ljava/lang/Object;", ordinal = 0))
    private <T> T gbw$allowEither(GameRules instance, GameRule<T> gameRule, Operation<Boolean> original) {
        return (T) (Object) (instance.get(GameRules.SHOW_DEATH_MESSAGES) || instance.get(AdventuresGameRules.SHOW_DEATH_COORDINATES));
    }

    @WrapOperation(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatTracker;getDeathMessage()Lnet/minecraft/network/chat/Component;"))
    private Component gwb$possiblyShowDeathCoordinates(CombatTracker instance, Operation<Component> original) {
        boolean showDeathMessages = level().getGameRules().get(GameRules.SHOW_DEATH_MESSAGES);
        boolean showDeathCoordinates = level().getGameRules().get(AdventuresGameRules.SHOW_DEATH_COORDINATES);

        if (showDeathMessages && !showDeathCoordinates)
            return original.call(instance);
        else if (showDeathCoordinates && !showDeathMessages)
             return UNKNOWN_PLAYER_DIED_AT.append(Component.translatable("death.showCoordinates", blockPosition().toShortString()));
        else
             return original.call(instance).copy().append(Component.translatable("death.showCoordinates", blockPosition().toShortString()));
    }
}
