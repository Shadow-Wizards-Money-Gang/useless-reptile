package nordmods.uselessreptile.client.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import nordmods.uselessreptile.client.renderer.base.URDragonRenderer;
import nordmods.uselessreptile.common.entity.MoleclawEntity;

public class MoleclawEntityRenderer extends URDragonRenderer<MoleclawEntity> {
    public MoleclawEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager);
        shadowRadius = 1.25f;
    }
}
