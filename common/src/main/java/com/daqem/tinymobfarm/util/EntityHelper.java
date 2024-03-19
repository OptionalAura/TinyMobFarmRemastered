package com.daqem.tinymobfarm.util;

import com.daqem.tinymobfarm.ConfigTinyMobFarm;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

public class EntityHelper {
	
	public static String getLootTableLocation(LivingEntity livingEntity) {
		return livingEntity.getLootTable().toString();
	}
	
	public static List<ItemStack> generateLoot(ResourceLocation lootTableLocation, ServerLevel level, ItemStack stack) {
		LootDataManager lootTableManager = level.getServer().getLootData();
		LootTable lootTable = lootTableManager.getLootTable(lootTableLocation);
		LootParams.Builder builder = new LootParams.Builder(level);
		ServerPlayer daniel = FakePlayerHelper.getPlayer(level);

		if (ConfigTinyMobFarm.allowLassoLooting.get()) {
			daniel.addItem(stack.copy());
		}

		builder.withParameter(LootContextParams.KILLER_ENTITY, daniel);

		LootContextParamSet.Builder setBuilder = new LootContextParamSet.Builder();
		setBuilder.required(LootContextParams.KILLER_ENTITY);
		return lootTable.getRandomItems(builder.create(setBuilder.build()));
	}

	public static Entity getEntityFromLasso(ItemStack lasso, BlockPos pos, Level level) {
		CompoundTag nbt = NBTHelper.getBaseTag(lasso);
		CompoundTag mobData = nbt.getCompound(NBTHelper.MOB_DATA);
		String id = nbt.getString(NBTHelper.MOB_ID);

		DoubleTag x = DoubleTag.valueOf(pos.getX() + 0.5);
		DoubleTag y = DoubleTag.valueOf(pos.getY());
		DoubleTag z = DoubleTag.valueOf(pos.getZ() + 0.5);
		ListTag mobPos = NBTHelper.createNBTList(x, y, z);
		mobData.put("Pos", mobPos);
		mobData.putString("id", id);

		return EntityType.loadEntityRecursive(mobData, level, entity -> entity);
	}
}
