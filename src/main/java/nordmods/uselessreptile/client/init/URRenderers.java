package nordmods.uselessreptile.client.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import nordmods.uselessreptile.client.renderer.LightningChaserEntityRenderer;
import nordmods.uselessreptile.client.renderer.MoleclawEntityRenderer;
import nordmods.uselessreptile.client.renderer.RiverPikehornEntityRenderer;
import nordmods.uselessreptile.client.renderer.WyvernEntityRenderer;
import nordmods.uselessreptile.client.renderer.special.AcidBlastEntityRenderer;
import nordmods.uselessreptile.client.renderer.special.LightningBreathEntityRenderer;
import nordmods.uselessreptile.client.renderer.special.ShockwaveSphereEntityRenderer;
import nordmods.uselessreptile.common.init.UREntities;

@Environment(EnvType.CLIENT)
public class URRenderers {
    public static void init() {
        EntityRendererRegistry.register(UREntities.WYVERN_ENTITY, WyvernEntityRenderer::new);
        EntityRendererRegistry.register(UREntities.MOLECLAW_ENTITY, MoleclawEntityRenderer::new);
        EntityRendererRegistry.register(UREntities.RIVER_PIKEHORN_ENTITY, RiverPikehornEntityRenderer::new);
        EntityRendererRegistry.register(UREntities.LIGHTNING_CHASER_ENTITY, LightningChaserEntityRenderer::new);

        EntityRendererRegistry.register(UREntities.ACID_BLAST_ENTITY, AcidBlastEntityRenderer::new);
        EntityRendererRegistry.register(UREntities.SHOCKWAVE_SPHERE_ENTITY, ShockwaveSphereEntityRenderer::new);
        EntityRendererRegistry.register(UREntities.LIGHTNING_BREATH_ENTITY, LightningBreathEntityRenderer::new);
    }
}
