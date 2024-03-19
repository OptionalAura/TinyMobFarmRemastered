package com.daqem.tinymobfarm.blockentity;

import com.daqem.tinymobfarm.TinyMobFarm;
import com.daqem.tinymobfarm.client.gui.MobFarmMenu;
import com.daqem.tinymobfarm.MobFarmType;
import com.daqem.tinymobfarm.util.EntityHelper;
import com.daqem.tinymobfarm.util.FakePlayerHelper;
import com.daqem.tinymobfarm.util.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MobFarmBlockEntity extends BlockEntity implements MenuProvider, Container {

	private MobFarmType mobFarmData;
	private LivingEntity livingEntity;
	private Direction modelFacing = Direction.NORTH;
	protected NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
	private int progress;
	private boolean powered;
	private boolean shouldUpdate;
	protected final ContainerData dataAccess = new ContainerData() {
		@Override
		public int get(int i) {
            return switch (i) {
                case 0 -> MobFarmBlockEntity.this.progress;
                case 1 -> MobFarmBlockEntity.this.mobFarmData == null ? 0 : MobFarmBlockEntity.this.mobFarmData.getMaxProgress();
				case 2 -> MobFarmBlockEntity.this.powered ? 1 : 0;
                default -> 0;
            };
		}

		@Override
		public void set(int i, int j) {
			switch (i) {
				case 0 -> MobFarmBlockEntity.this.progress = j;
				case 2 -> MobFarmBlockEntity.this.powered = j != 0;
			}
		}

		@Override
		public int getCount() {
			return 3;
		}
	};
	
	public MobFarmBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(TinyMobFarm.MOB_FARM_TILE_ENTITY.get(), blockPos, blockState);
	}

	@SuppressWarnings("unused")
	public static void tick(Level level, BlockPos blockPos, BlockState blockState, MobFarmBlockEntity blockEntity) {
		blockEntity.tick();
	}

	public void tick() {
		if (this.shouldUpdate) {
			this.updateModel();
			this.updateRedstone();
			this.shouldUpdate = false;
		}
		if (this.isWorking()) {
			this.progress++;
			if (this.level != null) {
				if (!this.level.isClientSide() && this.mobFarmData != null) {
					if (this.progress >= this.mobFarmData.getMaxProgress()) {
						this.progress = 0;

						this.generateDrops();

						if (level instanceof ServerLevel serverLevel) {
							ServerPlayer daniel = FakePlayerHelper.getPlayer(serverLevel);
							this.getLasso().hurtAndBreak(this.mobFarmData.getRandomDamage(serverLevel.random), daniel, wut -> {
							});
						}
						this.saveAndSync();
					}
				}
			}
		} else {
			this.progress = 0;
		}
	}
	
	private void generateDrops() {
		ItemStack lasso = this.getLasso();
		String lootTableLocation = NBTHelper.getBaseTag(lasso).getString(NBTHelper.MOB_LOOTTABLE_LOCATION);
		if (lootTableLocation.isEmpty()) return;

		if (this.level instanceof ServerLevel serverLevel) {
			List<ItemStack> drops = EntityHelper.generateLoot(new ResourceLocation(lootTableLocation), serverLevel, lasso);
			Direction direction = Direction.DOWN;
			BlockEntity tileEntity = this.level.getBlockEntity(this.worldPosition.relative(direction));
			if (tileEntity instanceof Container container) {
				if (!HopperBlockEntity.isFullContainer(container, direction)) {
					List<ItemStack> toRemove = new ArrayList<>();
					for (int i = 0; i < drops.size(); i++) {
						ItemStack drop = drops.get(i);
						TinyMobFarm.LOGGER.info("Dropping " + drop);
						ItemStack dropLeftOver = HopperBlockEntity.addItem(this, container, drop, direction);
						if (dropLeftOver.isEmpty()) {
							toRemove.add(drop);
						} else {
							drops.set(i, dropLeftOver);
						}
					}
					drops.removeAll(toRemove);
				}
			}

			for (ItemStack stack : drops) {
				ItemEntity entityItem = new ItemEntity(this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1, this.worldPosition.getZ() + 0.5, stack);
				this.level.addFreshEntity(entityItem);
			}
		}
	}
	
	private void updateModel() {
		if (this.level == null) return;
		if (this.level.isClientSide()) {
			if (this.getLasso().isEmpty()) {
				this.livingEntity = null;
			} else {
				CompoundTag nbt = NBTHelper.getBaseTag(this.getLasso());
				String mobName = nbt.getString(NBTHelper.MOB_NAME);
				String mobId = nbt.getString(NBTHelper.MOB_ID);
				//noinspection EqualsBetweenInconvertibleTypes
				if (this.livingEntity == null || !this.livingEntity.getName().getContents().equals(mobName)) {
					CompoundTag entityData = nbt.getCompound(NBTHelper.MOB_DATA);
					entityData.putString("id", mobId);
					Entity newModel = EntityType.loadEntityRecursive(entityData, this.level, entity -> entity);
					
					if (newModel instanceof LivingEntity) {
						this.livingEntity = (LivingEntity) newModel;
						this.modelFacing = this.level.getBlockState(this.worldPosition).getValue(HorizontalDirectionalBlock.FACING);
					}
				}
			}
		}
	}
	
	public boolean isWorking() {
		if (this.mobFarmData == null || this.getLasso().isEmpty() || this.isPowered()) return false;
		return this.mobFarmData.isLassoValid(this.getLasso());
	}
	
	public void updateRedstone() {
		if (this.level == null) return;
		this.powered = this.level.getBestNeighborSignal(worldPosition) != 0;
	}
	
	public ItemStack getLasso() {
		return this.items.get(0);
	}
	
	public void setMobFarmData(MobFarmType mobFarmData) {
		this.mobFarmData = mobFarmData;
	}
	
	public boolean isPowered() {
		return this.powered;
	}
	
	public LivingEntity getLivingEntity() {
		return this.livingEntity;
	}
	
	public Direction getModelFacing() {
		return this.modelFacing;
	}
	
	public void saveAndSync() {
		if (this.level == null) return;
		BlockState state = this.level.getBlockState(this.worldPosition);
		this.level.sendBlockUpdated(worldPosition, state, state, 3);
		this.setChanged();
	}
	
	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.mobFarmData = MobFarmType.values()[nbt.getInt(NBTHelper.MOB_FARM_DATA)];
		this.progress = nbt.getInt(NBTHelper.CURR_PROGRESS);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, this.items);
		this.shouldUpdate = true;
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		if (this.mobFarmData != null) {
			nbt.putInt(NBTHelper.MOB_FARM_DATA, this.mobFarmData.ordinal());
			nbt.putInt(NBTHelper.CURR_PROGRESS, this.progress);
			ContainerHelper.saveAllItems(nbt, this.items);
		}
		super.saveAdditional(nbt);
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public @NotNull CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
		return new MobFarmMenu(windowId, inv, this, this.dataAccess);
	}

	@Override
	public @NotNull Component getDisplayName() {
		return Component.translatable(mobFarmData.getUnlocalizedName());
	}

	@Override
	public int getContainerSize() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemStack : this.items) {
			if (itemStack.isEmpty()) continue;
			return false;
		}
		return true;
	}

	@Override
	public @NotNull ItemStack getItem(int i) {
		return this.items.get(i);
	}

	@Override
	public @NotNull ItemStack removeItem(int i, int j) {
		return ContainerHelper.removeItem(this.items, i, j);
	}

	@Override
	public @NotNull ItemStack removeItemNoUpdate(int i) {
		return ContainerHelper.takeItem(this.items, i);
	}

	@Override
	public void setItem(int i, ItemStack itemStack) {
		ItemStack itemStack2 = this.items.get(i);
		boolean bl = !itemStack.isEmpty() && ItemStack.isSameItemSameTags(itemStack2, itemStack);
		this.items.set(i, itemStack);
		if (itemStack.getCount() > this.getMaxStackSize()) {
			itemStack.setCount(this.getMaxStackSize());
		}
		if (i == 0 && !bl) {
			this.progress = 0;
			this.setChanged();
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	@Override
	public void setChanged() {
		super.setChanged();
		this.shouldUpdate = true;
	}
}
