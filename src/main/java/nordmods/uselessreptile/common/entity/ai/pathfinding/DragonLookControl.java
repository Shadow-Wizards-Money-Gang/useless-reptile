package nordmods.uselessreptile.common.entity.ai.pathfinding;

import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.mob.MobEntity;

public class DragonLookControl extends LookControl {
    public DragonLookControl(MobEntity entity) {
        super(entity);
    }

    @Override
    protected boolean shouldStayHorizontal() {
        return false;
    }
}
