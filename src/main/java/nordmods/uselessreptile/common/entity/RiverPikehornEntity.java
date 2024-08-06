package nordmods.uselessreptile.common.entity;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.UntamedActiveTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import nordmods.uselessreptile.common.config.URConfig;
import nordmods.uselessreptile.common.config.URMobAttributesConfig;
import nordmods.uselessreptile.common.entity.ai.goal.common.*;
import nordmods.uselessreptile.common.entity.ai.goal.river_pikehorn.*;
import nordmods.uselessreptile.common.entity.base.URFlyingDragonEntity;
import nordmods.uselessreptile.common.init.URAttributes;
import nordmods.uselessreptile.common.init.URItems;
import nordmods.uselessreptile.common.init.URSounds;
import nordmods.uselessreptile.common.item.FluteItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;

public class RiverPikehornEntity extends URFlyingDragonEntity {
    private final int huntCooldown = 3000;
    private int huntTimer = huntCooldown;
    public boolean forceTargetInWater = false;
    private final int eatCooldown = 200;
    private int eatTimer = eatCooldown;

    public RiverPikehornEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        experiencePoints = 5;
        setCanPickUpLoot(true);

        secondaryAttackDuration = 12;
        primaryAttackDuration = 12;
        canNavigateInFluids = true;
        inventory = new SimpleInventory(0);
        ticksUntilHeal = 400;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IS_HUNTING, false);
    }
    public static final TrackedData<Boolean> IS_HUNTING = DataTracker.registerData(RiverPikehornEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public boolean isHunting() {return dataTracker.get(IS_HUNTING);}
    public void setIsHunting (boolean state) {dataTracker.set(IS_HUNTING, state);}

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar animationData) {
        AnimationController<RiverPikehornEntity> main = new AnimationController<>(this, "main", TRANSITION_TICKS, this::mainController);
        AnimationController<RiverPikehornEntity> turn = new AnimationController<>(this, "turn", TRANSITION_TICKS, this::turnController);
        AnimationController<RiverPikehornEntity> attack = new AnimationController<>(this, "attack", 0, this::attackController);
        AnimationController<RiverPikehornEntity> eye = new AnimationController<>(this, "eye", 0, this::eyeController);
        main.setSoundKeyframeHandler(this::soundListenerMain);
        attack.setSoundKeyframeHandler(this::soundListenerAttack);
        animationData.add(main, turn, attack, eye);
    }

    private <A extends GeoEntity> PlayState eyeController(AnimationState<A> event) {
        return loopAnim("blink", event);
    }
    private <A extends GeoEntity> PlayState mainController(AnimationState<A> event) {
        event.getController().setAnimationSpeed(animationSpeed);
        if (hasVehicle()) return loopAnim("sit.head", event);
        if (isFlying()) {
            if (isMoving() || event.isMoving()) {
                if (getTiltState() == 1) return loopAnim("fly.straight.up", event);
                if (getTiltState() == 2) return loopAnim("fly.dive", event);
                if (isGliding() || shouldGlide) return loopAnim("fly.glide", event);
                return loopAnim("fly.straight", event);
            }
            event.getController().setAnimationSpeed(Math.max(animationSpeed, 1));
            return loopAnim("fly.idle", event);
        }
        if (getIsSitting() && !isDancing()) return loopAnim("sit", event);
        if (event.isMoving()) return loopAnim("walk", event);
        event.getController().setAnimationSpeed(1);
        if (isDancing()) return loopAnim("dance", event);
        return loopAnim("idle", event);
    }

    private <A extends GeoEntity> PlayState turnController(AnimationState<A> event) {
        byte turnState = getTurningState();
        event.getController().setAnimationSpeed(animationSpeed);

        if (isFlying() && (isMoving() || event.isMoving()) && !isSecondaryAttack() && !isMovingBackwards()) {
            if (turnState == 1) return loopAnim("turn.fly.left", event);
            if (turnState == 2) return loopAnim("turn.fly.right", event);
        }
        if (turnState == 1) return loopAnim("turn.left", event);
        if (turnState == 2) return loopAnim("turn.right", event);
        return loopAnim("turn.none", event);
    }

    private <A extends GeoEntity> PlayState attackController(AnimationState<A> event) {
        event.getController().setAnimationSpeed(1/ getCooldownModifier());
        if (isPrimaryAttack()) return playAnim( "attack" + getAttackType(), event);
        return playAnim("attack.none", event);
    }

    private <ENTITY extends GeoEntity> void soundListenerMain(SoundKeyframeEvent<ENTITY> event) {
        if (getWorld().isClient())
            switch (event.getKeyframeData().getSound()) {
                case "flap" -> playSound(SoundEvents.ENTITY_ENDER_DRAGON_FLAP, 1, 1.2F);
                case "woosh" -> playSound(URSounds.DRAGON_WOOSH, 0.7f, 1.2f);
                case "step" -> playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.5f, 0.8f);
            }
    }

    private <ENTITY extends GeoEntity> void soundListenerAttack(SoundKeyframeEvent<ENTITY> event) {
        if (getWorld().isClient())
            if (event.getKeyframeData().getSound().equals("attack")) playSound(URSounds.PIKEHORN_ATTACK, 1, 1);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return URSounds.PIKEHORN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return URSounds.PIKEHORN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return URSounds.PIKEHORN_DEATH;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (getVehicle() instanceof PlayerEntity && damageSource.isOf(DamageTypes.IN_WALL)) return true;
        return super.isInvulnerableTo(damageSource);
    }

    @Override
    public void tick() {
        super.tick();
        if (getVehicle() instanceof PlayerEntity) setHitboxModifiers(0.7f, 0.6f, 0);
        else if (isFlying() && isMoving()) setHitboxModifiers(0.6f, 1f, 0);
        else setHitboxModifiers(0.8f, 0.8f, 0);

        dropLootToOwner();

        if (!isTamed()) {
            if (huntTimer > 0 && !isHunting()) huntTimer--;
            else setIsHunting(true);

            ItemStack itemStack = getMainHandStack();
            if (itemStack.isIn(ItemTags.FISHES) && itemStack.getComponents().contains(DataComponentTypes.FOOD)) {
                if (eatTimer <= 0 || getMaxHealth() > getHealth()) {
                    eatFood(getWorld(), itemStack, itemStack.getComponents().get(DataComponentTypes.FOOD));
                    heal(getHealthRegenerationFromFood());
                    stopHunt();
                } else eatTimer--;
            }
        }

        if (isTamed()) {
            PlayerEntity owner = (PlayerEntity) getOwner();
            if (owner != null) {
                ItemStack main = owner.getMainHandStack();
                ItemStack offhand = owner.getOffHandStack();
                boolean mainCanTarget = main.getItem() instanceof FluteItem fluteItem && fluteItem.getFluteMode(main) == 1;
                boolean offhandCanTarget = offhand.getItem() instanceof FluteItem fluteItem && fluteItem.getFluteMode(offhand) == 1;
                if (owner.getItemCooldownManager().isCoolingDown(URItems.FLUTE) && (mainCanTarget || offhandCanTarget)) setIsHunting(true);
            }
        }

        if (isInsideWaterOrBubbleColumn()) {
            setSwimming(true);
            setFlying(true);
        }
        else setSwimming(false);

        if (getVehicle() instanceof PlayerEntity player) {
            getLookControl().setLockRotation(true);
            if (getWorld().isClient()) {
                prevYaw = getYaw();
                setYaw(player.getYaw());
                byte turnState = 0;
                float diff = prevYaw - getYaw();
                if (diff > 0) turnState = 1;
                if (diff < 0) turnState = 2;
                setTurningState(turnState);
            }
        } else getLookControl().setLockRotation(false);
    }

    public static DefaultAttributeContainer.Builder createPikehornAttributes() {
        return createDragonAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, attributes().riverPikehornDamage * attributes().dragonDamageMultiplier)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, attributes().riverPikehornKnockback * URMobAttributesConfig.getConfig().dragonKnockbackMultiplier)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, attributes().riverPikehornHealth * attributes().dragonHealthMultiplier)
                .add(EntityAttributes.GENERIC_ARMOR, attributes().riverPikehornArmor * attributes().dragonArmorMultiplier)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, attributes().riverPikehornArmorToughness * attributes().dragonArmorToughnessMultiplier)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, attributes().riverPikehornGroundSpeed * attributes().dragonGroundSpeedMultiplier)
                .add(URAttributes.DRAGON_VERTICAL_SPEED, attributes().riverPikehornVerticalSpeed)
                .add(URAttributes.DRAGON_ACCELERATION_DURATION, attributes().riverPikehornBaseAccelerationDuration)
                .add(URAttributes.DRAGON_GROUND_ROTATION_SPEED, attributes().riverPikehornRotationSpeedGround)
                .add(URAttributes.DRAGON_FLYING_ROTATION_SPEED, attributes().riverPikehornRotationSpeedAir)
                .add(URAttributes.DRAGON_PRIMARY_ATTACK_COOLDOWN, attributes().riverPikehornBasePrimaryAttackCooldown)
                .add(URAttributes.DRAGON_REGENERATION_FROM_FOOD, attributes().riverPikehornRegenerationFromFood);
    }

    @Override
    protected void initGoals() {
        goalSelector.add(1, new FlyingDragonCallBackGoal<>(this));
        goalSelector.add(1, new PikehornFluteCallGoal(this));
        goalSelector.add(1, new PikehornFollowGoal(this));
        goalSelector.add(2, new SitGoal(this));
        goalSelector.add(5, new PikehornAttackGoal(this, 4096 * 2));
        goalSelector.add(6, new PikehornHuntGoal(this));
        goalSelector.add(7, new FlyingDragonFlyDownGoal<>(this, 30));
        goalSelector.add(8, new DragonWanderAroundGoal(this));
        goalSelector.add(8, new FlyingDragonFlyAroundGoal<>(this, 30));
        goalSelector.add(9, new DragonLookAroundGoal(this));
        targetSelector.add(3, (new DragonRevengeGoal(this, new Class[0])).setGroupRevenge(new Class[0]));
        targetSelector.add(4, new DragonAttackWithOwnerGoal(this));
        targetSelector.add(4, new PikehornFluteTargetGoal(this));
        targetSelector.add(5, new DragonTrackOwnerAttackerGoal(this));
        if (URConfig.getConfig().dragonMadness) targetSelector.add(4, new UntamedActiveTargetGoal<>(this, PlayerEntity.class, true, null));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (isTamingItem(itemStack) && !isTamed()) {
            if (!player.isCreative()) player.setStackInHand(hand, new ItemStack(Items.WATER_BUCKET));
            setOwner(player);
            getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
            setPersistent();
            return ActionResult.SUCCESS;
        }

        if (isTamed() && isOwner(player)) {
            if (player.isSneaking() && itemStack.isEmpty()) startRiding(player);
        }
        return super.interactMob(player, hand);
    }

    public void attackMelee(LivingEntity target) {
        setPrimaryAttackCooldown(getMaxPrimaryAttackCooldown());
        setAttackType(random.nextInt(3)+1);
        tryAttack(target);
    }

    @Override
    protected void loot(ItemEntity item) {
        if (isOwnerClose()) return;
        if (getEquippedStack(EquipmentSlot.MAINHAND).isEmpty() && item.getStack().isIn(ItemTags.FISHES) || getEquippedStack(EquipmentSlot.MAINHAND).isOf(item.getStack().getItem())) {
            triggerItemPickedUpByEntityCriteria(item);
            ItemStack itemStack = item.getStack();
            equipStack(EquipmentSlot.MAINHAND, itemStack);
            updateDropChances(EquipmentSlot.MAINHAND);
            sendPickup(item, itemStack.getCount());
            item.discard();
        }
    }

    @Override
    public boolean hasTargetInWater() {
        return super.hasTargetInWater() || forceTargetInWater;
    }

    @Override
    public int getMaxAir() {
        return 1200;
    }

    public boolean isOwnerClose() {
        LivingEntity owner = getOwner();
        if (owner == null) return false;
        double distance = squaredDistanceTo(owner);
        return distance < getWidth() * 2.0f * (getWidth() * 2.0f);
    }

    private void dropLootToOwner() {
        if (!isTamed() || !isOwnerClose()) return;
        ItemStack stack = getEquippedStack(EquipmentSlot.MAINHAND).copy();
        if (!stack.isEmpty()) {
            dropStack(stack);
            getEquippedStack(EquipmentSlot.MAINHAND).decrement(stack.getCount());
            setIsHunting(false);
        }
    }

    @Override
    protected float getMovementSpeedModifier() {
        return super.getMovementSpeedModifier() / (isTouchingWater() ? 2 : 1);
    }

    public void stopHunt() {
        setIsHunting(false);
        huntTimer = huntCooldown + getRandom().nextInt(huntCooldown / 2);
        eatTimer = eatCooldown + getRandom().nextInt(eatCooldown / 2);
        setInAirTimer(getMaxInAirTimer());
    }

    @Override
    public boolean isFavoriteFood(ItemStack itemStack) {
        return itemStack.isIn(ItemTags.FISHES);
    }

    @Override
    public boolean isTamingItem(ItemStack itemStack) {
        return itemStack.isOf(Items.TROPICAL_FISH_BUCKET);
    }

    @Override
    public Box getAttackBox() {
        return getBoundingBox().expand(getScale(), 0, getScale());
    }

    @Override
    public String getDefaultVariant() {
        return "blue";
    }

    @Override
    public Vec3d getVehicleAttachmentPos(Entity vehicle) {
        return super.getVehicleAttachmentPos(vehicle).add(0, vehicle.getHeight() - vehicle.getEyeHeight(vehicle.getPose()) - 0.001, 0);
    }

    @Override
    public int getLimitPerChunk() {
        return URConfig.getConfig().pikehornMaxGroupSize * 2;
    }

    @Override
    public boolean isArmorSlot(EquipmentSlot slot) {
        return false;
    }
}
