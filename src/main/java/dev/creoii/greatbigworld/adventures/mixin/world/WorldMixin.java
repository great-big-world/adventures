package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.util.ShowDeathCoordinates;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(World.class)
public class WorldMixin implements ShowDeathCoordinates {
    @Unique
    private boolean gbw$showDeathCoordinates;

    @Override
    public void gbw$setShowDeathCoordinates(boolean showDeathCoordinates) {
        gbw$showDeathCoordinates = showDeathCoordinates;
    }

    @Override
    public boolean gbw$shouldShowDeathCoordinates() {
        return gbw$showDeathCoordinates;
    }
}
