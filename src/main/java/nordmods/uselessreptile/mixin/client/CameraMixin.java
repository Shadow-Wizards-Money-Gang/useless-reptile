package nordmods.uselessreptile.mixin.client;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.BlockView;
import nordmods.uselessreptile.client.config.URClientConfig;
import nordmods.uselessreptile.common.entity.base.URRideableDragonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow private Entity focusedEntity;

    @Shadow protected abstract void moveBy(float f, float g, float h);

    @Shadow protected abstract float clipToSpace(float f);

    @Inject(method = "update", at = @At(value = "TAIL"))
    public void offsetCameraDistance(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (!URClientConfig.getConfig().enableCameraOffset) return;
        if (this.focusedEntity.getVehicle() instanceof URRideableDragonEntity dragonEntity && thirdPerson) {
            float scale = focusedEntity instanceof  LivingEntity livingEntity ? livingEntity.getScale() : 1;

            float distanceToCameraOffset = URClientConfig.getConfig().cameraDistanceOffset * dragonEntity.getScale() * scale;
            float verticalOffset = URClientConfig.getConfig().cameraVerticalOffset * dragonEntity.getScale() * scale;
            float horizontalOffset = -URClientConfig.getConfig().cameraHorizontalOffset * dragonEntity.getScale() * scale;

            moveBy(clipToSpace(distanceToCameraOffset), 0, 0);
            moveBy(0, clipToSpace(verticalOffset), 0);
            moveBy(0, 0, clipToSpace(horizontalOffset));
        }
    }
}
