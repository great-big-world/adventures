package dev.creoii.greatbigworld.adventures;

import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import dev.creoii.greatbigworld.adventures.util.ExtendedHudPlayer;
import dev.creoii.greatbigworld.adventures.util.ItemInfoHud;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.TypedActionResult;

public class Adventures implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";

    @Override
    public void onInitialize() {
        AdventuresItems.register();

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient && !player.isSpectator() && player instanceof ExtendedHudPlayer extendedHudPlayer) {
                ItemStack stack = player.getStackInHand(hand);
                if (stack.isOf(Items.RECOVERY_COMPASS)) {
                    ItemInfoHud itemInfoHud = extendedHudPlayer.gbw$getItemInfoHuds().get(Items.RECOVERY_COMPASS);
                    itemInfoHud.invert();
                    extendedHudPlayer.gbw$getItemInfoHuds().forEach((key, value) -> System.out.println(key.getTranslationKey() + ": " + value.isActive()));
                    return TypedActionResult.success(stack);
                } else if (stack.isOf(Items.CLOCK)) {
                    ItemInfoHud itemInfoHud = extendedHudPlayer.gbw$getItemInfoHuds().get(Items.CLOCK);
                    itemInfoHud.invert();
                    extendedHudPlayer.gbw$getItemInfoHuds().forEach((key, value) -> System.out.println(key.getTranslationKey() + ": " + value.isActive()));
                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(ItemStack.EMPTY);
        });
    }
}
