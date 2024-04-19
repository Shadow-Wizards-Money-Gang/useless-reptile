package nordmods.uselessreptile.client.renderer.base;

import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import nordmods.uselessreptile.client.renderer.layers.URGlowingLayer;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;

public abstract class URDragonRenderer <T extends URDragonEntity> extends GeoEntityRenderer<T> {
    public URDragonRenderer(EntityRendererFactory.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
        addRenderLayer(new URGlowingLayer<>(this));
    }
}
