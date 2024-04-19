package nordmods.uselessreptile.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import dev.isxander.yacl.gui.controllers.cycling.EnumController;
import dev.isxander.yacl.gui.controllers.slider.DoubleSliderController;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import dev.isxander.yacl.gui.controllers.string.number.FloatFieldController;
import dev.isxander.yacl.gui.controllers.string.number.IntegerFieldController;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import net.minecraft.util.registry.Registry;
import nordmods.uselessreptile.client.config.URClientConfig;
import nordmods.uselessreptile.common.config.URConfig;
import nordmods.uselessreptile.common.config.URMobAttributesConfig;
import nordmods.uselessreptile.common.util.URSpawnGroup;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    public static Screen configScreen(Screen parentScreen) {
        return YetAnotherConfigLib.create(URConfig.CONFIG, ((defaults, config, builder) -> builder
                .title(key("title"))
                .category(gameplayCategory())
                .category(clientCategory())
                .category(mobAttributesCategory())
                .save(ModMenuIntegration::saveAll)))
                .generateScreen(parentScreen);
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenuIntegration::configScreen;
    }

    private static Text key(String id) {
        return Text.translatable("config.uselessreptile." + id);
    }
    private static Text requiresRestart() {
        return Text.translatable("config.uselessreptile.requires_restart.@Tooltip").formatted(Formatting.RED);
    }

    private static Text spawnGroupTooltip(SpawnGroup spawnGroup) {
        String entries = "";
        Language language = Language.getInstance();
        for (EntityType<?> entityType : Registry.ENTITY_TYPE.stream().filter(entityType -> entityType.getSpawnGroup() == spawnGroup).toList()) {
            String entry = language.get(entityType.getTranslationKey());
            entries = entries.concat(entry).concat(", ");
        }
        entries = entries.substring(0, entries.length() - 2);

        return Text.translatable("config.uselessreptile.option.spawnGroupCapacity.@Tooltip", entries);
    }

    private static void saveAll() {
        URClientConfig.CONFIG.save();
        URConfig.CONFIG.save();
        URMobAttributesConfig.CONFIG.save();
    }

    private static ConfigCategory gameplayCategory() {
        URConfig config = URConfig.getConfig();
        URConfig defaults = URConfig.CONFIG.getDefaults();

        ConfigCategory.Builder gameplayCategory = ConfigCategory.createBuilder()
                .name(key("category.gameplay"));

        //groups
        OptionGroup.Builder spawnWeightGroup = OptionGroup.createBuilder()
                .name(key("group.spawnWeight"))
                .tooltip(key("group.spawnWeight.@Tooltip"));
        OptionGroup.Builder spawnGroupsGroup = OptionGroup.createBuilder()
                .name(key("group.spawnGroups"))
                .tooltip(key("group.spawnGroups.@Tooltip"));
        OptionGroup.Builder groupSizeGroup = OptionGroup.createBuilder()
                .name(key("group.groupSize"))
                .tooltip(key("group.groupSize.@Tooltip"));
        OptionGroup.Builder dragonBehaviourGroup = OptionGroup.createBuilder()
                .name(key("group.dragonBehaviour"))
                .tooltip(key("group.dragonBehaviour.@Tooltip"));

        //options
        Option<Integer> wyvernSpawnWeight = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.wyvernSpawnWeight"))
                .tooltip(key("option.dragonSpawnWeight.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernSpawnWeight,
                        () -> config.wyvernSpawnWeight,
                        val -> config.wyvernSpawnWeight = val)
                .controller(opt -> new IntegerFieldController(opt, 0, Integer.MAX_VALUE))
                .build();

        Option<Integer> moleclawSpawnWeight = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.moleclawSpawnWeight"))
                .tooltip(key("option.dragonSpawnWeight.@Tooltip"), requiresRestart())
                .binding(defaults.moleclawSpawnWeight,
                        () -> config.moleclawSpawnWeight,
                        val -> config.moleclawSpawnWeight = val)
                .controller(opt -> new IntegerFieldController(opt, 0, Integer.MAX_VALUE))
                .build();

        Option<Integer> pikehornSpawnWeight = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.pikehornSpawnWeight"))
                .tooltip(key("option.dragonSpawnWeight.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornSpawnWeight,
                        () -> config.pikehornSpawnWeight,
                        val -> config.pikehornSpawnWeight = val)
                .controller(opt -> new IntegerFieldController(opt, 0, Integer.MAX_VALUE))
                .build();
        //Option<Integer> lightningChaserSpawnWeight = Option.<Integer>createBuilder()
        //        .name(key("option.lightningChaserSpawnWeight"))
        //        .tooltip(Optiontooltip.createBuilder()
        //                .text(key("option.dragonSpawnWeight.@Tooltip"), requiresRestart())
        //        .binding(defaults.lightningChaserSpawnWeight,
        //                () -> config.lightningChaserSpawnWeight,
        //                val -> config.lightningChaserSpawnWeight = val)
        //        .controller(opt -> new IntegerFieldController(opt, 0, Integer.MAX_VALUE))
        //        .build();
        //Option<Integer> lightningChaserThunderstormSpawnChance = Option.<Integer>createBuilder()
        //        .name(key("option.lightningChaserThunderstormSpawnChance"))
        //        .tooltip(Optiontooltip.createBuilder()
        //                .text(key("option.lightningChaserThunderstormSpawnChance.@Tooltip"))
        //        .binding(defaults.lightningChaserThunderstormSpawnChance,
        //                () -> config.lightningChaserThunderstormSpawnChance,
        //                val -> config.lightningChaserThunderstormSpawnChance = val)
        //        .controller(opt -> new IntegerSliderController(opt, 0, 100, 1))
        //        .build();
        //Option<Integer> lightningChaserThunderstormSpawnTimerCooldown = Option.<Integer>createBuilder()
        //        .name(key("option.lightningChaserThunderstormSpawnTimerCooldown"))
        //        .tooltip(Optiontooltip.createBuilder()
        //                .text(key("option.lightningChaserThunderstormSpawnTimerCooldown.@Tooltip"))
        //        .binding(defaults.lightningChaserThunderstormSpawnTimerCooldown,
        //                () -> config.lightningChaserThunderstormSpawnTimerCooldown,
        //                val -> config.lightningChaserThunderstormSpawnTimerCooldown = val)
        //        .controller(opt -> new IntegerFieldController(opt, 0, Integer.MAX_VALUE))
        //        .build();

        Option<Integer> dragonSpawnGroupCapacity = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.dragonSpawnGroupCapacity"))
                .tooltip(spawnGroupTooltip(URSpawnGroup.DRAGON.spawnGroup), requiresRestart())
                .binding(defaults.dragonSpawnGroupCapacity,
                        () -> config.dragonSpawnGroupCapacity,
                        val -> config.dragonSpawnGroupCapacity = val)
                .controller(opt -> new IntegerFieldController(opt, 0, Integer.MAX_VALUE))
                .build();
        Option<Integer> smallDragonSpawnGroupCapacity = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.smallDragonSpawnGroupCapacity"))
                .tooltip(spawnGroupTooltip(URSpawnGroup.SMALL_DRAGON.spawnGroup), requiresRestart())
                .binding(defaults.smallDragonSpawnGroupCapacity,
                        () -> config.smallDragonSpawnGroupCapacity,
                        val -> config.smallDragonSpawnGroupCapacity = val)
                .controller(opt -> new IntegerFieldController(opt, 0, Integer.MAX_VALUE))
                .build();
        Option<Integer> undergroundDragonSpawnGroupCapacity = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.undergroundDragonSpawnGroupCapacity"))
                .tooltip(spawnGroupTooltip(URSpawnGroup.UNDERGROUND_DRAGON.spawnGroup), requiresRestart())
                .binding(defaults.undergroundDragonSpawnGroupCapacity,
                        () -> config.undergroundDragonSpawnGroupCapacity,
                        val -> config.undergroundDragonSpawnGroupCapacity = val)
                .controller(opt -> new IntegerFieldController(opt, 0, Integer.MAX_VALUE))
                .build();

        Option<Integer> wyvernMinGroupSize = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.wyvernMinGroupSize"))
                .tooltip(key("option.dragonMinGroupSize.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernMinGroupSize,
                        () -> config.wyvernMinGroupSize,
                        val -> config.wyvernMinGroupSize = val)
                .controller(opt -> new IntegerFieldController(opt, 1, Integer.MAX_VALUE))
                .build();
        Option<Integer> wyvernMaxGroupSize = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.wyvernMaxGroupSize"))
                .tooltip(key("option.dragonMaxGroupSize.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernMaxGroupSize,
                        () -> config.wyvernMaxGroupSize,
                        val -> config.wyvernMaxGroupSize = val)
                .controller(opt -> new IntegerFieldController(opt, 1, Integer.MAX_VALUE))
                .build();
        Option<Integer> moleclawMinGroupSize = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.moleclawMinGroupSize"))
                .tooltip(key("option.dragonMinGroupSize.@Tooltip"), requiresRestart())
                .binding(defaults.moleclawMinGroupSize,
                        () -> config.moleclawMinGroupSize,
                        val -> config.moleclawMinGroupSize = val)
                .controller(opt -> new IntegerFieldController(opt, 1, Integer.MAX_VALUE))
                .build();
        Option<Integer> moleclawMaxGroupSize = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.moleclawMaxGroupSize"))
                .tooltip(key("option.dragonMaxGroupSize.@Tooltip"), requiresRestart())
                .binding(defaults.moleclawMaxGroupSize,
                        () -> config.moleclawMaxGroupSize,
                        val -> config.moleclawMaxGroupSize = val)
                .controller(opt -> new IntegerFieldController(opt, 1, Integer.MAX_VALUE))
                .build();
        Option<Integer> pikehornMinGroupSize = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.pikehornMinGroupSize"))
                .tooltip(key("option.dragonMinGroupSize.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornMinGroupSize,
                        () -> config.pikehornMinGroupSize,
                        val -> config.pikehornMinGroupSize = val)
                .controller(opt -> new IntegerFieldController(opt, 1, Integer.MAX_VALUE))
                .build();
        Option<Integer> pikehornMaxGroupSize = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.pikehornMaxGroupSize"))
                .tooltip(key("option.dragonMaxGroupSize.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornMaxGroupSize,
                        () -> config.pikehornMaxGroupSize,
                        val -> config.pikehornMaxGroupSize = val)
                .controller(opt -> new IntegerFieldController(opt, 1, Integer.MAX_VALUE))
                .build();
        //Option<Integer> lightningChaserMinGroupSize = Option.<Integer>createBuilder()
        //        .name(key("option.lightningChaserMinGroupSize"))
        //        .tooltip(Optiontooltip.createBuilder()
        //                .text(key("option.dragonMinGroupSize.@Tooltip"), requiresRestart())
        //        .binding(defaults.lightningChaserMinGroupSize,
        //                () -> config.lightningChaserMinGroupSize,
        //                val -> config.lightningChaserMinGroupSize = val)
        //        .controller(opt -> new IntegerFieldController(opt, 1, Integer.MAX_VALUE))
        //        .build();
        //Option<Integer> lightningChaserMaxGroupSize = Option.<Integer>createBuilder()
        //        .name(key("option.lightningChaserMaxGroupSize"))
        //        .tooltip(Optiontooltip.createBuilder()
        //                .text(key("option.dragonMaxGroupSize.@Tooltip"), requiresRestart())
        //        .binding(defaults.lightningChaserMaxGroupSize,
        //                () -> config.lightningChaserMaxGroupSize,
        //                val -> config.lightningChaserMaxGroupSize = val)
        //        .controller(opt -> new IntegerFieldController(opt, 1, Integer.MAX_VALUE))
        //        .build();

        Option<URConfig.DragonGriefing> allowDragonGriefing = Option.<URConfig.DragonGriefing>createBuilder(URConfig.DragonGriefing.class)
                .name(key("option.allowDragonGriefing"))
                .tooltip(key("option.allowDragonGriefing.@Tooltip"))
                .binding(defaults.allowDragonGriefing,
                        () -> config.allowDragonGriefing,
                        val -> config.allowDragonGriefing = val)
                .controller(EnumController::new)
                .build();
        Option<Integer> blockDropChance = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.blockDropChance"))
                .tooltip(key("option.blockDropChance.@Tooltip"))
                .binding(defaults.blockDropChance,
                        () -> config.blockDropChance,
                        val -> config.blockDropChance = val)
                .controller(opt -> new IntegerSliderController(opt, 0, 100, 1))
                .build();
        Option<Boolean> dragonMadness = Option.<Boolean>createBuilder(Boolean.class)
                .name(key("option.dragonMadness"))
                .tooltip(key("option.dragonMadness.@Tooltip"))
                .binding(config.dragonMadness,
                        () -> config.dragonMadness,
                        val -> config.dragonMadness = val)
                .controller(TickBoxController::new)
                .build();

        spawnWeightGroup.option(wyvernSpawnWeight);
        spawnWeightGroup.option(moleclawSpawnWeight);
        spawnWeightGroup.option(pikehornSpawnWeight);
        //spawnWeightGroup.option(lightningChaserSpawnWeight);
        //spawnWeightGroup.option(lightningChaserThunderstormSpawnChance);
        //spawnWeightGroup.option(lightningChaserThunderstormSpawnTimerCooldown);

        spawnGroupsGroup.option(dragonSpawnGroupCapacity);
        spawnGroupsGroup.option(undergroundDragonSpawnGroupCapacity);
        spawnGroupsGroup.option(smallDragonSpawnGroupCapacity);

        groupSizeGroup.option(wyvernMinGroupSize);
        groupSizeGroup.option(wyvernMaxGroupSize);
        groupSizeGroup.option(moleclawMinGroupSize);
        groupSizeGroup.option(moleclawMaxGroupSize);
        groupSizeGroup.option(pikehornMinGroupSize);
        groupSizeGroup.option(pikehornMaxGroupSize);
        //groupSizeGroup.option(lightningChaserMinGroupSize);
        //groupSizeGroup.option(lightningChaserMaxGroupSize);

        dragonBehaviourGroup.option(allowDragonGriefing);
        dragonBehaviourGroup.option(blockDropChance);
        dragonBehaviourGroup.option(dragonMadness);

        gameplayCategory.group(spawnWeightGroup.build());
        gameplayCategory.group(spawnGroupsGroup.build());
        gameplayCategory.group(groupSizeGroup.build());
        gameplayCategory.group(dragonBehaviourGroup.build());


        return gameplayCategory.build();
    }

    private static ConfigCategory clientCategory() {
        URClientConfig clientConfig = URClientConfig.getConfig();
        URClientConfig clientDefaults = URClientConfig.CONFIG.getDefaults();

        //category
        ConfigCategory.Builder clientCategory = ConfigCategory.createBuilder()
                .name(key("category.client"));

        //group
        OptionGroup.Builder cameraGroup = OptionGroup.createBuilder()
                .name(key("group.camera"))
                .tooltip(key("group.camera.@Tooltip"));
        OptionGroup.Builder dragonAppearanceGroup = OptionGroup.createBuilder()
                .name(key("group.dragonAppearance"))
                .tooltip(key("group.dragonAppearance.@Tooltip"));


        Option<Double> cameraDistanceOffset = Option.<Double>createBuilder(Double.class)
                .name(key("option.cameraDistanceOffset"))
                .binding(clientDefaults.cameraDistanceOffset,
                        () -> clientConfig.cameraDistanceOffset,
                        val -> clientConfig.cameraDistanceOffset = val)
                .controller(opt -> new DoubleSliderController(opt, -5, 5, 0.05))
                .build();
        Option<Double> cameraVerticalOffset = Option.<Double>createBuilder(Double.class)
                .name(key("option.cameraVerticalOffset"))
                .binding(clientDefaults.cameraVerticalOffset,
                        () -> clientConfig.cameraVerticalOffset,
                        val -> clientConfig.cameraVerticalOffset = val)
                .controller(opt -> new DoubleSliderController(opt, -5, 5, 0.05))
                .build();
        Option<Double> cameraHorizontalOffset = Option.<Double>createBuilder(Double.class)
                .name(key("option.cameraHorizontalOffset"))
                .binding(clientDefaults.cameraHorizontalOffset,
                        () -> clientConfig.cameraHorizontalOffset,
                        val -> clientConfig.cameraHorizontalOffset = val)
                .controller(opt -> new DoubleSliderController(opt, -5, 5, 0.05))
                .build();
        Option<Boolean> enableCameraOffset = Option.<Boolean>createBuilder(Boolean.class)
                .name(key("option.enableCameraOffset"))
                .tooltip(key("option.enableCameraOffset.@Tooltip"))
                .binding(clientDefaults.enableCameraOffset,
                        () -> clientConfig.enableCameraOffset,
                        val -> clientConfig.enableCameraOffset = val)
                .controller(TickBoxController::new)
                .build();
        Option<Boolean> enableCrosshair = Option.<Boolean>createBuilder(Boolean.class)
                .name(key("option.enableCrosshair"))
                .tooltip(key("option.enableCrosshair.@Tooltip"))
                .binding(clientConfig.enableCrosshair,
                        () -> clientConfig.enableCrosshair,
                        val -> clientConfig.enableCrosshair = val)
                .controller(TickBoxController::new)
                .build();
        Option<Boolean> autoThirdPerson = Option.<Boolean>createBuilder(Boolean.class)
                .name(key("option.autoThirdPerson"))
                .tooltip(key("option.autoThirdPerson.@Tooltip"))
                .binding(clientDefaults.autoThirdPerson,
                        () -> clientConfig.autoThirdPerson,
                        val -> clientConfig.autoThirdPerson = val)
                .controller(TickBoxController::new)
                .build();

        Option<Boolean> disableNamedTextures = Option.<Boolean>createBuilder(Boolean.class)
                .name(key("option.disableNamedEntityModels"))
                .tooltip(key("option.disableNamedEntityModels.@Tooltip"))
                .binding(clientDefaults.disableNamedEntityModels,
                        () -> clientConfig.disableNamedEntityModels,
                        val -> clientConfig.disableNamedEntityModels = val)
                .controller(TickBoxController::new)
                .build();
        Option<Boolean> disableEmissiveTextures = Option.<Boolean>createBuilder(Boolean.class)
                .name(key("option.disableEmissiveTextures"))
                .tooltip(key("option.disableEmissiveTextures.@Tooltip"))
                .binding(clientDefaults.disableEmissiveTextures,
                        () -> clientConfig.disableEmissiveTextures,
                        val -> clientConfig.disableEmissiveTextures = val)
                .controller(TickBoxController::new)
                .build();
        Option<Boolean> attackBoxesInDebug = Option.<Boolean>createBuilder(Boolean.class)
                .name(key("option.attackBoxesInDebug"))
                .tooltip(key("option.attackBoxesInDebug.@Tooltip"))
                .binding(clientDefaults.attackBoxesInDebug,
                        () -> clientConfig.attackBoxesInDebug,
                        val -> clientConfig.attackBoxesInDebug = val)
                .controller(TickBoxController::new)
                .build();

        cameraGroup.option(cameraDistanceOffset);
        cameraGroup.option(cameraVerticalOffset);
        cameraGroup.option(cameraHorizontalOffset);
        cameraGroup.option(enableCameraOffset);
        cameraGroup.option(enableCrosshair);
        cameraGroup.option(autoThirdPerson);

        dragonAppearanceGroup.option(disableNamedTextures);
        dragonAppearanceGroup.option(disableEmissiveTextures);
        dragonAppearanceGroup.option(attackBoxesInDebug);

        clientCategory.group(cameraGroup.build());
        clientCategory.group(dragonAppearanceGroup.build());

        return clientCategory.build();
    }

    private static ConfigCategory mobAttributesCategory() {
        URMobAttributesConfig config = URMobAttributesConfig.getConfig();
        URMobAttributesConfig defaults = URMobAttributesConfig.CONFIG.getDefaults();

        ConfigCategory.Builder mobAttributesCategory = ConfigCategory.createBuilder()
                .name(key("category.mobAttributes"));

        OptionGroup.Builder globalMultipliersGroup = OptionGroup.createBuilder()
                .name(key("group.globalMultipliers"))
                .tooltip(key("group.globalMultipliers.@Tooltip"));

        Option<Float> dragonDamageMultiplier = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonDamageMultiplier"))
                .tooltip(key("option.dragonDamageMultiplier.@Tooltip"), requiresRestart())
                .binding(defaults.dragonDamageMultiplier,
                        () -> config.dragonDamageMultiplier,
                        val -> config.dragonDamageMultiplier = val)
                .controller(FloatFieldController::new)
                .build();

        Option<Float> dragonKnockbackMultiplier = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonKnockbackMultiplier"))
                .tooltip(key("option.dragonKnockbackMultiplier.@Tooltip"), requiresRestart())
                .binding(defaults.dragonKnockbackMultiplier,
                        () -> config.dragonKnockbackMultiplier,
                        val -> config.dragonKnockbackMultiplier = val)
                .controller(FloatFieldController::new)
                .build();

        Option<Float> dragonHealthMultiplier = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonHealthMultiplier"))
                .tooltip(key("option.dragonHealthMultiplier.@Tooltip"), requiresRestart())
                .binding(defaults.dragonHealthMultiplier,
                        () -> config.dragonHealthMultiplier,
                        val -> config.dragonHealthMultiplier = val)
                .controller(FloatFieldController::new)
                .build();

        Option<Float> dragonArmorMultiplier = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonArmorMultiplier"))
                .tooltip(key("option.dragonArmorMultiplier.@Tooltip"), requiresRestart())
                .binding(defaults.dragonArmorMultiplier,
                        () -> config.dragonArmorMultiplier,
                        val -> config.dragonArmorMultiplier = val)
                .controller(FloatFieldController::new)
                .build();

        Option<Float> dragonArmorToughnessMultiplier = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonArmorToughnessMultiplier"))
                .tooltip(key("option.dragonArmorToughnessMultiplier.@Tooltip"), requiresRestart())
                .binding(defaults.dragonArmorToughnessMultiplier,
                        () -> config.dragonArmorToughnessMultiplier,
                        val -> config.dragonArmorToughnessMultiplier = val)
                .controller(FloatFieldController::new)
                .build();

        Option<Float> dragonGroundSpeedMultiplier = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonGroundSpeedMultiplier"))
                .tooltip(key("option.dragonGroundSpeedMultiplier.@Tooltip"), requiresRestart())
                .binding(defaults.dragonGroundSpeedMultiplier,
                        () -> config.dragonGroundSpeedMultiplier,
                        val -> config.dragonGroundSpeedMultiplier = val)
                .controller(FloatFieldController::new)
                .build();

        Option<Float> dragonFlyingSpeedMultiplier = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonFlyingSpeedMultiplier"))
                .tooltip(key("option.dragonFlyingSpeedMultiplier.@Tooltip"), requiresRestart())
                .binding(defaults.dragonFlyingSpeedMultiplier,
                        () -> config.dragonFlyingSpeedMultiplier,
                        val -> config.dragonFlyingSpeedMultiplier = val)
                .controller(FloatFieldController::new)
                .build();

        globalMultipliersGroup.option(dragonDamageMultiplier);
        globalMultipliersGroup.option(dragonKnockbackMultiplier);
        globalMultipliersGroup.option(dragonHealthMultiplier);
        globalMultipliersGroup.option(dragonArmorMultiplier);
        globalMultipliersGroup.option(dragonArmorToughnessMultiplier);
        globalMultipliersGroup.option(dragonGroundSpeedMultiplier);
        globalMultipliersGroup.option(dragonFlyingSpeedMultiplier);
        mobAttributesCategory.group(globalMultipliersGroup.build());

        addWyvernAttributesGroup(mobAttributesCategory, config, defaults);
        addMoleclawAttributesGroup(mobAttributesCategory, config, defaults);
        addPikehornAttributesGroup(mobAttributesCategory, config, defaults);
        addLightningChaserAttributesGroup(mobAttributesCategory, config, defaults);

        return mobAttributesCategory.build();
    }

    private static void addWyvernAttributesGroup(ConfigCategory.Builder category, URMobAttributesConfig config, URMobAttributesConfig defaults) {
        OptionGroup.Builder wyvernAttributesGroup = OptionGroup.createBuilder()
                .name(key("group.wyvernAttributes"))
                .tooltip(key("group.dragonAttributes.@Tooltip"));

        Option<Float> wyvernDamage = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonDamage"))
                .tooltip(key("option.dragonDamage.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernDamage,
                        () -> config.wyvernDamage,
                        val -> config.wyvernDamage = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> wyvernKnockback = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonKnockback"))
                .tooltip(key("option.dragonKnockback.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernKnockback,
                        () -> config.wyvernKnockback,
                        val -> config.wyvernKnockback = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> wyvernHealth = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonHealth"))
                .tooltip(key("option.dragonHealth.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernHealth,
                        () -> config.wyvernHealth,
                        val -> config.wyvernHealth = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> wyvernArmor = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonArmor"))
                .tooltip(key("option.dragonArmor.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernArmor,
                        () -> config.wyvernArmor,
                        val -> config.wyvernArmor = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> wyvernArmorToughness = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonArmorToughness"))
                .tooltip(key("option.dragonArmorToughness.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernArmorToughness,
                        () -> config.wyvernArmorToughness,
                        val -> config.wyvernArmorToughness = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> wyvernGroundSpeed = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonGroundSpeed"))
                .tooltip(key("option.dragonGroundSpeed.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernGroundSpeed,
                        () -> config.wyvernGroundSpeed,
                        val -> config.wyvernGroundSpeed = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> wyvernFlyingSpeed = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonFlyingSpeed"))
                .tooltip(key("option.dragonFlyingSpeed.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernFlyingSpeed,
                        () -> config.wyvernFlyingSpeed,
                        val -> config.wyvernFlyingSpeed = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Integer> wyvernBaseSecondaryAttackCooldown = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.dragonBaseSecondaryAttackCooldown"))
                .tooltip(key("option.wyvernBaseSecondaryAttackCooldown.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernBaseSecondaryAttackCooldown,
                        () -> config.wyvernBaseSecondaryAttackCooldown,
                        val -> config.wyvernBaseSecondaryAttackCooldown = val)
                .controller(IntegerFieldController::new)
                .build();
        Option<Integer> wyvernBasePrimaryAttackCooldown = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.dragonBasePrimaryAttackCooldown"))
                .tooltip(key("option.wyvernBasePrimaryAttackCooldown.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernBasePrimaryAttackCooldown,
                        () -> config.wyvernBasePrimaryAttackCooldown,
                        val -> config.wyvernBasePrimaryAttackCooldown = val)
                .controller(IntegerFieldController::new)
                .build();
        Option<Integer> wyvernBaseAccelerationDuration = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.dragonBaseAccelerationDuration"))
                .tooltip(key("option.dragonBaseAccelerationDuration.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernBaseAccelerationDuration,
                        () -> config.wyvernBaseAccelerationDuration,
                        val -> config.wyvernBaseAccelerationDuration = val)
                .controller(IntegerFieldController::new)
                .build();
        Option<Float> wyvernRotationSpeedGround = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonRotationSpeedGround"))
                .tooltip(key("option.dragonRotationSpeedGround.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernRotationSpeedGround,
                        () -> config.wyvernRotationSpeedGround,
                        val -> config.wyvernRotationSpeedGround = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> wyvernRotationSpeedAir = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonRotationSpeedAir"))
                .tooltip(key("option.dragonRotationSpeedAir.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernRotationSpeedAir,
                        () -> config.wyvernRotationSpeedAir,
                        val -> config.wyvernRotationSpeedAir = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> wyvernVerticalSpeed = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonVerticalSpeed"))
                .tooltip(key("option.dragonVerticalSpeed.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernVerticalSpeed,
                        () -> config.wyvernVerticalSpeed,
                        val -> config.wyvernVerticalSpeed = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> wyvernRegenerationFromFood = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonRegenerationFromFood"))
                .tooltip(key("option.dragonRegenerationFromFood.@Tooltip"), requiresRestart())
                .binding(defaults.wyvernRegenerationFromFood,
                        () -> config.wyvernRegenerationFromFood,
                        val -> config.wyvernRegenerationFromFood = val)
                .controller(FloatFieldController::new)
                .build();

        wyvernAttributesGroup.option(wyvernDamage);
        wyvernAttributesGroup.option(wyvernKnockback);
        wyvernAttributesGroup.option(wyvernBasePrimaryAttackCooldown);
        wyvernAttributesGroup.option(wyvernBaseSecondaryAttackCooldown);
        wyvernAttributesGroup.option(wyvernHealth);
        wyvernAttributesGroup.option(wyvernArmor);
        wyvernAttributesGroup.option(wyvernArmorToughness);
        wyvernAttributesGroup.option(wyvernRegenerationFromFood);
        wyvernAttributesGroup.option(wyvernGroundSpeed);
        wyvernAttributesGroup.option(wyvernFlyingSpeed);
        wyvernAttributesGroup.option(wyvernVerticalSpeed);
        wyvernAttributesGroup.option(wyvernBaseAccelerationDuration);
        wyvernAttributesGroup.option(wyvernRotationSpeedGround);
        wyvernAttributesGroup.option(wyvernRotationSpeedAir);
        category.group(wyvernAttributesGroup.build());
    }

    private static void addMoleclawAttributesGroup(ConfigCategory.Builder category, URMobAttributesConfig config, URMobAttributesConfig defaults) {
        OptionGroup.Builder moleclawAttributesGroup = OptionGroup.createBuilder()
                .name(key("group.moleclawAttributes"))
                .tooltip(key("group.dragonAttributes.@Tooltip"));

        Option<Float> moleclawDamage = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonDamage"))
                .tooltip(key("option.dragonDamage.@Tooltip"), requiresRestart())
                .binding(defaults.moleclawDamage,
                        () -> config.moleclawDamage,
                        val -> config.moleclawDamage = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> moleclawKnockback = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonKnockback"))
                .tooltip(key("option.dragonKnockback.@Tooltip"), requiresRestart())
                .binding(defaults.moleclawKnockback,
                        () -> config.moleclawKnockback,
                        val -> config.moleclawKnockback = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> moleclawHealth = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonHealth"))
                .tooltip(key("option.dragonHealth.@Tooltip"), requiresRestart())
                .binding(defaults.moleclawHealth,
                        () -> config.moleclawHealth,
                        val -> config.moleclawHealth = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> moleclawArmor = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonArmor"))
                .tooltip(key("option.dragonArmor.@Tooltip"), requiresRestart())
                .binding(defaults.moleclawArmor,
                        () -> config.moleclawArmor,
                        val -> config.moleclawArmor = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> moleclawArmorToughness = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonArmorToughness"))
                .tooltip(key("option.dragonArmorToughness.@Tooltip"), requiresRestart())
                .binding(defaults.moleclawArmorToughness,
                        () -> config.moleclawArmorToughness,
                        val -> config.moleclawArmorToughness = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> moleclawGroundSpeed = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonGroundSpeed"))
                .tooltip(key("option.dragonGroundSpeed.@Tooltip"), requiresRestart())
                .binding(defaults.moleclawGroundSpeed,
                        () -> config.moleclawGroundSpeed,
                        val -> config.moleclawGroundSpeed = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Integer> moleclawBaseSecondaryAttackCooldown = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.dragonBaseSecondaryAttackCooldown"))
                .tooltip(key("option.moleclawBaseSecondaryAttackCooldown.@Tooltip"), requiresRestart())
                .binding(defaults.moleclawBaseSecondaryAttackCooldown,
                        () -> config.moleclawBaseSecondaryAttackCooldown,
                        val -> config.moleclawBaseSecondaryAttackCooldown = val)
                .controller(IntegerFieldController::new)
                .build();
        Option<Integer> moleclawBasePrimaryAttackCooldown = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.dragonBasePrimaryAttackCooldown"))
                .tooltip(key("option.moleclawBasePrimaryAttackCooldown.@Tooltip"), requiresRestart())
                .binding(defaults.moleclawBasePrimaryAttackCooldown,
                        () -> config.moleclawBasePrimaryAttackCooldown,
                        val -> config.moleclawBasePrimaryAttackCooldown = val)
                .controller(IntegerFieldController::new)
                .build();
        Option<Float> moleclawRotationSpeedGround = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonRotationSpeedGround"))
                .tooltip(key("option.dragonRotationSpeedGround.@Tooltip"), requiresRestart())
                .binding(defaults.moleclawRotationSpeedGround,
                        () -> config.moleclawRotationSpeedGround,
                        val -> config.moleclawRotationSpeedGround = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> moleclawRegenerationFromFood = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonRegenerationFromFood"))
                .tooltip(key("option.dragonRegenerationFromFood.@Tooltip"), requiresRestart())
                .binding(defaults.moleclawRegenerationFromFood,
                        () -> config.moleclawRegenerationFromFood,
                        val -> config.moleclawRegenerationFromFood = val)
                .controller(FloatFieldController::new)
                .build();

        moleclawAttributesGroup.option(moleclawDamage);
        moleclawAttributesGroup.option(moleclawKnockback);
        moleclawAttributesGroup.option(moleclawBasePrimaryAttackCooldown);
        moleclawAttributesGroup.option(moleclawBaseSecondaryAttackCooldown);
        moleclawAttributesGroup.option(moleclawHealth);
        moleclawAttributesGroup.option(moleclawArmor);
        moleclawAttributesGroup.option(moleclawArmorToughness);
        moleclawAttributesGroup.option(moleclawRegenerationFromFood);
        moleclawAttributesGroup.option(moleclawGroundSpeed);
        moleclawAttributesGroup.option(moleclawRotationSpeedGround);
        category.group(moleclawAttributesGroup.build());
    }

    private static void addPikehornAttributesGroup(ConfigCategory.Builder category, URMobAttributesConfig config, URMobAttributesConfig defaults) {
        OptionGroup.Builder pikehornAttributesGroup = OptionGroup.createBuilder()
                .name(key("group.pikehornAttributes"))
                .tooltip(key("group.dragonAttributes.@Tooltip"));

        Option<Float> pikehornDamage = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonDamage"))
                .tooltip(key("option.dragonDamage.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornDamage,
                        () -> config.pikehornDamage,
                        val -> config.pikehornDamage = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> pikehornKnockback = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonKnockback"))
                .tooltip(key("option.dragonKnockback.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornKnockback,
                        () -> config.pikehornKnockback,
                        val -> config.pikehornKnockback = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> pikehornHealth = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonHealth"))
                .tooltip(key("option.dragonHealth.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornHealth,
                        () -> config.pikehornHealth,
                        val -> config.pikehornHealth = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> pikehornArmor = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonArmor"))
                .tooltip(key("option.dragonArmor.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornArmor,
                        () -> config.pikehornArmor,
                        val -> config.pikehornArmor = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> pikehornArmorToughness = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonArmorToughness"))
                .tooltip(key("option.dragonArmorToughness.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornArmorToughness,
                        () -> config.pikehornArmorToughness,
                        val -> config.pikehornArmorToughness = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> pikehornGroundSpeed = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonGroundSpeed"))
                .tooltip(key("option.dragonGroundSpeed.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornGroundSpeed,
                        () -> config.pikehornGroundSpeed,
                        val -> config.pikehornGroundSpeed = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> pikehornFlyingSpeed = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonFlyingSpeed"))
                .tooltip(key("option.dragonFlyingSpeed.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornFlyingSpeed,
                        () -> config.pikehornFlyingSpeed,
                        val -> config.pikehornFlyingSpeed = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Integer> pikehornBasePrimaryAttackCooldown = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.dragonBasePrimaryAttackCooldown"))
                .tooltip(key("option.pikehornBasePrimaryAttackCooldown.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornBasePrimaryAttackCooldown,
                        () -> config.pikehornBasePrimaryAttackCooldown,
                        val -> config.pikehornBasePrimaryAttackCooldown = val)
                .controller(IntegerFieldController::new)
                .build();
        Option<Integer> pikehornBaseAccelerationDuration = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.dragonBaseAccelerationDuration"))
                .tooltip(key("option.dragonBaseAccelerationDuration.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornBaseAccelerationDuration,
                        () -> config.pikehornBaseAccelerationDuration,
                        val -> config.pikehornBaseAccelerationDuration = val)
                .controller(IntegerFieldController::new)
                .build();
        Option<Float> pikehornRotationSpeedGround = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonRotationSpeedGround"))
                .tooltip(key("option.dragonRotationSpeedGround.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornRotationSpeedGround,
                        () -> config.pikehornRotationSpeedGround,
                        val -> config.pikehornRotationSpeedGround = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> pikehornRotationSpeedAir = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonRotationSpeedAir"))
                .tooltip(key("option.dragonRotationSpeedAir.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornRotationSpeedAir,
                        () -> config.pikehornRotationSpeedAir,
                        val -> config.pikehornRotationSpeedAir = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> pikehornVerticalSpeed = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonVerticalSpeed"))
                .tooltip(key("option.dragonVerticalSpeed.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornVerticalSpeed,
                        () -> config.pikehornVerticalSpeed,
                        val -> config.pikehornVerticalSpeed = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> pikehornRegenerationFromFood = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonRegenerationFromFood"))
                .tooltip(key("option.dragonRegenerationFromFood.@Tooltip"), requiresRestart())
                .binding(defaults.pikehornRegenerationFromFood,
                        () -> config.pikehornRegenerationFromFood,
                        val -> config.pikehornRegenerationFromFood = val)
                .controller(FloatFieldController::new)
                .build();

        pikehornAttributesGroup.option(pikehornDamage);
        pikehornAttributesGroup.option(pikehornKnockback);
        pikehornAttributesGroup.option(pikehornBasePrimaryAttackCooldown);
        pikehornAttributesGroup.option(pikehornHealth);
        pikehornAttributesGroup.option(pikehornArmor);
        pikehornAttributesGroup.option(pikehornArmorToughness);
        pikehornAttributesGroup.option(pikehornRegenerationFromFood);
        pikehornAttributesGroup.option(pikehornGroundSpeed);
        pikehornAttributesGroup.option(pikehornFlyingSpeed);
        pikehornAttributesGroup.option(pikehornVerticalSpeed);
        pikehornAttributesGroup.option(pikehornBaseAccelerationDuration);
        pikehornAttributesGroup.option(pikehornRotationSpeedGround);
        pikehornAttributesGroup.option(pikehornRotationSpeedAir);
        category.group(pikehornAttributesGroup.build());
    }

    private static void addLightningChaserAttributesGroup(ConfigCategory.Builder category, URMobAttributesConfig config, URMobAttributesConfig defaults) {
        OptionGroup.Builder lightningChaserAttributesGroup = OptionGroup.createBuilder()
                .name(key("group.lightningChaserAttributes"))
                .tooltip(key("group.dragonAttributes.@Tooltip"));

        Option<Float> lightningChaserDamage = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonDamage"))
                .tooltip(key("option.dragonDamage.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserDamage,
                        () -> config.lightningChaserDamage,
                        val -> config.lightningChaserDamage = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> lightningChaserKnockback = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonKnockback"))
                .tooltip(key("option.dragonKnockback.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserKnockback,
                        () -> config.lightningChaserKnockback,
                        val -> config.lightningChaserKnockback = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> lightningChaserHealth = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonHealth"))
                .tooltip(key("option.dragonHealth.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserHealth,
                        () -> config.lightningChaserHealth,
                        val -> config.lightningChaserHealth = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> lightningChaserArmor = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonArmor"))
                .tooltip(key("option.dragonArmor.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserArmor,
                        () -> config.lightningChaserArmor,
                        val -> config.lightningChaserArmor = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> lightningChaserArmorToughness = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonArmorToughness"))
                .tooltip(key("option.dragonArmorToughness.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserArmorToughness,
                        () -> config.lightningChaserArmorToughness,
                        val -> config.lightningChaserArmorToughness = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> lightningChaserGroundSpeed = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonGroundSpeed"))
                .tooltip(key("option.dragonGroundSpeed.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserGroundSpeed,
                        () -> config.lightningChaserGroundSpeed,
                        val -> config.lightningChaserGroundSpeed = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> lightningChaserFlyingSpeed = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonFlyingSpeed"))
                .tooltip(key("option.dragonFlyingSpeed.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserFlyingSpeed,
                        () -> config.lightningChaserFlyingSpeed,
                        val -> config.lightningChaserFlyingSpeed = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Integer> lightningChaserBaseSecondaryAttackCooldown = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.dragonBaseSecondaryAttackCooldown"))
                .tooltip(key("option.lightningChaserBaseSecondaryAttackCooldown.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserBaseSecondaryAttackCooldown,
                        () -> config.lightningChaserBaseSecondaryAttackCooldown,
                        val -> config.lightningChaserBaseSecondaryAttackCooldown = val)
                .controller(IntegerFieldController::new)
                .build();
        Option<Integer> lightningChaserBasePrimaryAttackCooldown = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.dragonBasePrimaryAttackCooldown"))
                .tooltip(key("option.lightningChaserBasePrimaryAttackCooldown.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserBasePrimaryAttackCooldown,
                        () -> config.lightningChaserBasePrimaryAttackCooldown,
                        val -> config.lightningChaserBasePrimaryAttackCooldown = val)
                .controller(IntegerFieldController::new)
                .build();
        Option<Integer> lightningChaserBaseAccelerationDuration = Option.<Integer>createBuilder(Integer.class)
                .name(key("option.dragonBaseAccelerationDuration"))
                .tooltip(key("option.dragonBaseAccelerationDuration.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserBaseAccelerationDuration,
                        () -> config.lightningChaserBaseAccelerationDuration,
                        val -> config.lightningChaserBaseAccelerationDuration = val)
                .controller(IntegerFieldController::new)
                .build();
        Option<Float> lightningChaserRotationSpeedGround = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonRotationSpeedGround"))
                .tooltip(key("option.dragonRotationSpeedGround.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserRotationSpeedGround,
                        () -> config.lightningChaserRotationSpeedGround,
                        val -> config.lightningChaserRotationSpeedGround = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> lightningChaserRotationSpeedAir = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonRotationSpeedAir"))
                .tooltip(key("option.dragonRotationSpeedAir.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserRotationSpeedAir,
                        () -> config.lightningChaserRotationSpeedAir,
                        val -> config.lightningChaserRotationSpeedAir = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> lightningChaserVerticalSpeed = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonVerticalSpeed"))
                .tooltip(key("option.dragonVerticalSpeed.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserVerticalSpeed,
                        () -> config.lightningChaserVerticalSpeed,
                        val -> config.lightningChaserVerticalSpeed = val)
                .controller(FloatFieldController::new)
                .build();
        Option<Float> lightningChaserRegenerationFromFood = Option.<Float>createBuilder(Float.class)
                .name(key("option.dragonRegenerationFromFood"))
                .tooltip(key("option.dragonRegenerationFromFood.@Tooltip"), requiresRestart())
                .binding(defaults.lightningChaserRegenerationFromFood,
                        () -> config.lightningChaserRegenerationFromFood,
                        val -> config.lightningChaserRegenerationFromFood = val)
                .controller(FloatFieldController::new)
                .build();

        lightningChaserAttributesGroup.option(lightningChaserDamage);
        lightningChaserAttributesGroup.option(lightningChaserKnockback);
        lightningChaserAttributesGroup.option(lightningChaserBasePrimaryAttackCooldown);
        lightningChaserAttributesGroup.option(lightningChaserBaseSecondaryAttackCooldown);
        lightningChaserAttributesGroup.option(lightningChaserHealth);
        lightningChaserAttributesGroup.option(lightningChaserArmor);
        lightningChaserAttributesGroup.option(lightningChaserArmorToughness);
        lightningChaserAttributesGroup.option(lightningChaserRegenerationFromFood);
        lightningChaserAttributesGroup.option(lightningChaserGroundSpeed);
        lightningChaserAttributesGroup.option(lightningChaserFlyingSpeed);
        lightningChaserAttributesGroup.option(lightningChaserVerticalSpeed);
        lightningChaserAttributesGroup.option(lightningChaserBaseAccelerationDuration);
        lightningChaserAttributesGroup.option(lightningChaserRotationSpeedGround);
        lightningChaserAttributesGroup.option(lightningChaserRotationSpeedAir);
        category.group(lightningChaserAttributesGroup.build());
    }
}
