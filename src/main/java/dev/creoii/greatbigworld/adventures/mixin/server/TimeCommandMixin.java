package dev.creoii.greatbigworld.adventures.mixin.server;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.context.CommandContext;
import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TimeCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TimeCommand.class)
public class TimeCommandMixin {
    @ModifyConstant(method = "getDayTime", constant = @Constant(longValue = 24000L))
    private static long gbw$overrideDayTime(long constant, @Local(argsOnly = true) ServerWorld world) {
        GameRules gameRules = world.getGameRules();
        return gameRules.getInt(AdventuresGameRules.LENGTH_OF_DAY) + gameRules.getInt(AdventuresGameRules.LENGTH_OF_NIGHT);
    }

    @ModifyConstant(method = "method_13794", constant = @Constant(intValue = 6000))
    private static int gbw$overrideNoonValue(int constant, @Local(argsOnly = true) CommandContext<ServerCommandSource> context) {
        GameRules gameRules = context.getSource().getWorld().getGameRules();
        return gameRules.getInt(AdventuresGameRules.LENGTH_OF_DAY) / 2;
    }

    @ModifyConstant(method = "method_13797", constant = @Constant(intValue = 13000))
    private static int gbw$overrideNightValue(int constant, @Local(argsOnly = true) CommandContext<ServerCommandSource> context) {
        GameRules gameRules = context.getSource().getWorld().getGameRules();
        return gameRules.getInt(AdventuresGameRules.LENGTH_OF_DAY) + 1000;
    }

    @ModifyConstant(method = "method_13785", constant = @Constant(intValue = 18000))
    private static int gbw$overrideMidnightValue(int constant, @Local(argsOnly = true) CommandContext<ServerCommandSource> context) {
        GameRules gameRules = context.getSource().getWorld().getGameRules();
        return gameRules.getInt(AdventuresGameRules.LENGTH_OF_DAY) + (gameRules.getInt(AdventuresGameRules.LENGTH_OF_DAY) / 2);
    }
}
