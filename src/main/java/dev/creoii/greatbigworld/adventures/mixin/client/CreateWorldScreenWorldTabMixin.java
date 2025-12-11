package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.util.BonusHouseHolder;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.SwitchGrid;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.WorldTab.class)
public class CreateWorldScreenWorldTabMixin {
    @Unique
    private static final Component BONUS_HOUSE_TEXT = Component.translatable("great_big_world.selectWorld.bonusHouse");

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 8))
    private int gbw$overrideWorldTabRowSpacing(int constant) {
        return 4;
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$Builder;build()Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid;"))
    private void gbw$modifyBonusChestButton(CreateWorldScreen createWorldScreen, CallbackInfo ci, @Local SwitchGrid.Builder builder) {
        WorldCreationUiState worldCreator = createWorldScreen.getUiState();
        if (worldCreator instanceof BonusHouseHolder bonusHouseHolder) {
            builder.addSwitch(BONUS_HOUSE_TEXT, bonusHouseHolder::gbw$isBonusHouseEnabled, bonusHouseHolder::gbw$setBonusHouseEnabled).withIsActiveCondition(() -> !worldCreator.isHardcore() && !worldCreator.isDebug());
        }
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$Builder;build()Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid;"))
    private SwitchGrid gbw$decreaseWorldGridRowSpacing(SwitchGrid.Builder instance) {
        return instance.withRowSpacing(1).build();
    }
}
