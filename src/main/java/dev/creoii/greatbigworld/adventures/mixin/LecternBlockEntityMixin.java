package dev.creoii.greatbigworld.adventures.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.creoii.greatbigworld.adventures.registry.AdventuresItems;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LecternBlockEntity.class)
public class LecternBlockEntityMixin {
    @Shadow ItemStack book;

    @ModifyExpressionValue(method = "hasBook", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z", ordinal = 0))
    private boolean gbw$lecternHasJournalBook(boolean original) {
        return original || book.isOf(AdventuresItems.JOURNAL);
    }
}
