package nordmods.uselessreptile.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import nordmods.uselessreptile.common.entity.base.URRideableDragonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;moveBy(DDD)V"))
    public void offset(Args args) {
        if (MinecraftClient.getInstance().player.getVehicle() instanceof URRideableDragonEntity) {
            args.set(1, 0);
            args.set(2, -1.5);
        }
    }

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(D)D"))
    public double offsetCameraDistance(double desiredCameraDistance) {
        if (MinecraftClient.getInstance().player.getVehicle() instanceof URRideableDragonEntity) return desiredCameraDistance + 2;
        else return desiredCameraDistance;
    }

}
