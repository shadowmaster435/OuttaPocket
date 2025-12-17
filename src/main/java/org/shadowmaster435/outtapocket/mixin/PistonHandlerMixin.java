package org.shadowmaster435.outtapocket.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonHandler;
import org.shadowmaster435.outtapocket.init.ModBlocks;
import org.shadowmaster435.outtapocket.mixin_helpers.PistonHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonHandler.class)
public class PistonHandlerMixin {
    @ModifyReturnValue(method = "isBlockSticky", at = @At("RETURN"))
    private static boolean isSixSidedPiston(boolean original, @Local(argsOnly = true) BlockState state) {
        return PistonHandlerHelper.isSixSidedPiston(state) || original;
    }

    @Inject(method = "isAdjacentBlockStuck", at = @At("HEAD"), cancellable = true)
    private static void isSixSidedPistonStuck(BlockState state, BlockState adjacentState, CallbackInfoReturnable<Boolean> cir) {
        if (state.isOf(ModBlocks.SIX_SIDED_STICKY_PISTON)) {
            cir.setReturnValue(PistonHandlerHelper.isSixSidedPistonStuck(state, adjacentState));
        }
    }
}
