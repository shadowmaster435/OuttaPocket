package org.shadowmaster435.outtapocket.mixin_helpers

import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import org.shadowmaster435.outtapocket.init.ModBlocks

object PistonHandlerHelper {

    @JvmStatic
    fun isSixSidedPiston(state: BlockState): Boolean {
        return state.isOf(ModBlocks.SIX_SIDED_STICKY_PISTON)
    }

    @JvmStatic
    fun isSixSidedPistonStuck(state: BlockState, adjacentState: BlockState): Boolean {
        return !adjacentState.isOf(ModBlocks.SIX_SIDED_STICKY_PISTON)
    }

}