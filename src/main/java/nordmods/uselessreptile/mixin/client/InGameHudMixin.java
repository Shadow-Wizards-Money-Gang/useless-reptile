package nordmods.uselessreptile.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.effect.StatusEffectInstance;
import nordmods.uselessreptile.client.config.URClientConfig;
import nordmods.uselessreptile.common.entity.base.URRideableDragonEntity;
import nordmods.uselessreptile.common.init.URStatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"))
    private boolean render(Perspective instance, Operation<Boolean> original) {
        if (URClientConfig.getConfig().enableCrosshair && MinecraftClient.getInstance().player.getVehicle() instanceof URRideableDragonEntity) return true;
        return original.call(instance);
    }

    @Inject(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Ljava/util/Collection;isEmpty()Z"))
    private void yeetShockEffect(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci, @Local LocalRef<Collection<StatusEffectInstance>> localRef) {
        List<StatusEffectInstance> copy = new ArrayList<>(List.copyOf(localRef.get()));
        copy.removeIf(statusEffectInstance -> statusEffectInstance.getEffectType().equals(URStatusEffects.SHOCK));
        localRef.set(copy);
    }
}
