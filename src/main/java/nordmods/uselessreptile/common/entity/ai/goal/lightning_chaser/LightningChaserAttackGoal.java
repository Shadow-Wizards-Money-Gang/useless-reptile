package nordmods.uselessreptile.common.entity.ai.goal.lightning_chaser;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nordmods.uselessreptile.common.entity.LightningChaserEntity;
import nordmods.uselessreptile.common.entity.special.LightningBreathEntity;
import nordmods.uselessreptile.common.entity.special.ShockwaveSphereEntity;

import java.util.EnumSet;
import java.util.List;

public class LightningChaserAttackGoal extends Goal {

    private final LightningChaserEntity entity;
    private LivingEntity target;
    private int attackCooldown = 20;
    private final int minDistanceSquared = 40;
    private final int maxDistanceSquared = 841;

    public LightningChaserAttackGoal(LightningChaserEntity entity) {
        this.entity = entity;
        setControls(EnumSet.of(Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (entity.hasSurrendered() || entity.getShouldBailOut()) return false;

        if (entity.canBeControlledByRider()) return false;
        if (entity.isTargetFriendly(entity.getTarget())) entity.setTarget(null);
        target = entity.getTarget();
        return target != null;
    }

    @Override
    public boolean shouldContinue() {
        if (target == null) return false;
        if (!target.isAlive()) return false;
        return !entity.getNavigation().isIdle() || canStart();
    }

    @Override
    public void stop() {
        target = null;
        entity.setTarget(null);
        entity.getNavigation().stop();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (target == null || target.isRemoved()) {
            stop();
            return;
        }
        entity.setSprinting(true);
        entity.lookAt(target);

        double distance = entity.squaredDistanceTo(target);
        if (distance > maxDistanceSquared) entity.getNavigation().startMovingTo(target, 1);
        if (distance < minDistanceSquared) entity.getMoveControl().moveBack();

        if (--attackCooldown <= 0) {
            if (tryMeleeAttack()) return;
            if (tryRangedAttack()) return;
            if (tryShockwaveAttack()) return;
        }
    }

    private boolean tryMeleeAttack() {
        if (entity.getSecondaryAttackCooldown() > 0) return false;
        if (entity.isFlying()) return false;
        boolean doesCollide = entity.doesCollide(entity.getAttackBox(), target.getBoundingBox());
        if (!doesCollide) return false;
        entity.meleeAttack(target);
        return true;
    }

    private boolean tryRangedAttack() {
        if (entity.getPrimaryAttackCooldown() > 0) return false;
        double distance = entity.squaredDistanceTo(target);
        if (distance > LightningBreathEntity.MAX_LENGTH * LightningBreathEntity.MAX_LENGTH * 0.81 || distance < minDistanceSquared) return false;
        entity.triggerShoot();
        return true;
    }

    private boolean tryShockwaveAttack() {
        if (entity.getSecondaryAttackCooldown() > 0) return false;
        if (!entity.isFlying()) return false;
        double attackDistance = ShockwaveSphereEntity.MAX_RADIUS * ShockwaveSphereEntity.MAX_RADIUS * 0.49;
        List<Entity> projectiles = entity.getWorld().getOtherEntities(entity, new Box(entity.getBlockPos()).expand(attackDistance * 2), c -> c instanceof ProjectileEntity projectile && projectile.getOwner() == target && !projectile.getVelocity().equals(Vec3d.ZERO));
        if (!projectiles.isEmpty()) {
            entity.triggerShockwave();
            return true;
        }
        double distance = entity.squaredDistanceTo(target);
        if (attackDistance < distance) return false;
        entity.triggerShockwave();
        return true;
    }
}
