package com.daqem.tinymobfarm.fabric;

import com.daqem.tinymobfarm.TinyMobFarm;
import net.fabricmc.api.ModInitializer;

public class TinyMobFarmCommonFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        TinyMobFarm.init();
    }

}
