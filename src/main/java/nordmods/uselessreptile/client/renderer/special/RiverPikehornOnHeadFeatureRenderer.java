package nordmods.uselessreptile.client.renderer.special;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import nordmods.uselessreptile.client.util.RenderUtil;
import nordmods.uselessreptile.common.entity.RiverPikehornEntity;

import java.util.HashSet;
import java.util.UUID;

public class RiverPikehornOnHeadFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public static final HashSet<UUID> ON_HEAD = new HashSet<>();

    public RiverPikehornOnHeadFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.getFirstPassenger() instanceof RiverPikehornEntity dragon) {
            if (dragon.isInvisible()) return;
            ON_HEAD.remove(dragon.getUuid());

            ModelPart head = getContextModel().head;
            head.rotate(matrices);

            float scale = 1 / entity.getScale();
            float offsetScale = dragon.getScale() / entity.getScale();
            matrices.translate(0, -0.2960000524520874 * offsetScale - 0.5 * (1 - offsetScale), 0);
            matrices.scale(-scale, -scale, scale);

            RenderUtil.renderEntity(dragon, tickDelta, matrices, vertexConsumers, light);

            ON_HEAD.add(dragon.getUuid());
        }
    }
}
