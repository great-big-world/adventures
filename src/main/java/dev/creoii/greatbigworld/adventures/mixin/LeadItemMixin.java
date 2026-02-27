package dev.creoii.greatbigworld.adventures.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.adventures.util.AdventuresTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LeadItem.class)
public class LeadItemMixin {
    @WrapOperation(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean gbw$allowLeadsOnMoreBlocks(BlockState instance, TagKey<Block> tagKey, Operation<Boolean> original) {
        return instance.is(AdventuresTags.LEASHABLE);
    }
}
