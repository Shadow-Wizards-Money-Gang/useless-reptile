package nordmods.uselessreptile.client.config;

import dev.isxander.yacl.config.ConfigEntry;
import dev.isxander.yacl.config.GsonConfigInstance;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class URClientConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("uselessreptile_client.json");
    public static final GsonConfigInstance<URClientConfig> CONFIG = new GsonConfigInstance<>(URClientConfig.class, CONFIG_PATH);

    @ConfigEntry
    public double cameraDistanceOffset = 2;
    @ConfigEntry
    public double cameraVerticalOffset = 0;
    @ConfigEntry
    public double cameraHorizontalOffset = -1.5;
    @ConfigEntry
    public boolean enableCameraOffset = true;
    @ConfigEntry
    public boolean enableCrosshair = true;
    @ConfigEntry
    public boolean autoThirdPerson = true;
    @ConfigEntry
    public boolean disableNamedEntityModels = false;
    @ConfigEntry
    public boolean disableEmissiveTextures = false;
    @ConfigEntry
    public boolean attackBoxesInDebug = false;

    public static URClientConfig getConfig() {
        return CONFIG.getConfig();
    }

    public static void init() {
        CONFIG.load();
    }
}
