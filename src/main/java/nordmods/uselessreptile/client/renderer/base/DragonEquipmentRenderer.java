package nordmods.uselessreptile.client.renderer.base;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import nordmods.uselessreptile.client.model.special.DragonEqupmentModel;
import nordmods.uselessreptile.client.renderer.layers.BannerLayer;
import nordmods.uselessreptile.client.renderer.layers.DragonPassengerLayer;
import nordmods.uselessreptile.client.renderer.layers.URGlowingLayer;
import nordmods.uselessreptile.client.util.DragonEquipmentAnimatable;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

public class DragonEquipmentRenderer extends GeoObjectRenderer<DragonEquipmentAnimatable> {
    private float yaw = 0;
    private float roll = 0;
    public DragonEquipmentRenderer() {
        super(new DragonEqupmentModel());
        addRenderLayer(new URGlowingLayer<>(this));
        addRenderLayer(new DragonPassengerLayer<>(this, "rider"));
        addRenderLayer(new BannerLayer<>(this));
    }

    //have to override that because for some reason they give offset for matrix by 0.5 on each axis
    @Override
    public void preRender(MatrixStack poseStack, DragonEquipmentAnimatable animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        this.objectRenderTranslations = new Matrix4f(poseStack.peek().getPositionMatrix());

        URDragonEntity owner = animatable.owner;

        float yaw = MathHelper.lerpAngleDegrees(partialTick, owner.prevBodyYaw, owner.bodyYaw);
        if (owner.isFrozen())
            yaw += (float)(Math.cos(owner.age * 3.25d) * Math.PI * 0.4d);
        this.yaw = 180f - yaw;
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(this.yaw));

        this.roll = 0;
        if (owner.deathTime > 0) {
            float deathRotation = (owner.deathTime + partialTick - 1f) / 20f * 1.6f;
            this.roll = Math.min(MathHelper.sqrt(deathRotation), 1) * 90f;
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(this.roll));
        }

        scaleWidth = scaleHeight = owner.getScale();
        scaleModelForRender(scaleWidth, scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
        poseStack.translate(0, 0.01, 0);
    }

    //todo fix z-fighting
    //*screams*
    @Override
    public void preApplyRenderLayers(MatrixStack poseStack, DragonEquipmentAnimatable animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource,
                                     VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        poseStack.push();
        poseStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(roll));
        poseStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(yaw));
        poseStack.translate(0, -0.01, 0);
        super.preApplyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        poseStack.pop();
    }

    @Override
    public void applyRenderLayers(MatrixStack poseStack, DragonEquipmentAnimatable animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource,
                                  VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        poseStack.push();
        poseStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(roll));
        poseStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(yaw));
        poseStack.translate(0, -0.01, 0);
        super.applyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        poseStack.pop();
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, DragonEquipmentAnimatable animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
                                  int packedOverlay, int colour) {
        //TODO: aaaand it's back
        if (buffer instanceof BufferBuilder builder && !builder.building) buffer = bufferSource.getBuffer(renderType);

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }
}