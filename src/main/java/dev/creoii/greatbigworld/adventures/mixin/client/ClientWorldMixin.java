package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.adventures.client.AdventuresClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Override
    public float getSkyAngle(float tickProgress) {
        double d = MathHelper.fractionalPart((double)getDimension().fixedTime().orElse((long) AdventuresClient.skyTime) / (double)(AdventuresClient.dayLength + AdventuresClient.nightLength) - (double).25f);
        double e = (double).5f - Math.cos(d * Math.PI) / (double)2f;
        return (float)(d * (double)2f + e) / 3f;
    }

    @Override
    public int getMoonPhase() {
        return (int)(AdventuresClient.skyTime / (AdventuresClient.dayLength + AdventuresClient.nightLength) % 8L + 8L) % 8;
    }

    @Override
    public boolean isNightAndNatural() {
        long total = AdventuresClient.dayLength + AdventuresClient.nightLength;
        if (!getDimension().natural()) {
            return false;
        } else {
            int i = (int)(getTimeOfDay() % total);
            return i >= AdventuresClient.dayLength + 600L && i <= total - 600L;
        }
    }
}
