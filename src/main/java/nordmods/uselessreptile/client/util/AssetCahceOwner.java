package nordmods.uselessreptile.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface AssetCahceOwner {
    @Environment(EnvType.CLIENT) AssetCache getAssetCache();
}
