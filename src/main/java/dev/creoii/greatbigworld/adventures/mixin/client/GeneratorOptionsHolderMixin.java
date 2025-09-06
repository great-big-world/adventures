package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.adventures.util.BonusHouseHolder;
import net.minecraft.client.world.GeneratorOptionsHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GeneratorOptionsHolder.class)
public class GeneratorOptionsHolderMixin implements BonusHouseHolder {
    @Unique
    private boolean bonusHouseEnabled = false;

    @Override
    public void gbw$setBonusHouseEnabled(boolean bonusHouseEnabled) {
        this.bonusHouseEnabled = bonusHouseEnabled;
    }

    @Override
    public boolean gbw$isBonusHouseEnabled() {
        return bonusHouseEnabled;
    }
}
