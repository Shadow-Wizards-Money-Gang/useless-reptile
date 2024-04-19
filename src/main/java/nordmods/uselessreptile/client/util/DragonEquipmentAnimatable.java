package nordmods.uselessreptile.client.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animatable.model.CoreGeoBone;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.item.Item;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import java.util.Map;

public class DragonEquipmentAnimatable implements GeoAnimatable, AssetCahceOwner {
    public final URDragonEntity owner;
    public final Item item;
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
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
