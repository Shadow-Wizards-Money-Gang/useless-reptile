package nordmods.uselessreptile.common.entity.special;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import nordmods.uselessreptile.common.init.UREntities;
import nordmods.uselessreptile.common.init.URSounds;
import nordmods.uselessreptile.common.init.URStatusEffects;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class LightningBreathEntity extends ProjectileEntity implements ProjectileDamageHelper {
    private boolean spawnSoundPlayed = false;
    private int age;
    public static final int MAX_AGE = 10;
    public static final int MAX_LENGTH = 50;
    public float prevAlpha = 0.5f;
    public final LightningBreathBolt[] lightningBreathBolts = new LightningBreathBolt[5];

    public LightningBreathEntity(EntityType<? extends ProjectileEntity> entityType, World world, Entity owner) {
        super(entityType, world);
        age = 0;
        ignoreCameraFrustum = true;
        setOwner(owner);
    }

    public LightningBreathEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        this(entityType, world, null);
    }

    public LightningBreathEntity(World world, Entity owner) {
        this(UREntities.LIGHTNING_BREATH_ENTITY, world, owner);
    }

    public static final TrackedData<Integer> BEAM_LENGTH = DataTracker.registerData(LightningBreathEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public void setBeamLength(int state) {dataTracker.set(BEAM_LENGTH, state);}
    public int getBeamLength() {return dataTracker.get(BEAM_LENGTH);}

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(BEAM_LENGTH, 0);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!(getWorld() instanceof ServerWorld serverWorld)) return;
        Entity target = entityHitResult.getEntity();
        DamageSource source = getDamageSources().create(DamageTypes.LIGHTNING_BOLT, getOwner());
        if (target.isInvulnerableTo(source)) return;
        if (target.damage(source, getResultingDamage())) {
            target.playSound(URSounds.SHOCKWAVE_HIT, 1, random.nextFloat() + 1f);
            boolean wasOnFireBefore = target.isOnFire();
            LightningEntity fakeLightningSoINoNullPointerExceptionWouldHappenIHope = new LightningEntity(EntityType.LIGHTNING_BOLT, serverWorld);
            target.onStruckByLightning(serverWorld, fakeLightningSoINoNullPointerExceptionWouldHappenIHope);
            if (!wasOnFireBefore) {
                target.setFireTicks(0);
                target.setOnFire(false);
            }
            if (target instanceof LivingEntity livingEntity)
                livingEntity.addStatusEffect(new StatusEffectInstance(URStatusEffects.SHOCK, 400, 0, false, false), getOwner());
        }
    }

    @Override
    public void tick() {
        super.tick();
        tryPlaySpawnSound();
        if (++age <= MAX_AGE) {
            List<Entity> targets = getWorld().getOtherEntities(this, getBoundingBox(), this::canTarget);
            for (Entity target : targets) {
                EntityHitResult entityHitResult = new EntityHitResult(target);
                onEntityHit(entityHitResult);
            }

            if (!(getOwner() instanceof URDragonEntity dragon) || !dragon.canBreakBlocks()) {
                discard();
                return;
            }

            Iterable<BlockPos> blocks = BlockPos.iterateOutwards(getBlockPos(), 2, 1, 2);
            float harnessLimit = 20;
            List<FallingBlockEntity> fallingBlockEntities = new ArrayList<>();
            for (BlockPos blockPos : blocks) {
                BlockState blockState = getWorld().getBlockState(blockPos);
                if (dragon.isBlockProtected(blockPos)) continue;
                float hardness = blockState.getHardness(getWorld(), blockPos);
                if (hardness < 0) continue;
                harnessLimit -= hardness;
                if (harnessLimit < 0) break;
                FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock(getWorld(), blockPos, blockState);
                fallingBlockEntities.add(fallingBlockEntity);
            }
            List<FallingBlockEntity> sorted = new ArrayList<>();

            while (!fallingBlockEntities.isEmpty()) {
                int maxY = -1000;
                FallingBlockEntity toAdd = null;
                for (FallingBlockEntity fallingBlockEntity : fallingBlockEntities) {
                    if (fallingBlockEntity.getBlockY() > maxY) {
                        maxY = fallingBlockEntity.getBlockY();
                        toAdd = fallingBlockEntity;
                    }
                }
                if (toAdd != null) {
                    sorted.add(toAdd);
                    fallingBlockEntities.remove(toAdd);
                }
            }

            sorted.forEach(fallingBlockEntity -> {
                Vec3d velocity = getBlockPos().toCenterPos().subtract(fallingBlockEntity.getBlockPos().toCenterPos()).add(0, 1, 0).normalize();
                fallingBlockEntity.setVelocity(velocity);
            });
            discard();
        } else discard();
    }

    private void tryPlaySpawnSound() {
        if (!spawnSoundPlayed) {
            playSound(URSounds.SHOCKWAVE, 0.25f, 1);
            spawnSoundPlayed = true;
        }
    }

    private boolean canTarget(Entity target) {
        if (target instanceof EntityPart part) target = part.owner;
        if (target.isInvulnerableTo(getDamageSources().create(DamageTypes.LIGHTNING_BOLT))) return false;
        Entity owner = getOwner();
        LivingEntity ownerOwner = owner instanceof TameableEntity tameable ? tameable.getOwner() : null;
        if (target == ownerOwner) return false;
        if (target instanceof TameableEntity tameableEntity && tameableEntity.getOwner() == ownerOwner) return false;

        return true;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    public float getDefaultDamage() {
        return 16;
    }

    @Override
    public float getDamageScaling() {
        return 2;
    }

    public static class LightningBreathBolt {
        public final List<Segment> segments = new ArrayList<>();

        public record Segment (Vector3f startPoint, Vector3f endPoint) {}
    }
}
