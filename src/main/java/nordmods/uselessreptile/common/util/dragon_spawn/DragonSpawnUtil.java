package nordmods.uselessreptile.common.util.dragon_spawn;

import net.minecraft.block.Block;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;

import java.util.ArrayList;
import java.util.List;

public class DragonSpawnUtil {

    public static boolean isBiomeInList(List<Codecs.TagEntryId> list, WorldAccess world, BlockPos blockPos) {
        RegistryEntry<Biome> biome = world.getBiome(blockPos);

        for (Codecs.TagEntryId tagEntryId : list) {
            if (tagEntryId.tag())
                if (biome.isIn(TagKey.of(RegistryKeys.BIOME, tagEntryId.id()))) return true;
                else if (biome.matchesId(tagEntryId.id())) return true;
        }

        return false;
    }

    public static boolean isBlockInList(List<Codecs.TagEntryId> list, WorldAccess world, BlockPos blockPos) {
        RegistryEntry<Block> block = world.getBlockState(blockPos.down()).getRegistryEntry();

        for (Codecs.TagEntryId tagEntryId : list) {
            if (tagEntryId.tag())
                if (block.isIn(TagKey.of(RegistryKeys.BLOCK, tagEntryId.id()))) return true;
                else if (block.matchesId(tagEntryId.id())) return true;
        }

        return false;
    }

    public static void assignVariantFromList(URDragonEntity entity, List<DragonSpawn> variants, SpawnReason spawnReason) {
        int totalWeight = 0;
        for (DragonSpawn variant : variants) totalWeight += variant.conditions().weight();
        boolean canWarn = spawnReason == SpawnReason.NATURAL
                || spawnReason == SpawnReason.EVENT
                || spawnReason == SpawnReason.CHUNK_GENERATION
                || spawnReason == SpawnReason.BREEDING;
        if (totalWeight <= 0) {
            if (canWarn) UselessReptile.LOGGER.warn("Failed to set variant for {} at {} as none can spawn there. Setting default", entity.getName().getString(), entity.getBlockPos());
            entity.setVariant(entity.getDefaultVariant());
            return;
        }

        int roll = entity.getRandom().nextInt(totalWeight);
        int previousBound = 0;

        for (DragonSpawn variant : variants) {
            if (roll >= previousBound && roll < previousBound + variant.conditions().weight()) {
                entity.setVariant(variant.variant());
                break;
            }
            previousBound += variant.conditions().weight();
        }
    }

    public static List<DragonSpawn> getAvailableVariants(WorldAccess world, URDragonEntity entity) {
        return getAvailableVariants(world, entity.getBlockPos(), entity.getDragonID());
    }

    public static List<DragonSpawn> getAvailableVariants(WorldAccess world, BlockPos pos, String name) {
        List<DragonSpawn> variants = DragonSpawn.getAllVariants(name);
        if (variants == null) throw new RuntimeException("Failed to get variants for " + name);

        List<DragonSpawn> allowedVariants = new ArrayList<>(variants.size());
        variants.forEach(variant -> {
            //altitude check
            if (variant.conditions().altitudeRestriction().isPresent()) {
                DragonSpawn.SpawnConditions.AltitudeRestriction restriction = variant.conditions().altitudeRestriction().get();
                if (restriction.min() > pos.getY() || restriction.max() <= pos.getY()) return;
            }
            //banned tagEntries check (blacklist)
            if (variant.conditions().bannedBiomes().isPresent()) {
                List <Codecs.TagEntryId> list = variant.conditions().bannedBiomes().get();
                if (!list.isEmpty() && isBiomeInList(list, world, pos)) return;
            }
            //allowed tagEntries check (whitelist)
            if (variant.conditions().allowedBiomes().isPresent()) {
                List <Codecs.TagEntryId> list = variant.conditions().allowedBiomes().get();
                if (!list.isEmpty() && !isBiomeInList(list, world, pos)) return;
            }
            //banned blocks check (blacklist)
            if (variant.conditions().bannedBlocks().isPresent()) {
                List <Codecs.TagEntryId> list = variant.conditions().bannedBlocks().get();
                if (!list.isEmpty() && isBlockInList(list, world, pos)) return;
            }
            //allowed blocks check (whitelist)
            if (variant.conditions().allowedBlocks().isPresent()) {
                List <Codecs.TagEntryId> list = variant.conditions().allowedBlocks().get();
                if (!list.isEmpty() && !isBlockInList(list, world, pos)) return;
            }

            allowedVariants.add(variant);
        });
        return allowedVariants;
    }
}
