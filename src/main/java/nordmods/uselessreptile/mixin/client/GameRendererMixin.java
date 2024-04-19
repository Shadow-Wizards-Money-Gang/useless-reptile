package nordmods.uselessreptile.mixin.client;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
//it's wip anyway
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    /*@Unique private float prevStrength;
    @Shadow @Final MinecraftClient client;

    @Shadow @Final private BufferBuilderStorage buffers;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void renderShockOverlay(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (client.player.hasStatusEffect(URStatusEffects.SHOCK)) {
            DrawContext drawContext = new DrawContext(client, buffers.getEntityVertexConsumers());
            float strength = MathHelper.clamp(client.player.getStatusEffect(URStatusEffects.SHOCK).getDuration()/100f, 0f, 1f);
            renderShockOverlay(drawContext, strength, tickDelta);
            prevStrength = strength;
        } else prevStrength = 1f;
    }

    private void renderShockOverlay(DrawContext context, float strength, float tickDelta) {
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        context.getMatrices().push();
        float scale = MathHelper.clamp(1.5f - MathHelper.lerp(tickDelta, prevStrength, strength), 1f, 2f);
        context.getMatrices().translate(width/2f, height/2f, 0f);
        context.getMatrices().scale(scale, scale, scale);
        context.getMatrices().translate(-width/2f, -height/2f, 0f);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE);

        float r = 0.72f * strength;
        float g = 0.82f * strength;
        float b = 0.9f * strength;
        context.setShaderColor(r, g, b, 1f);
        context.drawTexture(new Identifier("textures/misc/nausea.png"), 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
        context.setShaderColor(1f, 1f, 1f, 1f);

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        context.getMatrices().pop();
    }*/
}
