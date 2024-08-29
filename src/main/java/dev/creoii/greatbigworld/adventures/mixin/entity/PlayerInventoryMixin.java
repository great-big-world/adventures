package dev.creoii.greatbigworld.adventures.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.adventures.registry.AdventuresGameRules;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @WrapOperation(method = "dropAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;"))
    private ItemEntity gbw$applyItemDespawnTimeOnDeathGameRule(PlayerEntity instance, ItemStack stack, boolean throwRandomly, boolean retainOwnership, Operation<ItemEntity> original) {
        ItemEntity itemEntity = original.call(instance, stack, throwRandomly, retainOwnership);
        int itemDespawnTimeOnDeath = instance.getWorld().getGameRules().getInt(AdventuresGameRules.ITEM_DESPAWN_TIME_ON_DEATH);
        if (itemDespawnTimeOnDeath <= -32768) {
            itemEntity.setNeverDespawn();
        } else itemEntity.itemAge = itemDespawnTimeOnDeath + 6000;
        return itemEntity;
    }
}
