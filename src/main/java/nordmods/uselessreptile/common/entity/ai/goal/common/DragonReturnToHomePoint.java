package nordmods.uselessreptile.common.entity.ai.goal.common;

import net.minecraft.entity.ai.goal.Goal;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;

import java.util.EnumSet;

public class DragonReturnToHomePoint extends Goal {
    private final URDragonEntity entity;
    private final int toleranceDistance = 20;

    public DragonReturnToHomePoint(URDragonEntity entity) {
        this.entity = entity;
        setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
    }

    @Override
    public boolean canStart() {
        return entity.isTamed() && entity.squaredDistanceTo(entity.getHomePoint().toCenterPos()) > toleranceDistance * toleranceDistance;
    }

    @Override
    public boolean shouldContinue(){
        return entity.squaredDistanceTo(entity.getHomePoint().toCenterPos()) > toleranceDistance * toleranceDistance / 2f;
    }

    @Override
    public void tick() {
        entity.getNavigation().startMovingTo(entity.getHomePoint().getX(), entity.getHomePoint().getY(), entity.getHomePoint().getZ(), 1);
    }
}
