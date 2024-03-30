package dev.creoii.greatbigworld.adventures.item;

import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import dev.creoii.greatbigworld.adventures.util.ExtendedHudPlayer;
import dev.creoii.greatbigworld.adventures.util.ItemInfoHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class AstrolabeItem extends Item {
    public AstrolabeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient && user instanceof ExtendedHudPlayer extendedHudPlayer) {
            ItemInfoHud itemInfoHud = extendedHudPlayer.gbw$getItemInfoHuds().get(AdventuresItems.ASTROLABE);
            itemInfoHud.invert();
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }
}
