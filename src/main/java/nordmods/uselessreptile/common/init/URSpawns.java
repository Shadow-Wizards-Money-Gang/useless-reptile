package nordmods.uselessreptile.common.init;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;
import nordmods.uselessreptile.common.config.URConfig;
import nordmods.uselessreptile.common.entity.LightningChaserEntity;
import nordmods.uselessreptile.common.entity.MoleclawEntity;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import nordmods.uselessreptile.common.util.URSpawnGroup;

public class URSpawns {
    public static void init() {
        BiomeModifications.addSpawn(BiomeSelectors.tag(URTags.WYVERN_SPAWN_BLACKLIST).negate(),
                URSpawnGroup.DRAGON.spawnGroup,
                UREntities.WYVERN_ENTITY,
                URConfig.getConfig().wyvernSpawnWeight,
                URConfig.getConfig().wyvernMinGroupSize, URConfig.getConfig().wyvernMaxGroupSize);

        BiomeModifications.addSpawn(BiomeSelectors.tag(URTags.MOLECLAW_SPAWN_BLACKLIST).negate(),
                URSpawnGroup.UNDERGROUND_DRAGON.spawnGroup,
                UREntities.MOLECLAW_ENTITY,
                URConfig.getConfig().moleclawSpawnWeight,
                URConfig.getConfig().moleclawMinGroupSize, URConfig.getConfig().moleclawMaxGroupSize);

        BiomeModifications.addSpawn(BiomeSelectors.tag(URTags.RIVER_PIKEHORN_SPAWN_BLACKLIST).negate(),
                URSpawnGroup.SMALL_DRAGON.spawnGroup,
                UREntities.RIVER_PIKEHORN_ENTITY,
                URConfig.getConfig().pikehornSpawnWeight,
                URConfig.getConfig().pikehornMinGroupSize, URConfig.getConfig().pikehornMaxGroupSize);

        BiomeModifications.addSpawn(BiomeSelectors.tag(URTags.LIGHTNING_CHASER_SPAWN_BLACKLIST).negate(),
                URSpawnGroup.DRAGON.spawnGroup,
                UREntities.LIGHTNING_CHASER_ENTITY,
                URConfig.getConfig().lightningChaserSpawnWeight,
                URConfig.getConfig().lightningChaserMinGroupSize, URConfig.getConfig().lightningChaserMaxGroupSize);

        SpawnRestriction.register(UREntities.WYVERN_ENTITY, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, URDragonEntity::canDragonSpawn);
        SpawnRestriction.register(UREntities.MOLECLAW_ENTITY, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MoleclawEntity::canDragonSpawn);
        SpawnRestriction.register(UREntities.RIVER_PIKEHORN_ENTITY, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, URDragonEntity::canDragonSpawn);
        SpawnRestriction.register(UREntities.LIGHTNING_CHASER_ENTITY, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, LightningChaserEntity::canDragonSpawn);
    }
}
