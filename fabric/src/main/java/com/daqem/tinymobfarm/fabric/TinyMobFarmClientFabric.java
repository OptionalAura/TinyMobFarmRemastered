package com.daqem.tinymobfarm.fabric;

import com.daqem.tinymobfarm.TinyMobFarm;
import com.daqem.tinymobfarm.client.gui.MobFarmScreen;
import com.daqem.tinymobfarm.client.render.MobFarmRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class TinyMobFarmClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(TinyMobFarm.MOB_FARM_CONTAINER.get(), MobFarmScreen::new);
        BlockRenderLayerMap.INSTANCE.putBlock(TinyMobFarm.WOODEN_MOB_FARM_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TinyMobFarm.STONE_MOB_FARM_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TinyMobFarm.IRON_MOB_FARM_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TinyMobFarm.GOLD_MOB_FARM_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TinyMobFarm.DIAMOND_MOB_FARM_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TinyMobFarm.EMERALD_MOB_FARM_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TinyMobFarm.INFERNAL_MOB_FARM_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TinyMobFarm.ULTIMATE_MOB_FARM_BLOCK.get(), RenderType.cutout());
        BlockEntityRenderers.register(TinyMobFarm.MOB_FARM_TILE_ENTITY.get(), MobFarmRenderer::new);
    }
}
