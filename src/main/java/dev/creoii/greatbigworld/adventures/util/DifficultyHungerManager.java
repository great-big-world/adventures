package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.entity.player.HungerManager;

public class DifficultyHungerManager extends HungerManager {
    public DifficultyHungerManager(int startHunger) {
        foodLevel = startHunger;
        prevFoodLevel = startHunger;
    }
}
