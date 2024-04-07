package com.daqem.tinymobfarm.block;

import java.util.List;

import com.daqem.tinymobfarm.TinyMobFarm;
import com.daqem.tinymobfarm.blockentity.MobFarmBlockEntity;
import com.daqem.tinymobfarm.MobFarmType;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MobFarmBlock extends BaseEntityBlock {
	
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	
	private static final VoxelShape BOUNDING_BOX = Block.box(1, 0, 1, 15, 14, 15);

	private final MobFarmType mobFarmData;
	
	public MobFarmBlock(MobFarmType mobFarmData) {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5f, 6.0f));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
		this.mobFarmData = mobFarmData;
	}
	
	public Consumer<List<Component>> getTooltipBuilder() {
		return this.mobFarmData::addTooltip;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getHorizontalDirection().getOpposite();
		return this.defaultBlockState().setValue(FACING, facing);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);

		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (tileEntity instanceof MobFarmBlockEntity mobFarmBlockEntity) {
			mobFarmBlockEntity.setMobFarmData(mobFarmData);
			mobFarmBlockEntity.updateRedstone();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;

		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (tileEntity instanceof MobFarmBlockEntity mobFarmBlockEntity) {
            player.openMenu(mobFarmBlockEntity);
		}

		return InteractionResult.SUCCESS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
		BlockEntity tileEntity = levelAccessor.getBlockEntity(blockPos);
		if (tileEntity instanceof MobFarmBlockEntity mobFarmBlockEntity) {
            mobFarmBlockEntity.updateRedstone();
			mobFarmBlockEntity.saveAndSync();
		}
		return blockState;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
		BlockEntity tileEntity = level.getBlockEntity(blockPos);
		if (tileEntity instanceof MobFarmBlockEntity mobFarmBlockEntity) {
			mobFarmBlockEntity.updateRedstone();
			mobFarmBlockEntity.saveAndSync();
		}
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
		BlockEntity tileEntity = level.getBlockEntity(blockPos);
		if (tileEntity instanceof MobFarmBlockEntity mobFarmBlockEntity) {
			ItemStack lasso = mobFarmBlockEntity.getLasso();
			if (!lasso.isEmpty()) {
				ItemEntity drop = new ItemEntity(level, blockPos.getX() + 0.5, blockPos.getY() + 0.3, blockPos.getZ() + 0.5, lasso);
				level.addFreshEntity(drop);
			}
		}
		super.playerWillDestroy(level, blockPos, blockState, player);

	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return BOUNDING_BOX;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getLightBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return 0;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new MobFarmBlockEntity(blockPos, blockState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
		return MobFarmBlock.createTickerHelper(blockEntityType, TinyMobFarm.MOB_FARM_TILE_ENTITY.get(), MobFarmBlockEntity::tick);
	}

	@Override
	public @NotNull RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.MODEL;
	}
}
