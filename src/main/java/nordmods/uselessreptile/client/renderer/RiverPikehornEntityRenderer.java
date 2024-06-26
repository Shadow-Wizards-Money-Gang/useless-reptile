package nordmods.uselessreptile.client.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import nordmods.uselessreptile.client.renderer.base.URDragonRenderer;
import nordmods.uselessreptile.client.renderer.layers.DragonMainHandItemLayer;
import nordmods.uselessreptile.common.entity.RiverPikehornEntity;

public class RiverPikehornEntityRenderer extends URDragonRenderer<RiverPikehornEntity> {
    public RiverPikehornEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager);
        addRenderLayer(new DragonMainHandItemLayer<>(this));
        shadowRadius = 0.4f;
    }

    //todo come up with a way of redering geo model rather than entity model
    @Override
    public void render(RiverPikehornEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if (entity.getVehicle() instanceof PlayerEntity) return;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
