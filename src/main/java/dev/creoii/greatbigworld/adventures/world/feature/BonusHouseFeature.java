package dev.creoii.greatbigworld.adventures.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.inventory.LootableInventory;
import net.minecraft.loot.LootTables;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class BonusHouseFeature extends Feature<BonusHouseFeatureConfig> {
    public BonusHouseFeature(Codec<BonusHouseFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<BonusHouseFeatureConfig> context) {
        BlockPos origin = context.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, context.getOrigin());

        int chunkX = (origin.getX() >> 4) << 4;
        int chunkZ = (origin.getZ() >> 4) << 4;
        BlockPos pos = new BlockPos(chunkX + 8, origin.getY(), chunkZ + 8);

        Direction entrance = Direction.Type.HORIZONTAL.random(context.getRandom());

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int z = -3; z <= 3; ++z) {
            mutable.setZ(pos.getZ() + z);
            for (int y = 0; y <= 4; ++y) {
                mutable.setY(pos.getY() + y);
                for (int x = -3; x <= 3; ++x) {
                    mutable.setX(pos.getX() + x);

                    if (Math.abs(x) != 3 && y != 0 && y != 4 && Math.abs(z) != 3) {
                        context.getWorld().setBlockState(mutable, Blocks.AIR.getDefaultState(), 2);
                        continue;
                    }

                    if ((y == 1 || y == 2) && ((entrance == Direction.NORTH && z == -3 && x == 0) || (entrance == Direction.SOUTH && z == 3 && x == 0) || (entrance == Direction.WEST  && x == -3 && z == 0) || (entrance == Direction.EAST  && x == 3  && z == 0))) {
                        context.getWorld().setBlockState(mutable, Blocks.AIR.getDefaultState(), 2);
                        continue;
                    }

                    BlockState state = y == 0 ? Blocks.STONE.getDefaultState() : Blocks.OAK_PLANKS.getDefaultState();
                    context.getWorld().setBlockState(mutable, state, 2);
                }
            }
        }

        BlockPos torchPos = new BlockPos(pos.getX() + context.getRandom().nextBetween(-2, 2), pos.getY() + 1, pos.getZ() + context.getRandom().nextBetween(-2, 2));
        if (context.getConfig().hasChest()) {
            BlockPos chestPos = new BlockPos(pos.getX() + context.getRandom().nextBetween(-2, 2), pos.getY() + 1, pos.getZ() + context.getRandom().nextBetween(-2, 2));
            Direction facing = Direction.NORTH;
            for (Direction direction : Direction.Type.HORIZONTAL.getShuffled(context.getRandom())) {
                if (context.getWorld().isAir(chestPos.offset(direction))) {
                    facing = direction;
                    break;
                }
            }
            context.getWorld().setBlockState(chestPos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, facing), 2);
            LootableInventory.setLootTable(context.getWorld(), context.getRandom(), chestPos, LootTables.SPAWN_BONUS_CHEST);

            while (chestPos.equals(torchPos)) {
                torchPos = new BlockPos(pos.getX() + context.getRandom().nextBetween(-2, 2), pos.getY() + 1, pos.getZ() + context.getRandom().nextBetween(-2, 2));
            }
        }

        context.getWorld().setBlockState(torchPos, Blocks.TORCH.getDefaultState(), 2);

        return true;
    }
}
