package nordmods.uselessreptile.client.renderer.layers;

import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.BlockAndItemGeoLayer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import nordmods.uselessreptile.client.util.DragonEquipmentAnimatable;
import org.jetbrains.annotations.Nullable;

public class BannerLayer<T extends DragonEquipmentAnimatable> extends BlockAndItemGeoLayer<T> {
    public BannerLayer(GeoRenderer<T> renderer) {
        super(renderer);
    }

    @Nullable
    @Override
    protected ItemStack getStackForBone(GeoBone bone, T animatable) {
        if (animatable.item != Items.SADDLE) return null;
        return bone.getName().equals("banner") ? animatable.owner.getEquippedStack(EquipmentSlot.OFFHAND) : null;
    }
}
