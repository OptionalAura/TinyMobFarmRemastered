package com.daqem.tinymobfarm.mixin;

import com.daqem.tinymobfarm.TinyMobFarm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @Inject(at = @At("RETURN"), method = "canEnchant", cancellable = true)
    private void canEnchant(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        Enchantment enchantment = (Enchantment) (Object) this;
        if (enchantment == Enchantments.MOB_LOOTING) {
            if (itemStack.is(TinyMobFarm.LASSO.get())) {
                cir.setReturnValue(true);
            }
        }
    }
}
