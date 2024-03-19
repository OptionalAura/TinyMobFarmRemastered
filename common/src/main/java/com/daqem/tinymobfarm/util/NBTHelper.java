package com.daqem.tinymobfarm.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class NBTHelper {
	
	// Lasso.
	public static final String MOB = "capturedMob";
	public static final String MOB_NAME = "mobName";
	public static final String MOB_ID = "mobId";
	public static final String MOB_DATA = "mobData";
	public static final String MOB_HEALTH = "mobHealth";
	public static final String MOB_MAX_HEALTH = "mobMaxHealth";
	public static final String MOB_HOSTILE = "mobHostile";
	public static final String MOB_LOOTTABLE_LOCATION = "mobLootTableLocation";
	
	// Tile entity.
	public static final String MOB_FARM_DATA = "mobFarmData";
	public static final String CURR_PROGRESS = "currProgress";

	public static CompoundTag getBaseTag(ItemStack stack) {
		return stack.getOrCreateTagElement(MOB);
	}
	
	public static void setBaseTag(ItemStack stack, CompoundTag nbt) {
		stack.getOrCreateTag().put(MOB, nbt);
	}
	
	public static boolean hasMob(ItemStack stack) {
		return stack.getOrCreateTag().contains(MOB);
	}
	
	public static boolean hasHostileMob(ItemStack stack) {
		if (!hasMob(stack)) return false;
		CompoundTag nbt = NBTHelper.getBaseTag(stack);
		return nbt.getBoolean(NBTHelper.MOB_HOSTILE);
	}

	public static ListTag createNBTList(Tag... tags) {
		ListTag list = new ListTag();
        list.addAll(Arrays.asList(tags));
		return list;
	}
}
