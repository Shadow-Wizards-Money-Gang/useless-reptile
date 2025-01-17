package nordmods.uselessreptile.mixin.common;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import nordmods.uselessreptile.common.util.LightningChaserSpawnTimer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("WrongEntityDataParameterClass")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements LightningChaserSpawnTimer {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private static final TrackedData<Integer> LIGHTNING_CHASER_SPAWN_COOLDOWN = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void trackTimer(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(LIGHTNING_CHASER_SPAWN_COOLDOWN, 0);
    }

    @Inject(method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void writeToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("LightningChaserSpawnCooldown", useless_reptile$getTimer());
    }

    @Inject(method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void readFromNbt(NbtCompound nbt, CallbackInfo ci) {
        useless_reptile$setTimer(nbt.getInt("LightningChaserSpawnCooldown"));
    }

    public int useless_reptile$getTimer() {
        return dataTracker.get(LIGHTNING_CHASER_SPAWN_COOLDOWN);
    }
    public void useless_reptile$setTimer(int state) {
        dataTracker.set(LIGHTNING_CHASER_SPAWN_COOLDOWN, state);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickTimer(CallbackInfo ci) {
        if (useless_reptile$getTimer() > 0) useless_reptile$setTimer(useless_reptile$getTimer() - 1);
    }
}
