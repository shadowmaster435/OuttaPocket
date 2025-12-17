package org.shadowmaster435.outtapocket.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ButtonBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ButtonBlock.class)
public class ButtonBlockMixin {
    @Inject(method = "onUse", at = @At("RETURN"), cancellable = true)
    public void changeToPass(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (cir.getReturnValue() == ActionResult.CONSUME) {
            cir.setReturnValue(ActionResult.PASS);

        }
    }
}
