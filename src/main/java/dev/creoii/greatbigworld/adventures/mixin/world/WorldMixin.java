package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.util.AllowDebugHud;
import dev.creoii.greatbigworld.adventures.util.ShowDeathCoordinates;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(World.class)
public class WorldMixin implements ShowDeathCoordinates, AllowDebugHud {
    @Unique
    private boolean gbw$showDeathCoordinates;
    @Unique
    private boolean gbw$allowDebugHud;

    @Override
    public void gbw$setShowDeathCoordinates(boolean showDeathCoordinates) {
        gbw$showDeathCoordinates = showDeathCoordinates;
    }

    @Override
    public boolean gbw$shouldShowDeathCoordinates() {
        return gbw$showDeathCoordinates;
    }

    @Override
    public void gbw$setAllowDebugHud(boolean allowDebugHud) {
        gbw$allowDebugHud = allowDebugHud;
    }

    @Override
    public boolean gbw$shouldAllowDebugHud() {
        return gbw$allowDebugHud;
    }
}
