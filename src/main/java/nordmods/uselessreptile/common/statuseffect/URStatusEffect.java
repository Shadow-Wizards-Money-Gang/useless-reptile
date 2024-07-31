package nordmods.uselessreptile.common.statuseffect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleEffect;

public class URStatusEffect extends StatusEffect {
    public URStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public URStatusEffect(StatusEffectCategory category, int color, ParticleEffect particleEffect) {
        super(category, color, particleEffect);
    }
}
