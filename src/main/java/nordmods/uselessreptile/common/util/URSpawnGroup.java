package nordmods.uselessreptile.common.util;

import net.minecraft.entity.SpawnGroup;

//credits to Hybrid Aquatic code
public enum URSpawnGroup {
    DRAGON("ur_dragon", 12, true, false, 128),
    UNDERGROUND_DRAGON("ur_underground_dragon", 6, true, false, 128),
    SMALL_DRAGON("ur_small_sragon", 6, true, false, 128);

    public SpawnGroup spawnGroup;
    public final String name;
    public final int spawnCap;
    public final boolean peaceful;
    public final boolean rare;
    public final int immediateDespawnRange;

    URSpawnGroup(String name, int spawnCap, boolean peaceful, boolean rare, int immediateDespawnRange) {
        this.name = name;
        this.spawnCap = spawnCap;
        this.peaceful = peaceful;
        this.rare = rare;
        this.immediateDespawnRange = immediateDespawnRange;
    }
}
