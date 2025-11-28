package dev.creoii.greatbigworld.adventures.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.context.CommandContext;
import dev.creoii.greatbigworld.event.GameRuleEvents;
import net.minecraft.server.command.GameRuleCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRuleCommand.class)
public class GameRuleCommandMixin {
    @Inject(method = "executeSet", at = @At("RETURN"))
    private static <T extends GameRules.Rule<T>> void gbw$gameRuleEvent(CommandContext<ServerCommandSource> context, GameRules.Key<T> key, CallbackInfoReturnable<Integer> cir, @Local T rule) {
        GameRuleEvents.SET.invoker().set(key, context.getSource().getWorld());
    }
}
