package com.daqem.tinymobfarm;

import com.daqem.tinymobfarm.client.gui.MobFarmMenu;
import com.daqem.tinymobfarm.common.block.MobFarmBlock;
import com.daqem.tinymobfarm.common.item.MobFarmBlockItem;
import com.daqem.tinymobfarm.common.item.LassoItem;
import com.daqem.tinymobfarm.common.blockentity.MobFarmBlockEntity;
import com.daqem.tinymobfarm.core.EnumMobFarm;
import com.google.common.base.Suppliers;
import com.mojang.logging.LogUtils;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class TinyMobFarm {
    public static final String MOD_ID = "tinymobfarm";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static final Registrar<CreativeModeTab> TABS = MANAGER.get().get(Registries.CREATIVE_MODE_TAB);
    public static final RegistrySupplier<CreativeModeTab> JOBSPLUS_TOOLS_TAB = TABS.register(getId(MOD_ID + "_tab"), () ->
            CreativeTabRegistry.create(Component.translatable("itemGroup.tiny_mob_farm"),
                    () -> new ItemStack(TinyMobFarm.WOODEN_MOB_FARM.get())));

    public static final Registrar<MenuType<?>> MENUS = MANAGER.get().get(Registries.MENU);

    public static final RegistrySupplier<MenuType<MobFarmMenu>> MOB_FARM_CONTAINER = MENUS.register(getId("mob_farm_menu"), () -> new MenuType<>(MobFarmMenu::new, FeatureFlags.VANILLA_SET));

    public static final Registrar<Block> BLOCKS = MANAGER.get().get(Registries.BLOCK);

    public static final RegistrySupplier<Block> WOODEN_MOB_FARM_BLOCK = BLOCKS.register(getId("wood_farm"), () -> new MobFarmBlock(EnumMobFarm.WOOD));
    public static final RegistrySupplier<Block> STONE_MOB_FARM_BLOCK = BLOCKS.register(getId("stone_farm"), () -> new MobFarmBlock(EnumMobFarm.STONE));
    public static final RegistrySupplier<Block> IRON_MOB_FARM_BLOCK = BLOCKS.register(getId("iron_farm"), () -> new MobFarmBlock(EnumMobFarm.IRON));
    public static final RegistrySupplier<Block> GOLD_MOB_FARM_BLOCK = BLOCKS.register(getId("gold_farm"), () -> new MobFarmBlock(EnumMobFarm.GOLD));
    public static final RegistrySupplier<Block> DIAMOND_MOB_FARM_BLOCK = BLOCKS.register(getId("diamond_farm"), () -> new MobFarmBlock(EnumMobFarm.DIAMOND));
    public static final RegistrySupplier<Block> EMERALD_MOB_FARM_BLOCK = BLOCKS.register(getId("emerald_farm"), () -> new MobFarmBlock(EnumMobFarm.EMERALD));
    public static final RegistrySupplier<Block> INFERNAL_MOB_FARM_BLOCK = BLOCKS.register(getId("inferno_farm"), () -> new MobFarmBlock(EnumMobFarm.INFERNAL));
    public static final RegistrySupplier<Block> ULTIMATE_MOB_FARM_BLOCK = BLOCKS.register(getId("ultimate_farm"), () -> new MobFarmBlock(EnumMobFarm.ULTIMATE));

    public static final Registrar<BlockEntityType<?>> BLOCK_ENTITIES = MANAGER.get().get(Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<MobFarmBlockEntity>> MOB_FARM_TILE_ENTITY = BLOCK_ENTITIES.register(getId("mob_farm_block_entity"), () -> BlockEntityType.Builder.of(MobFarmBlockEntity::new, TinyMobFarm.WOODEN_MOB_FARM_BLOCK.get(), TinyMobFarm.STONE_MOB_FARM_BLOCK.get(), TinyMobFarm.IRON_MOB_FARM_BLOCK.get(), TinyMobFarm.GOLD_MOB_FARM_BLOCK.get(), TinyMobFarm.DIAMOND_MOB_FARM_BLOCK.get(), TinyMobFarm.EMERALD_MOB_FARM_BLOCK.get(), TinyMobFarm.INFERNAL_MOB_FARM_BLOCK.get(), TinyMobFarm.ULTIMATE_MOB_FARM_BLOCK.get()).build(null));

    public static final Registrar<Item> ITEMS = MANAGER.get().get(Registries.ITEM);

    public static final RegistrySupplier<Item> LASSO = ITEMS.register(getId("lasso"), () -> new LassoItem(new Item.Properties()));

    public static final RegistrySupplier<Item> WOODEN_MOB_FARM = ITEMS.register(getId("wood_farm"), () -> new MobFarmBlockItem((MobFarmBlock) TinyMobFarm.WOODEN_MOB_FARM_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> STONE_MOB_FARM = ITEMS.register(getId("stone_farm"), () -> new MobFarmBlockItem((MobFarmBlock) TinyMobFarm.STONE_MOB_FARM_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> IRON_MOB_FARM = ITEMS.register(getId("iron_farm"), () -> new MobFarmBlockItem((MobFarmBlock) TinyMobFarm.IRON_MOB_FARM_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> GOLD_MOB_FARM = ITEMS.register(getId("gold_farm"), () -> new MobFarmBlockItem((MobFarmBlock) TinyMobFarm.GOLD_MOB_FARM_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> DIAMOND_MOB_FARM = ITEMS.register(getId("diamond_farm"), () -> new MobFarmBlockItem((MobFarmBlock) TinyMobFarm.DIAMOND_MOB_FARM_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> EMERALD_MOB_FARM = ITEMS.register(getId("emerald_farm"), () -> new MobFarmBlockItem((MobFarmBlock) TinyMobFarm.EMERALD_MOB_FARM_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> INFERNAL_MOB_FARM = ITEMS.register(getId("inferno_farm"), () -> new MobFarmBlockItem((MobFarmBlock) TinyMobFarm.INFERNAL_MOB_FARM_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> ULTIMATE_MOB_FARM = ITEMS.register(getId("ultimate_farm"), () -> new MobFarmBlockItem((MobFarmBlock) TinyMobFarm.ULTIMATE_MOB_FARM_BLOCK.get(), new Item.Properties()));

    public static void init() {
        ConfigTinyMobFarm.init();
    }

    public static MutableComponent translatable(String s) {
        return translatable(s, new Object[0]);
    }

    public static MutableComponent translatable(String s, Object... objects) {
        return Component.translatable(MOD_ID + "." + s, objects);
    }

    public static MutableComponent translatable(String s, ChatFormatting color) {
        MutableComponent component = translatable(s);
        component.withStyle(color);
        return component;
    }

    public static MutableComponent translatable(String s, ChatFormatting color, Object... objects) {
        MutableComponent component = translatable(s, objects);
        component.withStyle(color);
        return component;
    }

    public static Component literal(String str) {
        return Component.literal(str);
    }

    public static ResourceLocation getId(String str) {
        return new ResourceLocation(MOD_ID, str);
    }

}
