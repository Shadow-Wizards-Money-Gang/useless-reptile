package nordmods.uselessreptile.common.entity.ai.goal.common;

import net.minecraft.entity.ai.goal.AttackWithOwnerGoal;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;


public class DragonAttackWithOwnerGoal extends AttackWithOwnerGoal {

    private final URDragonEntity mob;

    public DragonAttackWithOwnerGoal(URDragonEntity tameable) {
        super(tameable);
        this.mob = tameable;
    }

    @Override
    public boolean canStart() {
        if (super.canStart())
            return mob.canTarget(mob.getOwner().getAttacking());
        else return false;
    }
}
