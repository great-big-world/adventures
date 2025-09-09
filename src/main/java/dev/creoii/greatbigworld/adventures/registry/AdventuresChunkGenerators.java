package dev.creoii.greatbigworld.adventures.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.world.SkyblockChunkGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class AdventuresChunkGenerators {
    public static void register() {
        Registry.register(Registries.CHUNK_GENERATOR, Identifier.of(GreatBigWorld.NAMESPACE, "skyblock"), SkyblockChunkGenerator.CODEC);
    }
}
