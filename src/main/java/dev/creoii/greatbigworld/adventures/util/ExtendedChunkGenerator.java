package dev.creoii.greatbigworld.adventures.util;

public interface ExtendedChunkGenerator {
    void gbw$setWorldSize(int worldSize);

    int gbw$getWorldSize();

    static boolean isWithinWorld(ExtendedChunkGenerator chunkGenerator, int chunkX, int chunkZ) {
        return chunkGenerator.gbw$getWorldSize() > 0 && (chunkX >= chunkGenerator.gbw$getWorldSize() || chunkX < -chunkGenerator.gbw$getWorldSize() || chunkZ >= chunkGenerator.gbw$getWorldSize() || chunkZ < -chunkGenerator.gbw$getWorldSize());
    }
}
