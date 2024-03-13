package com.daqem.tinymobfarm.core.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;

public class Msg {

	public static void tellPlayer(Player player, String text) {
		player.sendSystemMessage(Component.literal(text));
	}
	
	public static MutableComponent tooltip(String text, Object... parameters) {
		MutableComponent msg = Component.translatable(text, parameters);
		msg.setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY));
		return msg;
	}
}
