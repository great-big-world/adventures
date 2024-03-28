package dev.creoii.greatbigworld.adventures.mixin;

import dev.creoii.greatbigworld.adventures.util.ExtendedLevelProperties;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin implements ExtendedLevelProperties {
    @Unique
    private int worldSize = -1;

    @Override
    public void gbw$setWorldSize(int worldSize) {
        this.worldSize = worldSize;
    }

    @Override
    public int gbw$getWorldSize() {
        return worldSize;
    }
}
