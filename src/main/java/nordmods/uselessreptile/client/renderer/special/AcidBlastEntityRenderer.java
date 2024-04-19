package nordmods.uselessreptile.client.renderer.special;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import nordmods.uselessreptile.client.model.special.AcidBlastEntityModel;
import nordmods.uselessreptile.common.entity.special.AcidBlastEntity;

public class AcidBlastEntityRenderer extends GeoEntityRenderer<AcidBlastEntity> {

    public AcidBlastEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AcidBlastEntityModel());
    }

}
