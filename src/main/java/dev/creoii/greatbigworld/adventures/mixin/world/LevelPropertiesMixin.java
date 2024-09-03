package dev.creoii.greatbigworld.adventures.mixin.world;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin implements WorldSizeHolder {
    @Unique private int worldSize = -1;

    @Override
    public void gbw$setWorldSize(int worldSize) {
        this.worldSize = worldSize;
    }

    @Override
    public int gbw$getWorldSize() {
        return worldSize;
    }

    @SuppressWarnings("deprecation")
    @Inject(method = "readProperties", at = @At("RETURN"))
    private static <T> void gbw$readExtendedProperties(Dynamic<T> dynamic, LevelInfo info, LevelProperties.SpecialProperty specialProperty, GeneratorOptions generatorOptions, Lifecycle lifecycle, CallbackInfoReturnable<LevelProperties> cir) {
        ((WorldSizeHolder) cir.getReturnValue()).gbw$setWorldSize(dynamic.get("worldSize").asInt(-1));
    }

    @Inject(method = "updateProperties", at = @At("TAIL"))
    private void gbw$updateExtendedProperties(DynamicRegistryManager registryManager, NbtCompound levelNbt, NbtCompound playerNbt, CallbackInfo ci) {
        levelNbt.putInt("worldSize", worldSize);
    }
}
