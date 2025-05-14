package dev.creoii.greatbigworld.adventures.util;

public interface WorldSizeHolder {
    void gbw$setWorldSize(int worldSize);

    int gbw$getWorldSize();

    static boolean isOutsideWorld(WorldSizeHolder worldSizeHolder, int chunkX, int chunkZ) {
        if (worldSizeHolder.gbw$getWorldSize() == -1)
            return false;
        if (worldSizeHolder.gbw$getWorldSize() == 0) {
            return chunkX > 0 || chunkZ > 0;
        } else return chunkX >= worldSizeHolder.gbw$getWorldSize() || chunkX < -worldSizeHolder.gbw$getWorldSize() || chunkZ >= worldSizeHolder.gbw$getWorldSize() || chunkZ < -worldSizeHolder.gbw$getWorldSize();
    }
}
