package com.daqem.tinymobfarm.forge;

import com.daqem.tinymobfarm.TinyMobFarm;
import com.daqem.tinymobfarm.client.gui.MobFarmScreen;
import com.daqem.tinymobfarm.client.render.MobFarmRenderer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class SideProxyForge {

    public SideProxyForge() {
        //Run common code
    }

    public static class Client extends SideProxyForge {

        public Client() {
            //Run client code

            IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
            eventBus.addListener(this::clientSetupHandler);
            eventBus.addListener(this::registerRenderers);
        }

        @SubscribeEvent
        public void clientSetupHandler(final FMLClientSetupEvent event) {
            MenuScreens.register(TinyMobFarm.MOB_FARM_CONTAINER.get(), MobFarmScreen::new);
        }

        @SubscribeEvent
        public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(TinyMobFarm.MOB_FARM_TILE_ENTITY.get(), MobFarmRenderer::new);
        }
    }

    public static class Server extends SideProxyForge {

        public Server() {
            //Run server code
        }
    }
}
