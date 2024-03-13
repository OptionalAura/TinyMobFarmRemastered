package com.daqem.tinymobfarm.core.util;

import com.daqem.tinymobfarm.ConfigTinyMobFarm;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
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
	
	public static List<ItemStack> generateLoot(ResourceLocation lootTableLocation, ServerLevel world, ItemStack stack) {
		LootDataManager lootTableManager = world.getServer().getLootData();
		LootTable lootTable = lootTableManager.getLootTable(lootTableLocation);
		LootParams.Builder builder = new LootParams.Builder(world);
		ServerPlayer daniel = FakePlayerHelper.getPlayer(world);

		if (ConfigTinyMobFarm.allowLassoLooting.get()) {
			daniel.addItem(stack.copy());
		}

		builder.withParameter(LootContextParams.KILLER_ENTITY, daniel);

		LootContextParamSet.Builder setBuilder = new LootContextParamSet.Builder();
		setBuilder.required(LootContextParams.KILLER_ENTITY);
		return lootTable.getRandomItems(builder.create(setBuilder.build()));
	}
}
