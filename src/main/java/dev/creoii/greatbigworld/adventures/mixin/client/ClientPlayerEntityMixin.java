package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.adventures.util.ExtendedHudPlayer;
import dev.creoii.greatbigworld.adventures.util.ItemInfoHud;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.level.Level;

@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin extends LivingEntity implements ExtendedHudPlayer {
    @Unique private Map<Type, ItemInfoHud> itemInfoHuds;

    protected ClientPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$initItemInfoHuds(Minecraft client, ClientLevel world, ClientPacketListener networkHandler, StatsCounter stats, ClientRecipeBook recipeBook, Input lastPlayerInput, boolean lastSprinting, CallbackInfo ci) {
        itemInfoHuds = ExtendedHudPlayer.DEFAULT;
    }

    @Override
    public Map<Type, ItemInfoHud> gbw$getItemInfoHuds() {
        return itemInfoHuds;
    }

    @Redirect(method = "shouldStopRunSprinting", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;horizontalCollision:Z", opcode = Opcodes.GETFIELD))
    private boolean gbw$allowSprintWhileClimbing(LocalPlayer instance) {
        return instance.horizontalCollision && !onClimbable();
    }
}
