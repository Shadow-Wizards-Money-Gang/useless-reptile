package nordmods.uselessreptile.common.entity.ai.goal.common;

import net.minecraft.entity.ai.goal.TrackOwnerAttackerGoal;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;

public class DragonTrackOwnerAttackerGoal extends TrackOwnerAttackerGoal {
    private final URDragonEntity mob;
    public DragonTrackOwnerAttackerGoal(URDragonEntity mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        if (super.canStart()) return mob.canTarget(mob.getAttacker());
        else return false;
    }
}
