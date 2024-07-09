package nordmods.uselessreptile.common.entity.ai.goal.common;

import net.minecraft.entity.ai.goal.RevengeGoal;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;

public class DragonRevengeGoal extends RevengeGoal {

    private final URDragonEntity mob;
    public DragonRevengeGoal(URDragonEntity mob, Class<?>... noRevengeTypes) {
        super(mob, noRevengeTypes);
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        if (super.canStart()) return mob.canTarget(mob.getAttacker());
        else return false;
    }
}
