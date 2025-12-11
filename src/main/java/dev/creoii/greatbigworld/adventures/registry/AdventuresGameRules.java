package dev.creoii.greatbigworld.adventures.registry;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

public final class AdventuresGameRules {
    public static GameRule<Boolean> SHOW_COORDINATES_ON_DEATH;
    public static GameRule<Boolean> SLEEP_DURING_DAY;
    public static GameRule<Boolean> ALLOW_DEBUG_HUD;

    public static void register() {
        SHOW_COORDINATES_ON_DEATH = Registry.register(BuiltInRegistries.GAME_RULE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "show_coordinates_on_death"), new GameRule<>(GameRuleCategory.PLAYER, GameRuleType.BOOL, BoolArgumentType.bool(), GameRuleTypeVisitor::visitBoolean, Codec.BOOL, value -> value ? 1 : 0, true, FeatureFlagSet.of()));
        SLEEP_DURING_DAY = Registry.register(BuiltInRegistries.GAME_RULE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "sleep_during_day"), new GameRule<>(GameRuleCategory.PLAYER, GameRuleType.BOOL, BoolArgumentType.bool(), GameRuleTypeVisitor::visitBoolean, Codec.BOOL, value -> value ? 1 : 0, false, FeatureFlagSet.of()));
        ALLOW_DEBUG_HUD = Registry.register(BuiltInRegistries.GAME_RULE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "allow_debug_hud"), new GameRule<>(GameRuleCategory.MISC, GameRuleType.BOOL, BoolArgumentType.bool(), GameRuleTypeVisitor::visitBoolean, Codec.BOOL, value -> value ? 1 : 0, true, FeatureFlagSet.of()));
    }
}
