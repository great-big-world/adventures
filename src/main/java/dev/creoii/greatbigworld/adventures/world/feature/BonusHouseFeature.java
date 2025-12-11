package dev.creoii.greatbigworld.adventures.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class BonusHouseFeature extends Feature<BonusHouseFeatureConfig> {
    public BonusHouseFeature(Codec<BonusHouseFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BonusHouseFeatureConfig> context) {
        BlockPos origin = context.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, context.origin());

        int chunkX = (origin.getX() >> 4) << 4;
        int chunkZ = (origin.getZ() >> 4) << 4;
        BlockPos pos = new BlockPos(chunkX + 8, origin.getY(), chunkZ + 8);

        Direction entrance = Direction.Plane.HORIZONTAL.getRandomDirection(context.random());

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int z = -3; z <= 3; ++z) {
            mutable.setZ(pos.getZ() + z);
            for (int y = 0; y <= 4; ++y) {
                mutable.setY(pos.getY() + y);
                for (int x = -3; x <= 3; ++x) {
                    mutable.setX(pos.getX() + x);

                    if (Math.abs(x) != 3 && y != 0 && y != 4 && Math.abs(z) != 3) {
                        context.level().setBlock(mutable, Blocks.AIR.defaultBlockState(), 2);
                        continue;
                    }

                    if ((y == 1 || y == 2) && ((entrance == Direction.NORTH && z == -3 && x == 0) || (entrance == Direction.SOUTH && z == 3 && x == 0) || (entrance == Direction.WEST  && x == -3 && z == 0) || (entrance == Direction.EAST  && x == 3  && z == 0))) {
                        context.level().setBlock(mutable, Blocks.AIR.defaultBlockState(), 2);
                        continue;
                    }

                    BlockState state = y == 0 ? Blocks.STONE.defaultBlockState() : Blocks.OAK_PLANKS.defaultBlockState();
                    context.level().setBlock(mutable, state, 2);
                }
            }
        }

        BlockPos torchPos = new BlockPos(pos.getX() + context.random().nextIntBetweenInclusive(-2, 2), pos.getY() + 1, pos.getZ() + context.random().nextIntBetweenInclusive(-2, 2));
        if (context.config().hasChest()) {
            BlockPos chestPos = new BlockPos(pos.getX() + context.random().nextIntBetweenInclusive(-2, 2), pos.getY() + 1, pos.getZ() + context.random().nextIntBetweenInclusive(-2, 2));
            Direction facing = Direction.NORTH;
            for (Direction direction : Direction.Plane.HORIZONTAL.shuffledCopy(context.random())) {
                if (context.level().isEmptyBlock(chestPos.relative(direction))) {
                    facing = direction;
                    break;
                }
            }
            context.level().setBlock(chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing), 2);
            RandomizableContainer.setBlockEntityLootTable(context.level(), context.random(), chestPos, BuiltInLootTables.SPAWN_BONUS_CHEST);

            while (chestPos.equals(torchPos)) {
                torchPos = new BlockPos(pos.getX() + context.random().nextIntBetweenInclusive(-2, 2), pos.getY() + 1, pos.getZ() + context.random().nextIntBetweenInclusive(-2, 2));
            }
        }

        context.level().setBlock(torchPos, Blocks.TORCH.defaultBlockState(), 2);

        return true;
    }
}
