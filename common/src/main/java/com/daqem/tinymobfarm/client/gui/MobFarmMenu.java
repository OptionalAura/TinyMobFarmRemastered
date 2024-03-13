package com.daqem.tinymobfarm.client.gui;

import com.daqem.tinymobfarm.TinyMobFarm;
import com.daqem.tinymobfarm.common.tileentity.MobFarmBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MobFarmMenu extends AbstractContainerMenu {

	private final ContainerData containerData;

	public MobFarmMenu(int windowId, Inventory inv, Container container, ContainerData containerData) {
		super(TinyMobFarm.MOB_FARM_CONTAINER.get(), windowId);

		this.containerData = containerData;

		this.addSlot(new LassoSlot(container, 0, 80, 25) {
			@Override
			public void setChanged() {
				super.setChanged();
				if (this.container instanceof MobFarmBlockEntity) {
					((MobFarmBlockEntity) this.container).saveAndSync();
				}
			}
		});
		
		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(inv, i, i * 18 + 8, 142));
		}
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 3; j++) {
				this.addSlot(new Slot(inv, i + j * 9 + 9, i * 18 + 8, j * 18 + 84));
			}
		}

		this.addDataSlots(containerData);
	}

	public MobFarmMenu(int i, Inventory inventory) {
		this(i, inventory, new SimpleContainer(1), new SimpleContainerData(3));
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
	
	@Override
	public @NotNull ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(index);
	
		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
		
			int containerSlots = slots.size() - player.getInventory().items.size();
	
			if (index < containerSlots) {
				if (!this.moveItemStackTo(itemstack1, containerSlots, slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}
	
			if (itemstack1.getCount() == 0) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
	
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}

	public int getProgress() {
		return this.containerData.get(0);
	}

	public int getMaxProgress() {
		return this.containerData.get(1);
	}

	public double getScaledProgress() {
		if (this.getProgress() <= 0) return 0;
		return this.getProgress() / (double) this.getMaxProgress();
	}

	public boolean isPowered() {
		return this.containerData.get(2) > 0;
	}
}
