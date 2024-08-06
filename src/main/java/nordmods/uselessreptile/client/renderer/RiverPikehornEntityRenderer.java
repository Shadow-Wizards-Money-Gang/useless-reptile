package nordmods.uselessreptile.client.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;
import nordmods.uselessreptile.client.renderer.base.URDragonRenderer;
import nordmods.uselessreptile.client.renderer.layers.DragonMainHandItemLayer;
import nordmods.uselessreptile.client.renderer.special.RiverPikehornOnHeadFeatureRenderer;
import nordmods.uselessreptile.common.entity.RiverPikehornEntity;

public class RiverPikehornEntityRenderer extends URDragonRenderer<RiverPikehornEntity> {
    public RiverPikehornEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager);
        addRenderLayer(new DragonMainHandItemLayer<>(this));
        shadowRadius = 0.4f;
    }

    @Override
    public void render(RiverPikehornEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if (entity.getVehicle() instanceof PlayerEntity) {
            if (RiverPikehornOnHeadFeatureRenderer.ON_HEAD.contains(entity.getUuid())) return;
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected void applyRotations(RiverPikehornEntity animatable, MatrixStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        if (animatable.getVehicle() instanceof PlayerEntity) {
            if (LivingEntityRenderer.shouldFlipUpsideDown(animatable)) {
                poseStack.translate(0, (animatable.getHeight() + 0.1f) / nativeScale, 0);
                poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180f));
            }
        } else super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }
}
