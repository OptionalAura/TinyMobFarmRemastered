package com.daqem.tinymobfarm.common.item;

import com.daqem.tinymobfarm.ConfigTinyMobFarm;
import com.daqem.tinymobfarm.TinyMobFarm;
import com.daqem.tinymobfarm.core.util.EntityHelper;
import com.daqem.tinymobfarm.core.util.Msg;
import com.daqem.tinymobfarm.core.util.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LassoItem extends Item {

	public LassoItem(Properties properties) {
		super(properties.arch$tab(TinyMobFarm.JOBSPLUS_TOOLS_TAB).defaultDurability(ConfigTinyMobFarm.lassoDurability.get()));
	}

	@Override
	public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand interactionHand) {
		if (NBTHelper.hasMob(stack) || !target.isAlive() || !(target instanceof Mob)) return InteractionResult.FAIL;

		CompoundTag nbt = NBTHelper.getBaseTag(stack);

		// Cannot capture boss.
		if (!target.canChangeDimensions()) {
			if (!player.level().isClientSide()) {
				Msg.tellPlayer(player, "tinymobfarm.error.cannot_capture_boss");
			}
			return InteractionResult.SUCCESS;
		}

		if (!player.level().isClientSide()) {
			CompoundTag mobData = target.saveWithoutId(new CompoundTag());
			mobData.put("Rotation", NBTHelper.createNBTList(
					DoubleTag.valueOf(0), DoubleTag.valueOf(0)));

			nbt.put(NBTHelper.MOB_DATA, mobData);

			Component name = target.getName();
			nbt.putString(NBTHelper.MOB_NAME, name.getString());
			nbt.putString(NBTHelper.MOB_ID, target.getType().arch$registryName().toString());
			nbt.putString(NBTHelper.MOB_LOOTTABLE_LOCATION, EntityHelper.getLootTableLocation(target));
			nbt.putDouble(NBTHelper.MOB_HEALTH, Math.round(target.getHealth() * 10) / 10.0);
			nbt.putDouble(NBTHelper.MOB_MAX_HEALTH, target.getMaxHealth());
			nbt.putBoolean(NBTHelper.MOB_HOSTILE, target instanceof Monster);

			if (player.isCreative()) {
				ItemStack newLasso = new ItemStack(this);
				NBTHelper.setBaseTag(newLasso, nbt);
				player.addItem(newLasso);
			}

			target.discard();
			player.getInventory().setChanged();
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public @NotNull InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		Direction facing = context.getClickedFace();
		BlockPos pos = context.getClickedPos().offset(facing.getStepX(), facing.getStepY(), facing.getStepZ());
		Level world = context.getLevel();

		if (!NBTHelper.hasMob(stack)) return InteractionResult.FAIL;
		if (player == null) return InteractionResult.FAIL;
		if (!player.mayUseItemAt(pos, facing, stack)) return InteractionResult.FAIL;

		if (!context.getLevel().isClientSide()) {
			CompoundTag nbt = NBTHelper.getBaseTag(stack);
			CompoundTag mobData = nbt.getCompound(NBTHelper.MOB_DATA);
			String id = nbt.getString(NBTHelper.MOB_ID);

			DoubleTag x = DoubleTag.valueOf(pos.getX() + 0.5);
			DoubleTag y = DoubleTag.valueOf(pos.getY());
			DoubleTag z = DoubleTag.valueOf(pos.getZ() + 0.5);
			ListTag mobPos = NBTHelper.createNBTList(x, y, z);
			mobData.put("Pos", mobPos);
			mobData.putString("id", id);

			Entity mob = EntityType.loadEntityRecursive(mobData, world, entity -> entity);
			if (mob != null) world.addFreshEntity(mob);

			stack.removeTagKey(NBTHelper.MOB);
			stack.hurtAndBreak(1, player, wutTheFak -> {});
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
		if (NBTHelper.hasMob(stack)) {
			CompoundTag nbt = NBTHelper.getBaseTag(stack);
			String name = nbt.getString(NBTHelper.MOB_NAME);
			String id = nbt.getString(NBTHelper.MOB_ID);
			double health = nbt.getDouble(NBTHelper.MOB_HEALTH);
			double maxHealth = nbt.getDouble(NBTHelper.MOB_MAX_HEALTH);
			
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.release_mob.key"));
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.mob_name.key", name));
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.mob_id.key", id));
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.health.key", health, maxHealth));
			if (nbt.getBoolean(NBTHelper.MOB_HOSTILE)) tooltip.add(Msg.tooltip("tinymobfarm.tooltip.hostile.key"));
		} else {
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.capture.key"));
		}
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return NBTHelper.hasMob(stack);
	}
}
