package dev.creoii.greatbigworld.adventures.mixin;

import dev.creoii.greatbigworld.adventures.util.ExtendedHudPlayer;
import dev.creoii.greatbigworld.adventures.util.ItemInfoHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CompassItem.class)
public abstract class CompassItemMixin extends Item implements Vanishable {
    public CompassItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient && user instanceof ExtendedHudPlayer extendedHudPlayer) {
            ItemInfoHud itemInfoHud = extendedHudPlayer.gbw$getItemInfoHuds().get(Items.COMPASS);
            itemInfoHud.invert();
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }
}
