package com.daqem.tinymobfarm.util;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.List;

public class ConfigTinyMobFarm extends MidnightConfig {
    @Entry(min=1, max=Integer.MAX_VALUE) public static int lassoDurability = 256;

    @Entry public static boolean allowLassoLooting = true;
    @Entry public static boolean allowLassoFireAspect = true;

    @Entry public static boolean requireFarmsToHaveStorage = false;

    @Entry public static boolean chickensLayEggs = true;
    @Entry(min=0.0,max=1.0) public static double chickenEggChance = 0.05;

    @Comment(centered = true, category = "farmSpeed") public static Comment farmSpeedText;
    @Entry(min=0.001,max=Double.MAX_VALUE, category = "farmSpeed") public static double woodFarmSpeed = 50.0;
    @Entry(min=0.001,max=Double.MAX_VALUE, category = "farmSpeed") public static double stoneFarmSpeed = 40.0;
    @Entry(min=0.001,max=Double.MAX_VALUE, category = "farmSpeed") public static double ironFarmSpeed = 30.0;
    @Entry(min=0.001,max=Double.MAX_VALUE, category = "farmSpeed") public static double goldFarmSpeed = 20.0;
    @Entry(min=0.001,max=Double.MAX_VALUE, category = "farmSpeed") public static double diamondFarmSpeed = 10.0;
    @Entry(min=0.001,max=Double.MAX_VALUE, category = "farmSpeed") public static double emeraldFarmSpeed = 5.0;
    @Entry(min=0.001,max=Double.MAX_VALUE, category = "farmSpeed") public static double infernoFarmSpeed = 2.5;
    @Entry(min=0.001,max=Double.MAX_VALUE, category = "farmSpeed") public static double ultimateFarmSpeed = 0.5;

    @Entry public static List<String> restrictedMobs = List.of("minecraft:wither","minecraft:ender_dragon");
}