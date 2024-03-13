package com.daqem.tinymobfarm.client.gui;

import com.daqem.tinymobfarm.TinyMobFarm;
import com.daqem.tinymobfarm.core.EnumMobFarm;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class MobFarmScreen extends AbstractContainerScreen<MobFarmMenu> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(TinyMobFarm.MOD_ID, "textures/gui/farm_gui.png");

	public MobFarmScreen(MobFarmMenu container, Inventory inv, Component text) {
		super(container, inv, text);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		graphics.drawString(this.font, this.title, (this.imageWidth - this.font.width(this.title)) / 2, 8, 4210752, false);

		String[] split = ((TranslatableContents) this.title.getContents()).getKey().split("\\.");
        String id = split[split.length - 1];
		EnumMobFarm mobFarmData = Arrays.stream(EnumMobFarm.values()).filter(farm -> farm.getRegistryName().equals(id)).findFirst().orElse(null);
		if (mobFarmData == null) return;

		ItemStack lasso = this.menu.slots.get(0).getItem();
		if (this.menu.getProgress() > 0 || !lasso.isEmpty() && !this.menu.isPowered() && mobFarmData.isLassoValid(lasso)) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			graphics.blit(TEXTURE, 48, 60, 176, 5, 80, 5);
			graphics.blit(TEXTURE, 48, 60, 176, 0, (int) (this.menu.getScaledProgress() * 80), 5);
		} else {
			String error;
			if (lasso.isEmpty()) error = "tinymobfarm.gui.no_lasso";
			else if (this.menu.isPowered()) error = "tinymobfarm.gui.redstone_disable";
			else error = "tinymobfarm.gui.higher_tier";
			graphics.drawString(this.font, Component.translatable(error), (this.imageWidth - this.font.width(Component.translatable(error))) / 2, 59, 16733525, false);
		}
	}
	
	@Override
	protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}
}
