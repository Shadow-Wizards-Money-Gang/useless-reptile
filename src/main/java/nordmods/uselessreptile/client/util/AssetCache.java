package nordmods.uselessreptile.client.util;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class AssetCache {
    private Identifier modelLocationCache;
    private Identifier textureLocationCache;
    private Identifier animationLocationCache;
    private Identifier glowLayerLocationCache;
    private RenderLayer renderTypeCache;
    private boolean hasGlowing = true;

    public Identifier getGlowLayerLocationCache() {
        return glowLayerLocationCache;
    }

    public void setGlowLayerLocationCache(Identifier state) {
        glowLayerLocationCache = state;
    }

    public Identifier getModelLocationCache() {
        return modelLocationCache;
    }

    public void setModelLocationCache(Identifier state) {
        modelLocationCache = state;
    }

    public Identifier getAnimationLocationCache() {
        return animationLocationCache;
    }

    public void setAnimationLocationCache(Identifier state) {
        animationLocationCache = state;
    }

    public Identifier getTextureLocationCache() {
        return textureLocationCache;
    }

    public void setTextureLocationCache(Identifier state) {
        textureLocationCache = state;
    }

    public RenderLayer getRenderTypeCache() {
        return renderTypeCache;
    }

    public void setRenderTypeCache(RenderLayer state) {
        renderTypeCache = state;
    }

    public boolean hasGlowing() {
        return hasGlowing;
    }
    public void setHasGlowing(boolean state) {
        hasGlowing = state;
    }

    public void cleanCache() {
        modelLocationCache = null;
        textureLocationCache = null;
        animationLocationCache = null;
        glowLayerLocationCache = null;
        renderTypeCache = null;
        hasGlowing = true;
    }
}
