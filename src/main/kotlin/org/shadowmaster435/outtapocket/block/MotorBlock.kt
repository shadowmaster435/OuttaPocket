package org.shadowmaster435.outtapocket.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldView
import net.minecraft.world.tick.ScheduledTickView
import org.shadowmaster435.outtapocket.init.ModBlocks

class MotorBlock(settings: Settings) : MechanicalBlock(settings) {
    override fun getCodec(): MapCodec<out MotorBlock> {
        return createCodec<MotorBlock> { settings: Settings -> MotorBlock(settings) }
    }

    init {
        defaultState = stateManager.defaultState.with(Properties.POWERED, false)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        world: WorldView,
        tickView: ScheduledTickView,
        pos: BlockPos,
        direction: Direction,
        neighborPos: BlockPos,
        neighborState: BlockState,
        random: Random?
    ): BlockState {
        return state.with(Properties.POWERED, isRedstonePoweredAtInput(world,pos))
    }


    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape? {

        return shape[state.get(FACING)]
    }

    companion object {
        fun shouldPowerGear(sourceDir: Direction, motorState: BlockState): Boolean {
            return isValidSide(sourceDir, motorState) && motorState.get(Properties.POWERED)
        }
        fun isValidSide(sourceDir: Direction, motorState: BlockState): Boolean {
            return motorState.isOf(ModBlocks.MOTOR) &&  motorState.get(FACING) == sourceDir.opposite
        }
    }
}