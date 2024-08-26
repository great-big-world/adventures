package dev.creoii.greatbigworld.adventures.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public final class AdventuresGameRules {
    public static GameRules.Key<GameRules.BooleanRule> SHOW_COORDINATES_ON_DEATH;

    public static void register() {
        SHOW_COORDINATES_ON_DEATH = GameRuleRegistry.register("showCoordinatesOnDeath", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true));
    }
}
