package nordmods.uselessreptile.common.config;

import dev.isxander.yacl.config.ConfigEntry;
import dev.isxander.yacl.config.GsonConfigInstance;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class URConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("uselessreptile.json");
    public static final GsonConfigInstance<URConfig> CONFIG = new GsonConfigInstance<>(URConfig.class, CONFIG_PATH);

    //COMMON
    @ConfigEntry
    public int wyvernSpawnWeight = 1;
    @ConfigEntry
    public int moleclawSpawnWeight = 1;
    @ConfigEntry
    public int pikehornSpawnWeight = 1;
    @ConfigEntry
    public int lightningChaserSpawnWeight = 0;
    @ConfigEntry
    public int lightningChaserThunderstormSpawnChance = 10;
    @ConfigEntry
    public int lightningChaserThunderstormSpawnTimerCooldown = 24000;
    @ConfigEntry
    public int dragonSpawnGroupCapacity = 2;
    @ConfigEntry
    public int smallDragonSpawnGroupCapacity = 12;
    @ConfigEntry
    public int undergroundDragonSpawnGroupCapacity = 6;
    @ConfigEntry
    public int wyvernMinGroupSize = 1;
    @ConfigEntry
    public int wyvernMaxGroupSize = 1;
    @ConfigEntry
    public int moleclawMinGroupSize = 1;
    @ConfigEntry
    public int moleclawMaxGroupSize = 1;
    @ConfigEntry
    public int pikehornMinGroupSize = 1;
    @ConfigEntry
    public int pikehornMaxGroupSize = 3;
    @ConfigEntry
    public int lightningChaserMinGroupSize = 1;
    @ConfigEntry
    public int lightningChaserMaxGroupSize = 1;
    @ConfigEntry
    public DragonGriefing allowDragonGriefing = DragonGriefing.ALL;
    @ConfigEntry
    public int blockDropChance = 100;
    @ConfigEntry
    public boolean dragonMadness = false;

    public static URConfig getConfig() {
        return CONFIG.getConfig();
    }

    public enum DragonGriefing {
        ALL(true, true),
        TAMED(false, true),
        DISABLED(false, false);

        private final boolean untamedBreaking;
        private final boolean tamedBreaking;

        DragonGriefing(boolean untamedBreaking, boolean tamedBreaking) {
            this.untamedBreaking = untamedBreaking;
            this.tamedBreaking = tamedBreaking;
        }

        public boolean canUntamedBreak() {
            return untamedBreaking;
        }

        public boolean canTamedBreak() {
            return tamedBreaking;
        }
    }

    public static void init(){
        CONFIG.load();
    }
}
