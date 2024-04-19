package nordmods.uselessreptile.client.util;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class RenderUtil {
    public static void renderQuad(
            Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices,
            Vec3f v0, Vec3f v1, Vec3f v2, Vec3f v3,
            float a, float r, float g, float b, int light,
            float minU, float maxU, float minV, float maxV
    ) {
        vertices.vertex(positionMatrix, v0.getX(), v0.getY(), v0.getZ()) //00
                .color(r, g, b, a).texture(minU, minV)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .next();
        vertices.vertex(positionMatrix, v1.getX(), v1.getY(), v1.getZ()) //10
                .color(r, g, b, a).texture(maxU, minV)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .next();
        vertices.vertex(positionMatrix, v2.getX(), v2.getY(), v2.getZ()) //11
                .color(r, g, b, a).texture(maxU, maxV)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .next();
        vertices.vertex(positionMatrix, v3.getX(), v3.getY(), v3.getZ()) //01
                .color(r, g, b, a).texture(minU, maxV)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .next();
    }
}
