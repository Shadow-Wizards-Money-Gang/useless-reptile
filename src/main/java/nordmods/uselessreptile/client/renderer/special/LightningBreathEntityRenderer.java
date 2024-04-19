package nordmods.uselessreptile.client.renderer.special;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.client.renderer.LightningChaserEntityRenderer;
import nordmods.uselessreptile.client.util.RenderUtil;
import nordmods.uselessreptile.common.entity.LightningChaserEntity;
import nordmods.uselessreptile.common.entity.special.LightningBreathEntity;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

//reference: https://habr.com/ru/articles/230483/
public class LightningBreathEntityRenderer extends EntityRenderer<LightningBreathEntity> {
    public LightningBreathEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(LightningBreathEntity entity) {
        return new Identifier(UselessReptile.MODID, "textures/entity/lightning_breath/beam.png");
    }

    @Override
    public void render(LightningBreathEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        int length = entity.getBeamLength();
        if (length < 1) return;

        for (int i = 0; i < entity.lightningBreathBolts.length; i++) {
            LightningBreathEntity.LightningBreathBolt lightningBreathBolt = entity.lightningBreathBolts[i];
            if (lightningBreathBolt == null) {
                if (!(entity.getOwner() instanceof LightningChaserEntity owner)) return;
                lightningBreathBolt = new LightningBreathEntity.LightningBreathBolt();
                float offset = length / (4f + i * 2);
                Vec3f headPos = LightningChaserEntityRenderer.headPos.get(owner.getUuid());
                if (headPos == null) return;
                //because actual owner's position and lightning breath's one are never the same, and we technically render lightning breath here...
                Vec3f startPos = new Vec3f((float) (owner.getX() - entity.getX()), (float) (owner.getY() - entity.getY()), (float) (owner.getZ() - entity.getZ()));
                startPos.add(headPos);
                Vec3d og = owner.getRotationVector().multiply(length);
                Vec3f vec3d = new Vec3f((float) og.x, (float) og.y, (float) og.z);
                lightningBreathBolt.segments.add(
                        new LightningBreathEntity.LightningBreathBolt.Segment(
                                new Vec3d(startPos.getX(), startPos.getY(), startPos.getZ()),
                                new Vec3d(vec3d.getX() + startPos.getX(), vec3d.getY() + startPos.getY(), vec3d.getZ() + startPos.getZ())));
                for (int l = 0; l < 3; l++) {
                    //do not the foreach unless you want to cause infinite loop
                    int listSize = lightningBreathBolt.segments.size();
                    for (int j = 0; j < listSize; j++) {
                        LightningBreathEntity.LightningBreathBolt.Segment segment = lightningBreathBolt.segments.get(j);
                        lightningBreathBolt.segments.remove(segment);
                        Vec3d start = segment.startPoint();
                        Vec3d end = segment.endPoint();
                        Random random = new Random(l + owner.getRandom().nextInt(100));
                        Vec3d mid = new Vec3d(
                                ((start.x + end.x) / 2f + random.nextFloat() * offset * 2f - offset),
                                ((start.y + end.y) / 2f + random.nextFloat() * offset * 2f - offset),
                                ((start.z + end.z) / 2f + random.nextFloat() * offset * 2f - offset));
                        lightningBreathBolt.segments.add(new LightningBreathEntity.LightningBreathBolt.Segment(start, mid));
                        lightningBreathBolt.segments.add(new LightningBreathEntity.LightningBreathBolt.Segment(mid, end));
                    }
                    offset /= 2f;
                }
                entity.lightningBreathBolts[i] = lightningBreathBolt;
            }
        }

        float alpha = MathHelper.clamp(1f - (entity.getAge() < 3 ? 0 : (float) entity.getAge() / LightningBreathEntity.MAX_AGE), 0f, 1f);
        alpha = MathHelper.lerp(tickDelta, entity.prevAlpha, alpha);
        entity.prevAlpha = alpha;
        matrices.push();
        for (LightningBreathEntity.LightningBreathBolt lightningBreathBolt : entity.lightningBreathBolts)
            for (int i = 0; i < lightningBreathBolt.segments.size(); i++) {
                LightningBreathEntity.LightningBreathBolt.Segment current = lightningBreathBolt.segments.get(i);

                Vec3f v0 = new Vec3f(current.startPoint());
                Vec3f v1 = new Vec3f(current.startPoint());
                Vec3f v2 = new Vec3f(current.endPoint());
                Vec3f v3 = new Vec3f(current.endPoint());

                v0.add(0, 0.1f, 0);
                v1.add(0, -0.1f, 0);
                v2.add(0, -0.1f, 0);
                v3.add(0, 0.1f, 0);

                RenderUtil.renderQuad(matrices.peek().getPositionMatrix(), matrices.peek().getNormalMatrix(),
                        vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(getTexture(entity))),
                        v0, v1, v2, v3,
                        alpha, 1, 1, 1, LightmapTextureManager.MAX_LIGHT_COORDINATE,
                        0, 1, 0, 1);

                v0.add(0, 0.1f, 0);
                v1.add(0, -0.1f, 0);
                v2.add(0, -0.1f, 0);
                v3.add(0, 0.1f, 0);

                RenderUtil.renderQuad(matrices.peek().getPositionMatrix(), matrices.peek().getNormalMatrix(),
                        vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(getTexture(entity))),
                        v0, v1, v2, v3,
                        alpha, 1, 1, 1, LightmapTextureManager.MAX_LIGHT_COORDINATE,
                        0, 1, 0, 1);

                v0.add(0, 0.1f, 0);
                v1.add(0, -0.1f, 0);
                v2.add(0, -0.1f, 0);
                v3.add(0, 0.1f, 0);

                RenderUtil.renderQuad(matrices.peek().getPositionMatrix(), matrices.peek().getNormalMatrix(),
                        vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(getTexture(entity))),
                        v0, v1, v2, v3,
                        alpha, 1, 1, 1, LightmapTextureManager.MAX_LIGHT_COORDINATE,
                        0, 1, 0, 1);
        }
        matrices.pop();
    }

}

