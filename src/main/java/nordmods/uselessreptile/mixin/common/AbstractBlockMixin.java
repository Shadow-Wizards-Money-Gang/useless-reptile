package nordmods.uselessreptile.mixin.common;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nordmods.uselessreptile.common.entity.RiverPikehornEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void putDragonAssOff(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (player.isSneaking() && player.getFirstPassenger() instanceof RiverPikehornEntity dragon ) {
            dragon.stopRiding();
            dragon.setPosition(pos.up().toCenterPos());
            cir.setReturnValue(ActionResult.SUCCESS_NO_ITEM_USED);
        }
    }

    @Inject(method = "onUseWithItem", at = @At("HEAD"), cancellable = true)
    private void putDragonAssOff(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir){
        if (player.isSneaking() && player.getFirstPassenger() instanceof RiverPikehornEntity dragon ) {
            dragon.stopRiding();
            dragon.setPosition(pos.up().toCenterPos());
            cir.setReturnValue(ItemActionResult.SUCCESS);
        }
    }
}
