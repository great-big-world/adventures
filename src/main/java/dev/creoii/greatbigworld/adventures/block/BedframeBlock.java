package dev.creoii.greatbigworld.adventures.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class BedframeBlock extends HorizontalFacingBlock {
    public static final MapCodec<BedframeBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(createSettingsCodec()).apply(instance, BedframeBlock::new);
    });
    public static final EnumProperty<BedPart> PART = Properties.BED_PART;
    protected static VoxelShape TOP_SHAPE = Block.createCuboidShape(0d, 3d, 0d, 16d, 6d, 16d);
    protected static final VoxelShape LEG_1_SHAPE = Block.createCuboidShape(0d, 0d, 0d, 3d, 3d, 3d);
    protected static final VoxelShape LEG_2_SHAPE = Block.createCuboidShape(0d, 0d, 13d, 3d, 3d, 16d);
    protected static final VoxelShape LEG_3_SHAPE = Block.createCuboidShape(13d, 0d, 0d, 16d, 3d, 3d);
    protected static final VoxelShape LEG_4_SHAPE = Block.createCuboidShape(13d, 0d, 13d, 16d, 3d, 16d);
    protected static final VoxelShape NORTH_SHAPE = VoxelShapes.union(TOP_SHAPE, LEG_1_SHAPE, LEG_3_SHAPE);
    protected static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(TOP_SHAPE, LEG_2_SHAPE, LEG_4_SHAPE);
    protected static final VoxelShape WEST_SHAPE = VoxelShapes.union(TOP_SHAPE, LEG_1_SHAPE, LEG_2_SHAPE);
    protected static final VoxelShape EAST_SHAPE = VoxelShapes.union(TOP_SHAPE, LEG_3_SHAPE, LEG_4_SHAPE);

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    public BedframeBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(PART, BedPart.FOOT));
    }

    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == getDirectionTowardsOtherPart(state.get(PART), state.get(FACING))) {
            return neighborState.isOf(this) && neighborState.get(PART) != state.get(PART) ? state : Blocks.AIR.getDefaultState();
        } else {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    private static Direction getDirectionTowardsOtherPart(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            BedPart bedPart = state.get(PART);
            if (bedPart == BedPart.FOOT) {
                BlockPos blockPos = pos.offset(getDirectionTowardsOtherPart(bedPart, state.get(FACING)));
                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.isOf(this) && blockState.get(PART) == BedPart.HEAD) {
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 35);
                    world.syncWorldEvent(player, 2001, blockPos, Block.getRawIdFromState(blockState));
                }
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getHorizontalPlayerFacing();
        BlockPos blockPos = ctx.getBlockPos();
        BlockPos blockPos2 = blockPos.offset(direction);
        World world = ctx.getWorld();
        return world.getBlockState(blockPos2).canReplace(ctx) && world.getWorldBorder().contains(blockPos2) ? (BlockState)this.getDefaultState().with(FACING, direction) : null;
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = getOppositePartDirection(state).getOpposite();
        return switch (direction) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> EAST_SHAPE;
        };
    }

    public static Direction getOppositePartDirection(BlockState state) {
        Direction direction = state.get(FACING);
        return state.get(PART) == BedPart.HEAD ? direction.getOpposite() : direction;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            BlockPos blockPos = pos.offset(state.get(FACING));
            world.setBlockState(blockPos, state.with(PART, BedPart.HEAD), 3);
            world.updateNeighbors(pos, Blocks.AIR);
            state.updateNeighbors(world, pos, 3);
        }
    }

    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}
