package dev.creoii.greatbigworld.adventures.mixin;

import com.mojang.authlib.GameProfile;
import dev.creoii.greatbigworld.adventures.util.ExtendedHudPlayer;
import dev.creoii.greatbigworld.adventures.util.ItemInfoHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements ExtendedHudPlayer {
    @Unique private Map<Item, ItemInfoHud> itemInfoHuds;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$initItemInfoHuds(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
        itemInfoHuds = ExtendedHudPlayer.DEFAULT;
    }

    @Override
    public Map<Item, ItemInfoHud> gbw$getItemInfoHuds() {
        return itemInfoHuds;
    }
}
