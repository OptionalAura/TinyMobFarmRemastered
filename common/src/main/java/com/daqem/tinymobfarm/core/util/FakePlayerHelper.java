package com.daqem.tinymobfarm.core.util;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class FakePlayerHelper {

	public static final String FAKE_NAME = "[TinyMobFarm_DanielTheEgg]";
	
	private static final GameProfile GAME_PROFILE = new GameProfile(UUID.randomUUID(), FAKE_NAME);
	private static ServerPlayer fakePlayer;
	
	public static ServerPlayer getPlayer(ServerLevel world) {
		if (fakePlayer == null) fakePlayer = new ServerPlayer(world.getServer(), world, GAME_PROFILE);
		return fakePlayer;
	}
}
