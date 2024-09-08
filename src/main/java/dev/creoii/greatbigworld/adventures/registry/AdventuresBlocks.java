package dev.creoii.greatbigworld.adventures.registry;

import dev.creoii.greatbigworld.adventures.Adventures;
import dev.creoii.greatbigworld.adventures.block.BedframeBlock;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class AdventuresBlocks {
    public static final Block BEDFRAME = new BedframeBlock(AbstractBlock.Settings.copy(Blocks.WHITE_BED).mapColor(MapColor.OAK_TAN));

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(Adventures.NAMESPACE, "bedframe"), BEDFRAME);

        FlammableBlockRegistry.getDefaultInstance().add(BEDFRAME, 5, 5);
    }
}
