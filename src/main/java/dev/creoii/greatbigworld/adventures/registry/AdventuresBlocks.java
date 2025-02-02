package dev.creoii.greatbigworld.adventures.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.adventures.block.BedframeBlock;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.util.Identifier;

public final class AdventuresBlocks {
    public static Block BEDFRAME;

    public static void register() {
        BEDFRAME = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "bedframe"), BedframeBlock::new, AbstractBlock.Settings.copy(Blocks.WHITE_BED).strength(.8f, .4f).mapColor(MapColor.OAK_TAN));

        FlammableBlockRegistry.getDefaultInstance().add(BEDFRAME, 5, 5);
    }
}
