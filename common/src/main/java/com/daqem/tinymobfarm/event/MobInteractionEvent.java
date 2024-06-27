package com.daqem.tinymobfarm.event;

import com.daqem.tinymobfarm.TinyMobFarm;
import com.daqem.tinymobfarm.item.LassoItem;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import static com.daqem.tinymobfarm.item.LassoItem.isMobDisabled;

public class MobInteractionEvent {

    public static void registerEvent() {

        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof LassoItem lassoItem && entity instanceof LivingEntity target) {
                if (isMobDisabled(player, target) && !Permissions.check(player, "tinymobfarm.bypassDisabledMobs", false)){
                    if (!player.level().isClientSide()) {
                        player.sendSystemMessage(TinyMobFarm.translatable("error.blacklisted_mob"));
                    }
                    return EventResult.interruptFalse();
                }
                if (lassoItem.interactMob(stack, player, target, hand) == InteractionResult.SUCCESS) {
                    return EventResult.interruptTrue();
                }
            }
            return EventResult.pass();
        });
    }
}
