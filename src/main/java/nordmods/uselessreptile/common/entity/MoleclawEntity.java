package nordmods.uselessreptile.common.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import nordmods.uselessreptile.common.config.URConfig;
import nordmods.uselessreptile.common.config.URMobAttributesConfig;
import nordmods.uselessreptile.common.entity.ai.goal.common.*;
import nordmods.uselessreptile.common.entity.ai.goal.moleclaw.MoleclawAttackGoal;
import nordmods.uselessreptile.common.entity.ai.goal.moleclaw.MoleclawEscapeLightGoal;
import nordmods.uselessreptile.common.entity.ai.goal.moleclaw.MoleclawUntamedTargetGoal;
import nordmods.uselessreptile.common.entity.ai.navigation.MoleclawNavigation;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import nordmods.uselessreptile.common.entity.base.URRideableDragonEntity;
import nordmods.uselessreptile.common.event.MoleclawGetBlockMiningLevelEvent;
import nordmods.uselessreptile.common.gui.MoleclawScreenHandler;
import nordmods.uselessreptile.common.init.URAttributes;
import nordmods.uselessreptile.common.init.URSounds;
import nordmods.uselessreptile.common.init.URTags;
import nordmods.uselessreptile.common.network.GUIEntityToRenderS2CPacket;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;

import java.util.List;

public class MoleclawEntity extends URRideableDragonEntity {
    public int attackDelay = 0;
    public static final float defaultWidth = 2f;
    public static final float defaultHeight = 2.9f;
    private int panicSoundDelay = 0;

    public MoleclawEntity(EntityType<? extends URRideableDragonEntity> entityType, World world) {
        super(entityType, world);
        experiencePoints = 20;
        navigation = new MoleclawNavigation(this, world);

        pitchLimitGround = 50;
        baseTamingProgress = 64;
        ticksUntilHeal = 400;
    }

    public static boolean canDragonSpawn(EntityType<? extends MobEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (world.getLightLevel(LightType.SKY, pos) > 0 || world.getLightLevel(LightType.BLOCK, pos) > 0) return false;
        return URDragonEntity.canDragonSpawn(type, world, spawnReason, pos, random);
    }

