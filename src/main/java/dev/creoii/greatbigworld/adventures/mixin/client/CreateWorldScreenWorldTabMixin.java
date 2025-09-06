package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.adventures.util.BonusHouseHolder;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.gui.screen.world.WorldScreenOptionGrid;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.WorldTab.class)
public class CreateWorldScreenWorldTabMixin {
    @Unique
    private static final Text BONUS_HOUSE_TEXT = Text.translatable("great_big_world.selectWorld.bonusHouse");

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 8))
    private int gbw$overrideWorldTabRowSpacing(int constant) {
        return 4;
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/WorldScreenOptionGrid$Builder;build()Lnet/minecraft/client/gui/screen/world/WorldScreenOptionGrid;"))
    private void gbw$modifyBonusChestButton(CreateWorldScreen createWorldScreen, CallbackInfo ci, @Local WorldScreenOptionGrid.Builder builder) {
        WorldCreator worldCreator = createWorldScreen.getWorldCreator();
        if (worldCreator instanceof BonusHouseHolder bonusHouseHolder) {
            builder.add(BONUS_HOUSE_TEXT, bonusHouseHolder::gbw$isBonusHouseEnabled, bonusHouseHolder::gbw$setBonusHouseEnabled).toggleable(() -> !worldCreator.isHardcore() && !worldCreator.isDebug());
        }
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/WorldScreenOptionGrid$Builder;build()Lnet/minecraft/client/gui/screen/world/WorldScreenOptionGrid;"))
    private WorldScreenOptionGrid gbw$decreaseWorldGridRowSpacing(WorldScreenOptionGrid.Builder instance) {
        return instance.setRowSpacing(1).build();
    }
}
