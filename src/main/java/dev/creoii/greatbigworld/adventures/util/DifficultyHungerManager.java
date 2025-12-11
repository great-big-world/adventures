package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.world.food.FoodData;

public class DifficultyHungerManager extends FoodData {
    public DifficultyHungerManager(int startHunger) {
        foodLevel = startHunger;
    }
}
