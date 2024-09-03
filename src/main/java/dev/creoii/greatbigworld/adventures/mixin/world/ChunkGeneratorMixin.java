package dev.creoii.greatbigworld.adventures.mixin.world;

import dev.creoii.greatbigworld.adventures.util.WorldSizeHolder;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin implements WorldSizeHolder {
    @Unique private int worldSize = -1;

    @Override
    public void gbw$setWorldSize(int worldSize) {
        this.worldSize = worldSize;
    }

    @Override
    public int gbw$getWorldSize() {
        return worldSize;
    }

    @Inject(method = "generateFeatures", at = @At("HEAD"), cancellable = true)
    private void gbw$limitFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor, CallbackInfo ci) {
        if (WorldSizeHolder.isWithinWorld(this, chunk.getPos().x, chunk.getPos().z)) {
            ci.cancel();
        }
    }

    @Inject(method = "setStructureStarts", at = @At("HEAD"), cancellable = true)
    private void gbw$limitStructureStarts(DynamicRegistryManager registryManager, StructurePlacementCalculator placementCalculator, StructureAccessor structureAccessor, Chunk chunk, StructureTemplateManager structureTemplateManager, CallbackInfo ci) {
        if (WorldSizeHolder.isWithinWorld(this, chunk.getPos().x, chunk.getPos().z)) {
            ci.cancel();
        }
    }
}
