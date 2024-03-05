package nordmods.uselessreptile.common.init;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.UselessReptile;

public class URConfig {
    public static final ConfigClassHandler<URConfig> CONFIG = ConfigClassHandler.createBuilder(URConfig.class)
            .id(new Identifier(UselessReptile.MODID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("uselessreptile.json5"))
                    .setJson5(true)
                    .build())
            .build();

    //COMMON
    @SerialEntry(comment = "SPAWN WEIGHT AND CHANCES")
    public int wyvernSpawnWeight = 3;
    @SerialEntry
    public int moleclawSpawnWeight = 500;
    @SerialEntry
    public int pikehornSpawnWeight = 1;
    @SerialEntry
    public int lightningChaserSpawnWeight = 0;
    @SerialEntry(comment = "Defines a chance of Lightning Chaser spawning near player during thunderstorms each 30 seconds")
    public int lightningChaserThunderstormSpawnChance = 10;
    @SerialEntry(comment = "GROUP SIZES")
    public int wyvernMinGroupSize = 1;
    @SerialEntry
    public int wyvernMaxGroupSize = 1;
    @SerialEntry
    public int moleclawMinGroupSize = 1;
    @SerialEntry
    public int moleclawMaxGroupSize = 1;
    @SerialEntry
    public int pikehornMinGroupSize = 2;
    @SerialEntry
    public int pikehornMaxGroupSize = 6;
    @SerialEntry
    public int lightningChaserMinGroupSize = 1;
    @SerialEntry
    public int lightningChaserMaxGroupSize = 1;

    @SerialEntry(comment = "BEHAVIOUR")
    public DragonGriefing allowDragonGriefing = DragonGriefing.ALL;
    @SerialEntry
    public int blockDropChance = 100;
    @SerialEntry
    public float dragonDamageMultiplier = 1;
    @SerialEntry
    public float dragonHealthMultiplier = 1;

    public static URConfig getConfig() {
        return CONFIG.instance();
    }
    public static float getHealthMultiplier() {
        return URConfig.getConfig().dragonHealthMultiplier;
    }

    public static float getDamageMultiplier() {
        return URConfig.getConfig().dragonDamageMultiplier;
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
