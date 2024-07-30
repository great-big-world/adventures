package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldListWidget.WorldEntry.class)
public class WorldListWidgetWorldEntryMixin {
    @WrapOperation(method = "delete", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelSummary;getName()Ljava/lang/String;"))
    private String gbw$deleteGBWWorld(LevelSummary instance, Operation<String> original) {
        return "gbw/" + original.call(instance);
    }
}
