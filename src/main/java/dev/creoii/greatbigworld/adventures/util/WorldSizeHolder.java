package dev.creoii.greatbigworld.adventures.util;

import net.minecraft.util.math.BlockPos;

public interface WorldSizeHolder {
    void gbw$setWorldSize(int worldSize);

    int gbw$getWorldSize();

    static boolean isOutsideWorld(WorldSizeHolder worldSizeHolder, int chunkX, int chunkZ) {
        return worldSizeHolder.gbw$getWorldSize() > 0 && (chunkX >= worldSizeHolder.gbw$getWorldSize() || chunkX < -worldSizeHolder.gbw$getWorldSize() || chunkZ >= worldSizeHolder.gbw$getWorldSize() || chunkZ < -worldSizeHolder.gbw$getWorldSize());
    }

    static boolean isWithinWorld(WorldSizeHolder worldSizeHolder, BlockPos pos) {
        int sizeInBlocks = worldSizeHolder.gbw$getWorldSize() << 4; // multiply by 4
        return sizeInBlocks > 0 && (pos.getX() < sizeInBlocks || pos.getX() >= -sizeInBlocks || pos.getZ() < sizeInBlocks || pos.getZ() >= -sizeInBlocks);
    }
}
