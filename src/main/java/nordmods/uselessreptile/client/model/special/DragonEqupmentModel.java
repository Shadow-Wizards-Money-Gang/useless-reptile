package nordmods.uselessreptile.client.model.special;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.client.util.AssetCache;
import nordmods.uselessreptile.client.util.DragonEquipmentAnimatable;
import nordmods.uselessreptile.client.util.ResourceUtil;
import nordmods.uselessreptile.client.util.model_data.ModelDataUtil;
import nordmods.uselessreptile.client.util.model_data.base.EquipmentModelData;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;

public class DragonEqupmentModel extends GeoModel<DragonEquipmentAnimatable> {

    @Override
    @Nullable
    public Identifier getModelResource(DragonEquipmentAnimatable entity) {
        AssetCache assetCache = entity.getAssetCache();
        if (!ResourceUtil.isResourceReloadFinished) {
            assetCache.setModelLocationCache(null);
            return null;
        }
        Identifier id = assetCache.getModelLocationCache();
        if (id != null) return id;

        EquipmentModelData data = ModelDataUtil.getEquipmentModelData(entity.owner, entity.item);
        if (data != null) {
            id = data.modelData().model();
            assetCache.setModelLocationCache(id);
            return id;
        }

        return null;
    }

    @Override
    @Nullable
    public Identifier getTextureResource(DragonEquipmentAnimatable entity) {
        AssetCache assetCache = entity.getAssetCache();
        if (!ResourceUtil.isResourceReloadFinished) {
            assetCache.setTextureLocationCache(null);
            return null;
        }
        Identifier id = assetCache.getTextureLocationCache();
        if (id != null) return id;

        EquipmentModelData data = ModelDataUtil.getEquipmentModelData(entity.owner, entity.item);
        if (data != null) {
            id = data.modelData().texture();
            assetCache.setTextureLocationCache(id);
            return id;
        }

        return null;
    }

    @Override
    @Nullable
    public Identifier getAnimationResource(DragonEquipmentAnimatable entity) {
        AssetCache assetCache = entity.getAssetCache();
        if (!ResourceUtil.isResourceReloadFinished) {
            assetCache.setAnimationLocationCache(null);
            return null;
        }
        Identifier id = assetCache.getAnimationLocationCache();
        if (id != null) return id;

        EquipmentModelData data = ModelDataUtil.getEquipmentModelData(entity.owner, entity.item);
        if (data != null) {
            id = data.modelData().animation();
            assetCache.setAnimationLocationCache(id);
            return id;
        }

        return null;
    }

    @Override
    public RenderLayer getRenderType(DragonEquipmentAnimatable entity, Identifier texture) {
        if (!ResourceUtil.isResourceReloadFinished) return RenderLayer.getEntityCutout(texture);

        AssetCache assetCache = entity.getAssetCache();
        RenderLayer renderType = assetCache.getRenderTypeCache();
        if (renderType != null) return renderType;

        EquipmentModelData data = ModelDataUtil.getEquipmentModelData(entity.owner, entity.item);
        if (data != null) {
            renderType = data.modelData().renderType();
            assetCache.setRenderTypeCache(renderType);
            return renderType;
        }

        renderType = RenderLayer.getEntityCutout(texture);
        assetCache.setRenderTypeCache(renderType);
        return renderType;
    }
}
