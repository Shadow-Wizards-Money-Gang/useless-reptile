package nordmods.uselessreptile.common.init;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import nordmods.uselessreptile.common.config.URConfig;
import nordmods.uselessreptile.common.entity.LightningChaserEntity;
import nordmods.uselessreptile.common.event.DragonEquipmentTooltipEntryEvent;
import nordmods.uselessreptile.common.event.MoleclawGetBlockMiningLevelEvent;
import nordmods.uselessreptile.common.network.URPacketHelper;
import nordmods.uselessreptile.common.util.LightningChaserSpawnTimer;

import java.util.ArrayList;
import java.util.List;

public class URModEvents {
    public static void init() {
        spawnLightningChaser();
        addDragonEquipmentTooltipEntries();
        getDefaultBlockMiningLevelForMoleclaw();
    }

    private static void spawnLightningChaser() {
        //Lightning Chaser spawn event
        ServerTickEvents.START_WORLD_TICK.register(world -> {
            if (world instanceof LightningChaserSpawnTimer worldTimer && world.isThundering()) {
                if (worldTimer.useless_reptile$getTimer() > 0) {
                    worldTimer.useless_reptile$setTimer(worldTimer.useless_reptile$getTimer() - 1);
                    return;
                }
                for (ServerPlayerEntity player : world.getPlayers()) {
                    if (!(player instanceof LightningChaserSpawnTimer playerTimer)) continue;
                    if (player.getY() < 60) continue;
                    if (playerTimer.useless_reptile$getTimer() > 0) continue;
                    if (URConfig.getConfig().lightningChaserThunderstormSpawnChance >= player.getRandom().nextFloat() * 100) {
                        double cos = Math.cos(Math.toRadians(player.getHeadYaw() + 180));
                        double sin = Math.sin(Math.toRadians(player.getHeadYaw() + 180));
                        BlockPos pos = player.getBlockPos();
                        BlockPos spawnPos = new BlockPos((int) (pos.getX() + sin * 128),
                                world.getTopY(Heightmap.Type.WORLD_SURFACE, (int) (pos.getX() + sin * 128), (int) (pos.getZ() + cos * 128)) + 16,
                                (int) (pos.getZ() + cos * 128));
                        while (!world.getBlockState(spawnPos).isAir()) spawnPos = spawnPos.up();
                        LightningChaserEntity lightningChaser = UREntities.LIGHTNING_CHASER_ENTITY.spawn(world, spawnPos, SpawnReason.EVENT);
                        if (lightningChaser != null) {
                            lightningChaser.setFlying(true);
                            lightningChaser.roamingSpot = new BlockPos(pos.getX(),
                                    world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.getX(), pos.getZ()),
                                    pos.getZ());
                            URPacketHelper.playSound(lightningChaser, URSounds.LIGHTNING_CHASER_DISTANT_ROAR, lightningChaser.getSoundCategory(), 1, 1, 1);
                        }
                        playerTimer.useless_reptile$setTimer(URConfig.getConfig().lightningChaserThunderstormSpawnTimerCooldown);
                        break;
                    }
                }
                worldTimer.useless_reptile$setTimer(1200);
            }
        });
    }

    private static void addDragonEquipmentTooltipEntries() {
        DragonEquipmentTooltipEntryEvent.EVENT.register(item -> {
            List<EntityType<?>> entityTypes = new ArrayList<>();
            RegistryEntry<Item> entry = Registries.ITEM.getEntry(item);
            if (entry.isIn(URTags.MOLECLAW_TAIL_ARMOR) || entry.isIn(URTags.MOLECLAW_CHESTPLATES) || entry.isIn(URTags.MOLECLAW_HELMETS))
                entityTypes.add(UREntities.MOLECLAW_ENTITY);
            if (entry.isIn(URTags.LIGHTNING_CHASER_TAIL_ARMOR) || entry.isIn(URTags.LIGHTNING_CHASER_CHESTPLATES) || entry.isIn(URTags.LIGHTNING_CHASER_HELMETS))
                entityTypes.add(UREntities.LIGHTNING_CHASER_ENTITY);
            return entityTypes;
        });
    }

    private static void getDefaultBlockMiningLevelForMoleclaw() {
        MoleclawGetBlockMiningLevelEvent.EVENT.register(blockState -> {
            if (blockState.isIn(BlockTags.INCORRECT_FOR_NETHERITE_TOOL)) return 5;
            if (blockState.isIn(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)) return 4;
            if (blockState.isIn(BlockTags.INCORRECT_FOR_IRON_TOOL)) return 3;
            if (blockState.isIn(BlockTags.INCORRECT_FOR_STONE_TOOL)) return 2;
            if (blockState.isIn(BlockTags.INCORRECT_FOR_WOODEN_TOOL)) return 1;
            return 0;
        });
    }
}
