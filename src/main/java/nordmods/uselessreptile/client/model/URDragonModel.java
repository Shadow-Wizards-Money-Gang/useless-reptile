package nordmods.uselessreptile.client.model;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.client.util.AssetCache;
import nordmods.uselessreptile.client.util.DragonAssetCache;
import nordmods.uselessreptile.client.util.ResourceUtil;
import nordmods.uselessreptile.client.util.model_data.ModelDataUtil;
import nordmods.uselessreptile.client.util.model_data.base.DragonModelData;
import nordmods.uselessreptile.client.util.model_data.base.ModelData;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import software.bernie.geckolib.model.GeoModel;

public class URDragonModel<T extends URDragonEntity> extends GeoModel<T> {
    @Override
    public Identifier getAnimationResource(T entity) {
        if (!ResourceUtil.isResourceReloadFinished) return getDefaultAnimation(entity);

        AssetCache assetCache = entity.getAssetCache();
        Identifier id = assetCache.getAnimationLocationCache();
        if (id != null) return id;

        DragonModelData data  = ModelDataUtil.getDragonModelData(entity);
        if (data != null && data.modelData().animation().isPresent()) {
            id = data.modelData().animation().get();
            if (ResourceUtil.doesExist(id)) {
                assetCache.setAnimationLocationCache(id);
                return id;
            }
        }

        id = getDefaultAnimation(entity);
        assetCache.setAnimationLocationCache(id);
        return id;
    }

    @Override
    public Identifier getModelResource(T entity) {
        if (!ResourceUtil.isResourceReloadFinished) return getDefaultModel(entity);

        AssetCache assetCache = entity.getAssetCache();
        Identifier id = assetCache.getModelLocationCache();
        if (id != null) return id;

        DragonModelData data  = ModelDataUtil.getDragonModelData(entity);
        if (data != null && data.modelData().model().isPresent()) {
            id = data.modelData().model().get();
            if (ResourceUtil.doesExist(id)) {
                assetCache.setModelLocationCache(id);
                return id;
            }
        }

        id = getDefaultModel(entity);
        assetCache.setModelLocationCache(id);
        return id;
    }

    @Override
    public Identifier getTextureResource(T entity) {
        if (!ResourceUtil.isResourceReloadFinished) return getDefaultTexture(entity);

        AssetCache assetCache = entity.getAssetCache();
        Identifier id = assetCache.getTextureLocationCache();
        if (id != null) return id;

        DragonModelData data = ModelDataUtil.getDragonModelData(entity);
        if (data != null && ResourceUtil.doesExist(data.modelData().texture())) {
            id = data.modelData().texture();
            assetCache.setTextureLocationCache(id);
            return id;
        }

        id = getDefaultTexture(entity);
        assetCache.setTextureLocationCache(id);
        return id;
    }

    protected final Identifier getDefaultTexture(T entity) {
        return UselessReptile.id("textures/entity/"+ entity.getDragonID() + "/" + entity.getDefaultVariant() + ".png");
    }

    protected final Identifier getDefaultAnimation(T entity) {
        return UselessReptile.id("animations/entity/" + entity.getDragonID() + "/" + entity.getDragonID() + ".animation.json");
    }

    protected final Identifier getDefaultModel(T entity) {
        return UselessReptile.id("geo/entity/" + entity.getDragonID() + "/" + entity.getDragonID() + ".geo.json");
    }

    @Override
    public RenderLayer getRenderType(T entity, Identifier texture) {
        if (!ResourceUtil.isResourceReloadFinished) return RenderLayer.getEntityCutout(texture);

        DragonAssetCache assetCache = entity.getAssetCache();
        RenderLayer renderType = assetCache.getRenderTypeCache();
        if (renderType != null) return renderType;

        DragonModelData data = ModelDataUtil.getDragonModelData(entity);
        if (data != null) {
            ModelData modelData = data.modelData();
            if (modelData.cull()) renderType = modelData.translucent() ? RenderLayer.getEntityTranslucentCull(texture) : RenderLayer.getEntityCutout(texture);
            else renderType = modelData.translucent() ? RenderLayer.getEntityTranslucent(texture) : RenderLayer.getEntityCutoutNoCull(texture);
            assetCache.setRenderTypeCache(renderType);
            return renderType;
        }

        renderType = RenderLayer.getEntityCutout(texture);
        assetCache.setRenderTypeCache(renderType);
        return renderType;
    }

}
