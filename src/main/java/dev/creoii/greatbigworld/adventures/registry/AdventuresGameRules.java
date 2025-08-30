package dev.creoii.greatbigworld.adventures.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public final class AdventuresGameRules {
    public static GameRules.Key<GameRules.BooleanRule> SHOW_COORDINATES_ON_DEATH;
    public static GameRules.Key<GameRules.BooleanRule> SLEEP_DURING_DAY;
    public static GameRules.Key<GameRules.BooleanRule> ALLOW_DEBUG_HUD;

    public static void register() {
        SHOW_COORDINATES_ON_DEATH = GameRuleRegistry.register("showCoordinatesOnDeath", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
        SLEEP_DURING_DAY = GameRuleRegistry.register("sleepDuringDay", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
        ALLOW_DEBUG_HUD = GameRuleRegistry.register("allowDebugHud", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
    }
}
