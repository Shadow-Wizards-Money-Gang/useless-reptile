package nordmods.uselessreptile.common.init;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;
import nordmods.uselessreptile.common.entity.MoleclawEntity;
import nordmods.uselessreptile.common.entity.RiverPikehornEntity;
import nordmods.uselessreptile.common.entity.WyvernEntity;
import nordmods.uselessreptile.common.util.URSpawnGroup;

public class URSpawns {

    public static void init() {
        BiomeModifications.addSpawn(BiomeSelectors
                        .tag(URTags.SWAMP_WYVERN_SPAWN_WHITELIST)
                        .and(BiomeSelectors.tag(URTags.SWAMP_WYVERN_SPAWN_BLACKLIST).negate()),
                URSpawnGroup.DRAGON.spawnGroup,
                UREntities.WYVERN_ENTITY,
               1,
                1, 2);

        BiomeModifications.addSpawn(BiomeSelectors
                        .tag(URTags.MOLECLAW_SPAWN_WHITELIST)
                        .and(BiomeSelectors.tag(URTags.MOLECLAW_SPAWN_BLACKLIST).negate()),
                URSpawnGroup.UNDERGROUND_DRAGON.spawnGroup,
                UREntities.MOLECLAW_ENTITY,
                1,
               1, 3);

        BiomeModifications.addSpawn(BiomeSelectors
                        .tag(URTags.RIVER_PIKEHORN_SPAWN_WHITELIST)
                        .and(BiomeSelectors.tag(URTags.RIVER_PIKEHORN_SPAWN_BLACKLIST).negate()),
                URSpawnGroup.SMALL_DRAGON.spawnGroup,
                UREntities.RIVER_PIKEHORN_ENTITY,
                1,
              2, 4);

        //BiomeModifications.addSpawn(BiomeSelectors
        //                .tag(URTags.LIGHTNING_CHASER_SPAWN_WHITELIST)
        //                .and(BiomeSelectors.tag(URTags.LIGHTNING_CHASER_SPAWN_BLACKLIST).negate()),
        //        URSpawnGroup.DRAGON.spawnGroup,
        //        UREntities.LIGHTNING_CHASER_ENTITY,
        //        URConfig.getConfig().lightningChaserSpawnWeight,
        //        URConfig.getConfig().lightningChaserMinGroupSize, URConfig.getConfig().lightningChaserMaxGroupSize);

        SpawnRestriction.register(UREntities.WYVERN_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WyvernEntity::canDragonSpawn);
        SpawnRestriction.register(UREntities.MOLECLAW_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MoleclawEntity::canDragonSpawn);
        SpawnRestriction.register(UREntities.RIVER_PIKEHORN_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, RiverPikehornEntity::canDragonSpawn);
        //SpawnRestriction.register(UREntities.LIGHTNING_CHASER_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, LightningChaserEntity::canDragonSpawn);

    }
}
