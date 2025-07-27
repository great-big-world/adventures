package dev.creoii.greatbigworld.adventures.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.context.CommandContext;
import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import dev.creoii.greatbigworld.adventures.util.ShowDeathCoordinates;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.GameRuleCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRuleCommand.class)
public class GameRuleCommandMixin {
    @Inject(method = "executeSet", at = @At("RETURN"))
    private static <T extends GameRules.Rule<T>> void gbw$setClientGameRules(CommandContext<ServerCommandSource> context, GameRules.Key<T> key, CallbackInfoReturnable<Integer> cir, @Local T rule) {
        ServerWorld world = context.getSource().getWorld();
        if (world instanceof ShowDeathCoordinates showDeathCoordinates && key == AdventuresGameRules.SHOW_COORDINATES_ON_DEATH) {
            boolean value = world.getGameRules().getBoolean(AdventuresGameRules.SHOW_COORDINATES_ON_DEATH);
            showDeathCoordinates.gbw$setShowDeathCoordinates(value);
            PlayerLookup.all(context.getSource().getServer()).forEach(serverPlayer -> {
                ServerPlayNetworking.send(serverPlayer, new ShowDeathCoordinates.SyncS2C(value));
            });
        }
    }
}
