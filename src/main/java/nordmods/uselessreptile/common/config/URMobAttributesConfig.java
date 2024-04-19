package nordmods.uselessreptile.common.config;

import dev.isxander.yacl.config.ConfigEntry;
import dev.isxander.yacl.config.GsonConfigInstance;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class URMobAttributesConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("uselessreptile_mob_attributes.json");
    public static final GsonConfigInstance<URMobAttributesConfig> CONFIG = new GsonConfigInstance<>(URMobAttributesConfig.class, CONFIG_PATH);

    @ConfigEntry
    public float dragonDamageMultiplier = 1;
    @ConfigEntry
    public float dragonKnockbackMultiplier = 1;
    @ConfigEntry
    public float dragonHealthMultiplier = 1;
    @ConfigEntry
    public float dragonArmorMultiplier = 1;
    @ConfigEntry
    public float dragonArmorToughnessMultiplier = 1;
    @ConfigEntry
    public float dragonGroundSpeedMultiplier = 1;
    @ConfigEntry
    public float dragonFlyingSpeedMultiplier = 1;

    @ConfigEntry
    public float wyvernDamage = 6.0f;
    @ConfigEntry
    public float wyvernKnockback = 0.3f;
    @ConfigEntry
    public float wyvernHealth = 50.0f;
    @ConfigEntry
    public float wyvernArmor = 4.0f;
    @ConfigEntry
    public float wyvernArmorToughness = 2.0f;
    @ConfigEntry
    public float wyvernGroundSpeed = 0.2f;
    @ConfigEntry
    public float wyvernFlyingSpeed = 0.7f;
    @ConfigEntry
    public int wyvernBaseSecondaryAttackCooldown = 30;
    @ConfigEntry
    public int wyvernBasePrimaryAttackCooldown = 80;
    @ConfigEntry
    public int wyvernBaseAccelerationDuration = 400;
    @ConfigEntry
    public float wyvernRotationSpeedGround = 8;
    @ConfigEntry
    public float wyvernRotationSpeedAir = 4;
    @ConfigEntry
    public float wyvernVerticalSpeed = 0.4f;
    @ConfigEntry
    public float wyvernRegenerationFromFood = 4;

    @ConfigEntry
    public float moleclawDamage = 8.0f;
    @ConfigEntry
    public float moleclawKnockback = 0.5f;
    @ConfigEntry
    public float moleclawHealth = 80.0f;
    @ConfigEntry
    public float moleclawArmor = 8.0f;
    @ConfigEntry
    public float moleclawArmorToughness = 4.0f;
    @ConfigEntry
    public float moleclawGroundSpeed = 0.25f;
    @ConfigEntry
    public int moleclawBaseSecondaryAttackCooldown = 30;
    @ConfigEntry
    public int moleclawBasePrimaryAttackCooldown = 60;
    @ConfigEntry
    public float moleclawRotationSpeedGround = 6;
    @ConfigEntry
    public float moleclawRegenerationFromFood = 2;

    @ConfigEntry
    public float pikehornDamage = 3.0f;
    @ConfigEntry
    public float pikehornKnockback = 0f;
    @ConfigEntry
    public float pikehornHealth = 20.0f;
    @ConfigEntry
    public float pikehornArmor = 0f;
    @ConfigEntry
    public float pikehornArmorToughness = 0f;
    @ConfigEntry
    public float pikehornGroundSpeed = 0.2f;
    @ConfigEntry
    public float pikehornFlyingSpeed = 0.8f;
    @ConfigEntry
    public int pikehornBasePrimaryAttackCooldown = 20;
    @ConfigEntry
    public int pikehornBaseAccelerationDuration = 100;
    @ConfigEntry
    public float pikehornRotationSpeedGround = 10;
    @ConfigEntry
    public float pikehornRotationSpeedAir = 10;
    @ConfigEntry
    public float pikehornVerticalSpeed = 0.2f;
    @ConfigEntry
    public float pikehornRegenerationFromFood = 3;

    @ConfigEntry
    public float lightningChaserDamage = 6.0f;
    @ConfigEntry
    public float lightningChaserKnockback = 0.3f;
    @ConfigEntry
    public float lightningChaserHealth = 70.0f;
    @ConfigEntry
    public float lightningChaserArmor = 6f;
    @ConfigEntry
    public float lightningChaserArmorToughness = 6.0f;
    @ConfigEntry
    public float lightningChaserGroundSpeed = 0.25f;
    @ConfigEntry
    public float lightningChaserFlyingSpeed = 0.8f;
    @ConfigEntry
    public int lightningChaserBaseSecondaryAttackCooldown = 30;
    @ConfigEntry
    public int lightningChaserBasePrimaryAttackCooldown = 30;
    @ConfigEntry
    public int lightningChaserBaseAccelerationDuration = 800;
    @ConfigEntry
    public float lightningChaserRotationSpeedGround = 9;
    @ConfigEntry
    public float lightningChaserRotationSpeedAir = 7;
    @ConfigEntry
    public float lightningChaserVerticalSpeed = 0.3f;
    @ConfigEntry
    public float lightningChaserRegenerationFromFood = 4;

    public static URMobAttributesConfig getConfig() {
        return CONFIG.getConfig();
    }

    public static void init() {
        CONFIG.load();
    }
}
