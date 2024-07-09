package nordmods.uselessreptile.common.entity.ai.goal.river_pikehorn;

import net.minecraft.entity.LivingEntity;
import nordmods.uselessreptile.common.entity.RiverPikehornEntity;
import nordmods.uselessreptile.common.entity.ai.goal.common.FlyingDragonCallBackGoal;

public class PikehornFollowGoal extends FlyingDragonCallBackGoal<RiverPikehornEntity> {
    private final int toleranceDistance = 20;

    public PikehornFollowGoal(RiverPikehornEntity entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        LivingEntity owner = entity.getOwner();
        if (owner == null) return false;
        if (entity.isLeashed() || entity.hasVehicle() || entity.isSitting()) return false;
        if (entity.getTarget() != null || entity.forceTargetInWater) return false;
        if (isFollowing) return true;

        double distance = entity.squaredDistanceTo(owner);
        int toleranceDistanceSquared = toleranceDistance * toleranceDistance;
        return distance > toleranceDistanceSquared && entity.getRandom().nextInt(toleranceDistanceSquared) < distance - toleranceDistanceSquared;
    }
}
