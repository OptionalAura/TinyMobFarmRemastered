package com.daqem.tinymobfarm.forge;

import dev.architectury.platform.forge.EventBuses;
import com.daqem.tinymobfarm.TinyMobFarm;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TinyMobFarm.MOD_ID)
public class TinyMobFarmForge {
    public TinyMobFarmForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(TinyMobFarm.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        TinyMobFarm.init();

        DistExecutor.safeRunForDist(() -> SideProxyForge.Client::new, () -> SideProxyForge.Server::new);
    }
}
