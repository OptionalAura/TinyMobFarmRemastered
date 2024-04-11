package com.daqem.tinymobfarm;

import com.supermartijn642.configlib.api.ConfigBuilders;
import com.supermartijn642.configlib.api.IConfigBuilder;

import java.util.function.Supplier;

public class ConfigTinyMobFarm {

	public static final Supplier<Integer> lassoDurability;
	public static final Supplier<Boolean> allowLassoLooting;

	public static final Supplier<Double> woodFarmSpeed;
	public static final Supplier<Double> stoneFarmSpeed;
	public static final Supplier<Double> ironFarmSpeed;
	public static final Supplier<Double> goldFarmSpeed;
	public static final Supplier<Double> diamondFarmSpeed;
	public static final Supplier<Double> emeraldFarmSpeed;
	public static final Supplier<Double> infernoFarmSpeed;
	public static final Supplier<Double> ultimateFarmSpeed;

	static {
		IConfigBuilder config = ConfigBuilders.newTomlConfig(TinyMobFarm.MOD_ID, null, false);

		lassoDurability = config
				.comment("The durability of the lasso.")
				.define("Lasso Durability", 256, 1, Integer.MAX_VALUE);

		allowLassoLooting = config
				.comment("Whether the looting enchantment will be taken into consideration when generating mob loot.")
				.define("Allow Lasso Looting", true);

		woodFarmSpeed = config
				.comment("The speed of the wood farm.")
				.define("Wood Farm Speed", 50.0, 0.001, Double.MAX_VALUE);

		stoneFarmSpeed = config
				.comment("The speed of the stone farm.")
				.define("Stone Farm Speed", 40.0, 0.001, Double.MAX_VALUE);

		ironFarmSpeed = config
				.comment("The speed of the iron farm.")
				.define("Iron Farm Speed", 30.0, 0.001, Double.MAX_VALUE);

		goldFarmSpeed = config
				.comment("The speed of the gold farm.")
				.define("Gold Farm Speed", 20.0, 0.001, Double.MAX_VALUE);

		diamondFarmSpeed = config
				.comment("The speed of the diamond farm.")
				.define("Diamond Farm Speed", 10.0, 0.001, Double.MAX_VALUE);

		emeraldFarmSpeed = config
				.comment("The speed of the emerald farm.")
				.define("Emerald Farm Speed", 5.0, 0.001, Double.MAX_VALUE);

		infernoFarmSpeed = config
				.comment("The speed of the inferno farm.")
				.define("Inferno Farm Speed", 2.5, 0.001, Double.MAX_VALUE);

		ultimateFarmSpeed = config
				.comment("The speed of the ultimate farm.")
				.define("Ultimate Farm Speed", 0.5, 0.001, Double.MAX_VALUE);


		config.build();
	}

	public static void init() {
	}
}
