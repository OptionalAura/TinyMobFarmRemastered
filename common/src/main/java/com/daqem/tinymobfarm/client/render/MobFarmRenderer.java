package com.daqem.tinymobfarm.client.render;

import com.daqem.tinymobfarm.common.blockentity.MobFarmBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public class MobFarmRenderer implements BlockEntityRenderer<MobFarmBlockEntity> {

    public MobFarmRenderer(@SuppressWarnings("unused") BlockEntityRendererProvider.Context context) {
    }

	@Override
	public void render(MobFarmBlockEntity mobFarmBlockEntity, float partialTicks, PoseStack matrix,
					   MultiBufferSource typeBuffer, int combinedLight, int combinedOverlay) {
		
		LivingEntity model = mobFarmBlockEntity.getModel();
		if (model != null) {
			float modelHorizontalAngle = -mobFarmBlockEntity.getModelFacing().toYRot();
			
			AABB box = model.getBoundingBox();
			double length = Math.max(
					Math.max(box.maxX - box.minX, box.maxY - box.minY), box.maxZ - box.minZ);
			float modelScale = (float) (0.5 / length);
			
			matrix.pushPose();
			matrix.translate(0.5, 0.125, 0.5);
			matrix.scale(modelScale, modelScale, modelScale);
			
			matrix.mulPose(Axis.YP.rotationDegrees(modelHorizontalAngle));
			
			Minecraft.getInstance().getEntityRenderDispatcher().render(model, 0, 0, 0, 0,
					partialTicks, matrix, typeBuffer, combinedLight);
			matrix.popPose();
		}
	}
}
