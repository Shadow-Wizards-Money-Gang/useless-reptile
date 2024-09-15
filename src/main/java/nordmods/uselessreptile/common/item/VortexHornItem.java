package nordmods.uselessreptile.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.InstrumentTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import nordmods.uselessreptile.common.init.URItems;
import nordmods.uselessreptile.common.item.component.URDragonDataStorageComponent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VortexHornItem extends GoatHornItem {
    private final int maxCapacity;
    public VortexHornItem(Settings settings, TagKey<Instrument> instrumentTag, int maxCapacity) {
        super(settings, instrumentTag);
        this.maxCapacity = maxCapacity;
    }

    public VortexHornItem(Settings settings,int maxCapacity) {
        this(settings, InstrumentTags.GOAT_HORNS, maxCapacity);
    }

    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof URDragonEntity dragon && dragon.getOwner() == user && !user.isSneaking()) {
            if (tryCollectDragon(stack, user, dragon, hand)) {
                user.playSound(SoundEvents.ITEM_BOTTLE_FILL); //TODO
                return ActionResult.SUCCESS;
            }
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.isSneaking()) {
            if (tryMassCatchOrRelease(stack, user, world, hand)) return TypedActionResult.success(stack);
        }
        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        if (context.getPlayer() instanceof PlayerEntity user) {
            World world = context.getWorld();
            Hand hand = context.getHand();
            if (user.isSneaking()) {
                if (tryMassCatchOrRelease(stack, user, world, hand)) return ActionResult.SUCCESS;
            }
            BlockPos pos = context.getBlockPos();
            BlockState blockState = world.getBlockState(pos);
            Direction direction = context.getSide();
            if (!blockState.getCollisionShape(world, pos).isEmpty()) pos = pos.offset(direction);

            if (tryCreateDragon(stack, user, world, hand, pos)) {
                user.playSound(SoundEvents.ITEM_BOTTLE_EMPTY); //TODO
                return ActionResult.SUCCESS;
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (stack.getComponents().contains(URItems.DRAGON_STORAGE_COMPONENT)) {
            URDragonDataStorageComponent dataComponent = stack.get(URItems.DRAGON_STORAGE_COMPONENT);
            if (dataComponent != null) {
                tooltip.add(Text.literal(getCurrentCapacity(stack) + " / " + maxCapacity));
            }
            tooltip.add(Text.empty());
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    protected boolean tryMassCatchOrRelease(ItemStack stack, PlayerEntity user, World world, Hand hand) {
        Box box = new Box(user.getBlockPos()).expand(2);
        List<URDragonEntity> dragons = world.getEntitiesByClass(URDragonEntity.class, box, entity -> entity.getOwner() == user);
        if (dragons.isEmpty() || getCurrentCapacity(stack) >= getMaxCapacity()) {
            URDragonDataStorageComponent dataComponent = stack.get(URItems.DRAGON_STORAGE_COMPONENT);
            if (dataComponent != null) {
                for (int i = 0; i < dataComponent.entityData().size(); i++) tryCreateDragon(stack, user, world, hand, user.getBlockPos());
                user.playSound(SoundEvents.ITEM_BUCKET_EMPTY); //TODO
                return true;
            }
        } else {
            dragons.sort(Comparator.comparingDouble((dragon) -> dragon.squaredDistanceTo(dragon.getOwner())));
            for (URDragonEntity dragon : dragons) {
                if (!tryCollectDragon(stack, user, dragon, hand)) break;
            }
            user.playSound(SoundEvents.ITEM_BUCKET_FILL); //TODO
            return true;
        }
        return false;
    }

    protected boolean tryCollectDragon(ItemStack stack, PlayerEntity user, URDragonEntity dragon, Hand hand) {
        if (getCurrentCapacity(stack) >= getMaxCapacity()) return false;

        dragon.stopRiding();
        dragon.removeAllPassengers();

        URDragonDataStorageComponent dataComponent = stack.get(URItems.DRAGON_STORAGE_COMPONENT);
        URDragonDataStorageComponent appliedComponent;
        if (dataComponent != null) {
            List<NbtComponent> dragons = new ArrayList<>(dataComponent.entityData());
            NbtComponent data = URDragonDataStorageComponent.createData(dragon);
            dragons.add(data);
            appliedComponent = new URDragonDataStorageComponent(dragons);
        } else appliedComponent = URDragonDataStorageComponent.DEFAULT;
        stack.set(URItems.DRAGON_STORAGE_COMPONENT, appliedComponent);
        user.setStackInHand(hand, stack);

        spawnCloud(dragon);

        dragon.discard();
        return true;
    }

    protected boolean tryCreateDragon(ItemStack stack, PlayerEntity user, World world, Hand hand, BlockPos pos) {
        URDragonDataStorageComponent dataComponent = stack.get(URItems.DRAGON_STORAGE_COMPONENT);
        if (dataComponent != null) {
            List<NbtComponent> dragons = new ArrayList<>(dataComponent.entityData());
            if (dragons.isEmpty()) return false;
            NbtComponent last = dragons.getLast();
            if (!world.isClient()) {
                Entity dragon = URDragonDataStorageComponent.createEntity(last , world);
                if (dragon == null) return false;
                dragon.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                if (dragon instanceof URDragonEntity urDragon) {
                    urDragon.setHomePoint(pos);
                    urDragon.setBoundedInstrumentSound(URDragonEntity.getInstrument(stack));
                    urDragon.updateEquipment();
                    spawnCloud(urDragon);
                }
                world.spawnEntity(dragon);
            }
            dragons.removeLast();
            stack.set(URItems.DRAGON_STORAGE_COMPONENT, new URDragonDataStorageComponent(dragons));
            user.setStackInHand(hand, stack);
            return true;
        }
        return false;
    }

    protected void spawnCloud(URDragonEntity dragon) {
        MinecraftServer server = dragon.getServer();
        if (server != null) {
            double x = dragon.getX();
            double y = dragon.getY();
            double z = dragon.getZ();
            ParticleS2CPacket packet = new ParticleS2CPacket(ParticleTypes.CLOUD, false,
                    x, y, z,
                    dragon.getWidth() / 2, dragon.getHeight(), dragon.getWidth() / 2, 0.05f, 20);
            server.getPlayerManager().sendToAround(null, x, y, z, 128, dragon.getWorld().getRegistryKey(), packet);
        }
    }

    protected int getCurrentCapacity(ItemStack stack) {
        if (stack.getComponents().contains(URItems.DRAGON_STORAGE_COMPONENT)) {
            URDragonDataStorageComponent dataComponent = stack.get(URItems.DRAGON_STORAGE_COMPONENT);
            if (dataComponent != null) return dataComponent.entityData().size();
        }
        return 0;
    }

    protected int getMaxCapacity() {
        return maxCapacity;
    }
}
