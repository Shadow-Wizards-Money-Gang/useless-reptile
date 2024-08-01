package nordmods.uselessreptile.client.renderer.special;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import nordmods.uselessreptile.client.util.RenderUtil;
import nordmods.uselessreptile.common.entity.RiverPikehornEntity;

import java.util.HashSet;
import java.util.UUID;

public class RiverPikehornOnHeadFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public static final HashSet<UUID> ON_HEAD = new HashSet<>();

    public RiverPikehornOnHeadFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context, EntityModelLoader loader) {
        super(context);
    }

    //todo: fix rendering when riding other dragons
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.getFirstPassenger() instanceof RiverPikehornEntity dragon) {
            if (dragon.isInvisible()) return;

            matrices.push();
            ON_HEAD.remove(dragon.getUuid());

            ModelPart head = getContextModel().head;
            head.rotate(matrices);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotation(head.yaw - MathHelper.wrapDegrees(entity.getYaw(tickDelta)) * MathHelper.RADIANS_PER_DEGREE));

            float scale = 1 / entity.getScale();
            Vec3d vec3d = dragon.getVehicleAttachmentPos(entity);
            matrices.translate(vec3d.x * scale, -0.25 * 1/scale, vec3d.z * scale);
            matrices.scale(scale, -scale, -scale);

            RenderUtil.renderEntity(dragon, tickDelta, matrices, vertexConsumers, light);

            ON_HEAD.add(dragon.getUuid());
            matrices.pop();
        }
    }
}
