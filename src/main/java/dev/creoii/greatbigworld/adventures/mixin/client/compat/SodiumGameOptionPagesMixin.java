package dev.creoii.greatbigworld.adventures.mixin.client.compat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.util.DynamicDarknessQuality;
import dev.creoii.greatbigworld.util.OptionsAPI;
import net.caffeinemc.mods.sodium.api.config.StorageEventHandler;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.EnumOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionGroupBuilder;
import net.caffeinemc.mods.sodium.client.gui.SodiumConfigBuilder;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SodiumConfigBuilder.class)
public class SodiumGameOptionPagesMixin {
    @Shadow
    @Final
    private StorageEventHandler vanillaStorage;

    @ModifyExpressionValue(method = "buildQualityPage", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/api/config/structure/OptionGroupBuilder;addOption(Lnet/caffeinemc/mods/sodium/api/config/structure/OptionBuilder;)Lnet/caffeinemc/mods/sodium/api/config/structure/OptionGroupBuilder;", ordinal = 1), remap = false)
    private OptionGroupBuilder gbw$addSodiumSeasonTransitionQualityOption(OptionGroupBuilder original, @Local(argsOnly = true) ConfigBuilder builder) {
        return original.addOption(builder.createEnumOption(Identifier.fromNamespaceAndPath("sodium", "dynamic_darkness_quality"), DynamicDarknessQuality.class).setName(Component.translatable("options.dynamicDarknessQuality")).setTooltip(Component.empty()).setElementNameProvider(EnumOptionBuilder.nameProviderFrom(DynamicDarknessQuality.NAMES)).setBinding(quality -> {}, () -> {
            @SuppressWarnings("unchecked")
            OptionInstance<DynamicDarknessQuality> optionInstance = (OptionInstance<DynamicDarknessQuality>) OptionsAPI.getOption(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "dynamic_darkness_quality"));
            return optionInstance.get();
        }).setStorageHandler(vanillaStorage).setDefaultValue(DynamicDarknessQuality.NORMAL).setImpact(OptionImpact.LOW));
    }
}