    @Override
    protected void initGoals() {
        goalSelector.add(1, new SwimGoal(this));
        goalSelector.add(2, new MoleclawEscapeLightGoal(this));
        goalSelector.add(2, new DragonCallBackGoal(this));
        goalSelector.add(3, new SitGoal(this));
        goalSelector.add(4, new DragonConsumeFoodFromInventoryGoal(this));
        goalSelector.add(8, new MoleclawAttackGoal(this, 512));
        goalSelector.add(9, new DragonReturnToHomePoint(this));
        goalSelector.add(10, new DragonWanderAroundGoal(this));
        goalSelector.add(11, new DragonLookAroundGoal(this));
        targetSelector.add(5, new MoleclawUntamedTargetGoal<>(this, PlayerEntity.class));
        targetSelector.add(6, new MoleclawUntamedTargetGoal<>(this, ChickenEntity.class));
        targetSelector.add(5, new DragonAttackWithOwnerGoal(this));
        targetSelector.add(6, new DragonTrackOwnerAttackerGoal(this));
        targetSelector.add(4, new DragonRevengeGoal(this));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IS_PANICKING, false);
    }
    public static final TrackedData<Boolean> IS_PANICKING = DataTracker.registerData(MoleclawEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public boolean isPanicking() {return dataTracker.get(IS_PANICKING);}
    public void setIsPanicking (boolean state) {dataTracker.set(IS_PANICKING, state);}

    public static DefaultAttributeContainer.Builder createMoleclawAttributes() {
        return createDragonAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, attributes().moleclawDamage * attributes().dragonDamageMultiplier)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, attributes().moleclawKnockback * URMobAttributesConfig.getConfig().dragonKnockbackMultiplier)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, attributes().moleclawHealth * attributes().dragonHealthMultiplier)
                .add(EntityAttributes.GENERIC_ARMOR, attributes().moleclawArmor * attributes().dragonArmorMultiplier)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, attributes().moleclawArmorToughness * attributes().dragonArmorToughnessMultiplier)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, attributes().moleclawGroundSpeed * attributes().dragonGroundSpeedMultiplier)
                .add(URAttributes.DRAGON_GROUND_ROTATION_SPEED, attributes().moleclawRotationSpeedGround)
                .add(URAttributes.DRAGON_PRIMARY_ATTACK_COOLDOWN, attributes().moleclawBasePrimaryAttackCooldown)
                .add(URAttributes.DRAGON_SECONDARY_ATTACK_COOLDOWN, attributes().moleclawBaseSecondaryAttackCooldown)
                .add(URAttributes.DRAGON_REGENERATION_FROM_FOOD, attributes().moleclawRegenerationFromFood)
                .add(URAttributes.MOLECLAW_MINING_LEVEL, 0);

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar animationData) {
        AnimationController<MoleclawEntity> main = new AnimationController<>(this, "main", TRANSITION_TICKS, this::mainController);
        AnimationController<MoleclawEntity> turn = new AnimationController<>(this, "turn", TRANSITION_TICKS, this::turnController);
        AnimationController<MoleclawEntity> attack = new AnimationController<>(this, "attack", 0, this::attackController);
        AnimationController<MoleclawEntity> eye = new AnimationController<>(this, "eye", 0, this::eyeController);
        main.setSoundKeyframeHandler(this::soundListenerMain);
        attack.setSoundKeyframeHandler(this::soundListenerAttack);
        animationData.add(main, turn, attack, eye);
    }

    private <ENTITY extends GeoEntity> void soundListenerMain(SoundKeyframeEvent<ENTITY> event) {
        if (getWorld().isClient())
            if (event.getKeyframeData().getSound().equals("step"))
                playSound(getStepSound(getBlockPos(), getWorld().getBlockState(getBlockPos())), 1, 1);
    }

    private <ENTITY extends GeoEntity> void soundListenerAttack(SoundKeyframeEvent<ENTITY> event) {
        if (getWorld().isClient())
            switch (event.getKeyframeData().getSound()) {
                case "attack_strong" -> playSound(URSounds.MOLECLAW_STRONG_ATTACK, 1, 1F);
                case "attack" -> playSound(URSounds.MOLECLAW_ATTACK, 1, 1);
            }
    }

    private <A extends GeoEntity> PlayState eyeController(AnimationState<A> event) {
        return loopAnim("blink", event);
    }

    private <A extends GeoEntity> PlayState mainController(AnimationState<A> event) {
        event.getController().setAnimationSpeed(animationSpeed);
        if (getIsSitting() && !isDancing()) return loopAnim("sit", event);
        if (event.isMoving() || isMoveForwardPressed() || isMovingBackwards()) {
            if (isPanicking()) return loopAnim("panic", event);
            return loopAnim("walk", event);
        }
        event.getController().setAnimationSpeed(1);
        if (isDancing() && !hasPassengers()) return loopAnim("dance", event);
        if (isPanicking()) return loopAnim("panic.idle", event);
        return loopAnim("idle", event);
    }

    private <A extends GeoEntity> PlayState turnController(AnimationState<A> event) {
        byte turnState = getTurningState();
        event.getController().setAnimationSpeed(animationSpeed);
        if (turnState == 1) return loopAnim("turn.left", event);
        if (turnState == 2) return loopAnim("turn.right", event);
        return loopAnim("turn.none", event);
    }

    private <A extends GeoEntity> PlayState attackController(AnimationState<A> event){
        event.getController().setAnimationSpeed(1/ getCooldownModifier());
        if (isSecondaryAttack()) return playAnim( "attack.normal" + getAttackType(), event);
        if (isPrimaryAttack()) {
            if (isPanicking()) return playAnim( "attack.strong.panic", event);
            return playAnim( "attack.strong", event);
        }
        return loopAnim("attack.none", event);
    }

    @Override
    public void tick() {
        super.tick();
        if (!getIsSitting()) setHitboxModifiers(1, 1, 2.5f);
        else setHitboxModifiers(0.75f, 1f, 2.5f);
        tryPanic();

        if (canBeControlledByRider()) {
            if (isSecondaryAttackPressed && getSecondaryAttackCooldown() == 0) scheduleNormalAttack();
            if (isPrimaryAttackPressed && getPrimaryAttackCooldown() == 0) scheduleStrongAttack();
        }

        if (attackDelay > 0) {
            attackDelay++;
            if (attackDelay > TRANSITION_TICKS + 1) {
                if (isPrimaryAttack()) strongAttack();
                if (isSecondaryAttack()) meleeAttack();
                attackDelay = 0;
            }
        }
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (!isAlive()) return;

        if (!isMoving()) setSprinting(false);
        if (isSprinting()) setSpeedMod(1.1f);
        else setSpeedMod(1f);
        if (isMovingBackwards()) setSpeedMod(0.6f);
        float speed = (float) getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        setMovementSpeed(speed * getSpeedModifier());

        if (canBeControlledByRider()) {
            PlayerEntity rider = (PlayerEntity) getControllingPassenger();

            float f1 = MathHelper.clamp(rider.forwardSpeed, -forwardSpeed, forwardSpeed);

            if (isSprintPressed()) setSprinting(true);
            setMovingBackwards(isMoveBackPressed() || (!isMoveForwardPressed() && !isMoveBackPressed() && isMoving()));
            if (isMovingBackwards()) setSprinting(false);
            setRotation(rider);
            setPitch(MathHelper.clamp(rider.getPitch(), -getPitchLimit(), getPitchLimit()));
            if (isJumpPressed() && isOnGround()) jump();

            super.travel(new Vec3d(0, movementInput.y, f1));
        } else {
            super.travel(movementInput);
        }
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        if (!getWorld().isClient()) GUIEntityToRenderS2CPacket.send((ServerPlayerEntity) player, this);
        return MoleclawScreenHandler.createScreenHandler(syncId, inv, inventory);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (isTamingItem(itemStack) && !isTamed()) {
            eat(player, hand, itemStack);
            if (random.nextInt(3) == 0) setTamingProgress(getTamingProgress() - 2);
            else setTamingProgress(getTamingProgress() - 1);
            if (player.isCreative()) setTamingProgress(0);
            if (getTamingProgress() <= 0) {
                setOwner(player);
                getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
            } else {
                getWorld().sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
            }
            setPersistent();
            return ActionResult.SUCCESS;
        }

        if (isTamed()) {
            if (player.isSneaking() && itemStack.isEmpty() && isOwner(player)) {
                player.openHandledScreen(this);
                return ActionResult.SUCCESS;
            }
        }
        return super.interactMob(player, hand);
    }

    public void meleeAttack() {
        List<Entity> targets = getWorld().getOtherEntities(this, getAttackBox(), livingEntity -> !getPassengerList().contains(livingEntity));
        if (!targets.isEmpty()) for (Entity mob: targets) {
            Box targetBox = mob.getBoundingBox();
            if (doesCollide(targetBox, getAttackBox())) tryAttack(mob);
        }
    }

    public void strongAttack() {
        List<Entity> targets = getWorld().getOtherEntities(this, getSecondaryAttackBox(), livingEntity -> !getPassengerList().contains(livingEntity));
        if (!targets.isEmpty()) for (Entity mob : targets) {
            Box targetBox = mob.getBoundingBox();
            if (doesCollide(targetBox, getSecondaryAttackBox())) tryAttack(mob);
        }

        if (!canBreakBlocks()) return;

        Box box = getSecondaryAttackBox();
        Iterable<BlockPos> blocks = BlockPos.iterate((int) box.minX, (int) box.minY, (int) box.minZ, (int) box.maxX, (int) box.maxY, (int) box.maxZ);
        float maxMiningLevel = (float) getAttributeValue(URAttributes.MOLECLAW_MINING_LEVEL);
        if (hasStatusEffect(StatusEffects.STRENGTH)) maxMiningLevel += getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1;
        if (hasStatusEffect(StatusEffects.WEAKNESS)) maxMiningLevel -= getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1;
        for (BlockPos blockPos : blocks) {
            if (isBlockProtected(blockPos)) continue;

            BlockState blockState = getWorld().getBlockState(blockPos);
            float miningLevel = MoleclawGetBlockMiningLevelEvent.EVENT.invoker().getMiningLevel(blockState);
            if (!blockState.isAir() && miningLevel <= maxMiningLevel) {
                boolean shouldDrop = getRandom().nextDouble() * 100 <= URConfig.getConfig().blockDropChance;
                getWorld().breakBlock(blockPos, shouldDrop, this);
            }
        }
    }

    @Override
    public boolean canBreakBlocks() {
        if (getWorld().isClient()) return false;
        boolean shouldBreakBlocks = isTamed() ? URConfig.getConfig().moleclawGriefing.canTamedBreak() : URConfig.getConfig().moleclawGriefing.canUntamedBreak();
        return shouldBreakBlocks && getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
    }

    @Override
    public Box getAttackBox() {
        Vec3d rotationVec = getRotationVector(0, getYaw());
        double x = rotationVec.x * 2;
        double z = rotationVec.z * 2;
        return new Box(getPos().getX() + x - 1.5, getPos().getY(), getPos().getZ() + z - 1.5,
                getPos().getX() + x + 1.5, getPos().getY() + getHeight(), getPos().getZ() + z + 1.5);
    }

    @Override
    public Box getSecondaryAttackBox() {
        double x = -Math.sin(Math.toRadians(getYaw())) * 2;
        double y = -Math.sin(Math.toRadians(getPitch()));
        double z = Math.cos(Math.toRadians(getYaw())) * 2;
        return new Box(getPos().getX() + x - 1.25, getPos().getY() + y + 0.5, getPos().getZ() + z - 1.25,
                getPos().getX() + x + 1.25, getPos().getY() + getHeight() + 1 + y, getPos().getZ() + z + 1.25);
    }

    @Override
    public String getDefaultVariant() {
        return "black";
    }

    public void tryPanic() {
        playPanicSound();
        if (!hasLightProtection()) setIsPanicking(isTooBrightAtPos(getBlockPos()));
        else setIsPanicking(false);
    }

    public boolean hasLightProtection() {
        return getEquippedStack(EquipmentSlot.HEAD).isIn(URTags.PROTECTS_MOLECLAW_FROM_LIGHT);
    }

    public boolean isTooBrightAtPos(BlockPos blockPos) {
        return getLightAtPos(blockPos, this) > 7 && !hasLightProtection();
    }

    public static int getLightAtPos(BlockPos blockPos, LivingEntity entity) {
        World world = entity.getWorld();
        int lightLevelBlock = world.getLightLevel(LightType.BLOCK, blockPos);
        int lightLevelSky = world.getLightLevel(LightType.SKY, blockPos);
        long timeOfDay = world.getTimeOfDay() % 24000;
        boolean isDayTime = (timeOfDay < 13000 || timeOfDay > 23000) && !world.getDimension().hasFixedTime();
        return Math.max(lightLevelBlock, isDayTime ? lightLevelSky : 0);
    }

    @Override
    public double getSwimHeight() {
        return 1;
    }

    @Override
    public boolean canBeControlledByRider() {
        return super.canBeControlledByRider() && !isPanicking();
    }

    public void scheduleNormalAttack() {
        setSecondaryAttackCooldown(getMaxSecondaryAttackCooldown());
        if (attackDelay == 0) attackDelay = 6;
        setAttackType(random.nextInt(2)+1);
    }

    public void scheduleStrongAttack() {
        if (attackDelay == 0) attackDelay = 6;
        setPrimaryAttackCooldown(getMaxPrimaryAttackCooldown());
    }

    private SoundEvent getStepSound(BlockPos pos, BlockState state) {
        if (state.getFluidState().isEmpty()) {
            BlockState blockState = getWorld().getBlockState(pos.up());
            BlockSoundGroup blockSoundGroup = blockState.isIn(BlockTags.INSIDE_STEP_SOUND_BLOCKS) ? blockState.getSoundGroup() : state.getSoundGroup();
            return blockSoundGroup.getStepSound();
        }
        return getSwimSound();
    }

    @Override
    public boolean disablesShield() {
        return isPrimaryAttack();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return URSounds.MOLECLAW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return URSounds.MOLECLAW_DEATH;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return URSounds.MOLECLAW_AMBIENT;
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return -world.getPhototaxisFavor(pos);
    }

    private void playPanicSound() {
        if (isPanicking()) {
            if (panicSoundDelay == 0) {
                playSound(URSounds.MOLECLAW_PANICKING, 1 ,1);
                panicSoundDelay = random.nextInt(41) + 60;
            }
            else panicSoundDelay--;
        } else panicSoundDelay = 2;
    }

    @Override
    public boolean isFavoriteFood(ItemStack itemStack){
        return itemStack.isOf(Items.BEETROOT);
    }

    @Override
    public int getLimitPerChunk() {
        return URConfig.getConfig().moleclawMaxGroupSize * 2;
    }
}
