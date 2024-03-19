package com.daqem.tinymobfarm.client.render;

import com.daqem.tinymobfarm.blockentity.MobFarmBlockEntity;

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
	public void render(MobFarmBlockEntity mobFarmBlockEntity, float partialTicks, PoseStack poseStack,
					   MultiBufferSource typeBuffer, int combinedLight, int combinedOverlay) {
		
		LivingEntity livingEntity = mobFarmBlockEntity.getLivingEntity();
		if (livingEntity != null) {
			float modelHorizontalAngle = -mobFarmBlockEntity.getModelFacing().toYRot();

			AABB box = livingEntity.getBoundingBox();
			double length = Math.max(Math.max(box.maxX - box.minX, box.maxY - box.minY), box.maxZ - box.minZ);
			float modelScale = (float) (0.5 / length);

			poseStack.pushPose();
			poseStack.translate(0.5, 0.125, 0.5);
			poseStack.scale(modelScale, modelScale, modelScale);

			poseStack.mulPose(Axis.YP.rotationDegrees(modelHorizontalAngle));

			Minecraft.getInstance().getEntityRenderDispatcher().render(livingEntity, 0, 0, 0, 0,
					partialTicks, poseStack, typeBuffer, combinedLight);
			poseStack.popPose();
		}
	}
}
