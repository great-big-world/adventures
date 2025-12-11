package dev.creoii.greatbigworld.adventures.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.world.SkyblockChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class AdventuresChunkGenerators {
    public static void register() {
        Registry.register(BuiltInRegistries.CHUNK_GENERATOR, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "skyblock"), SkyblockChunkGenerator.CODEC);
    }
}
