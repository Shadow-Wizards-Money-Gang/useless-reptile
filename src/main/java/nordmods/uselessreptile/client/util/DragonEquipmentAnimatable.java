package nordmods.uselessreptile.client.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

import java.util.Map;

public class DragonEquipmentAnimatable implements GeoAnimatable, AssetCahceOwner {
    public final URDragonEntity owner;
    public final Item item;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final AssetCache assetCache = new AssetCache();
    public final Map<String, CoreGeoBone> equipmentBones = new Object2ObjectOpenHashMap<>();

    public AssetCache getAssetCache() {
        return assetCache;
    }

    public DragonEquipmentAnimatable(URDragonEntity owner, Item item) {
        this.owner = owner;
        this.item = item;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<DragonEquipmentAnimatable> idle = new AnimationController<>(this, "idle", URDragonEntity.TRANSITION_TICKS, event -> {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
            return PlayState.CONTINUE;
        });
        controllers.add(idle);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return RenderUtils.getCurrentTick();
    }
}
