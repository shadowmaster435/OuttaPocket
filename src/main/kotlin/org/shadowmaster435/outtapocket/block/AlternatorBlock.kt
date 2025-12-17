package org.shadowmaster435.outtapocket.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import org.shadowmaster435.outtapocket.init.ModBlocks

class AlternatorBlock(settings: Settings) : MechanicalBlock(settings) {


    override fun getCodec(): MapCodec<out AlternatorBlock> {
        return createCodec<AlternatorBlock> { settings: Settings -> AlternatorBlock(settings) }
    }

    init {
        defaultState = stateManager.defaultState.with(Properties.POWERED, false)
    }


    override fun getWeakRedstonePower(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        direction: Direction
    ): Int {
        return if (direction == state.get(FACING) && state.get(Properties.POWERED)) 15 else 0
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random?) {
        world.setBlockState(pos, world.getBlockState(pos).with(Properties.POWERED, isGearPoweredAtInput(world,pos)))
        super.scheduledTick(state, world, pos, random)
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

        fun isValidSide(sourceDir: Direction, motorState: BlockState): Boolean {
            return motorState.isOf(ModBlocks.ALTERNATOR) && motorState.get(FACING) == sourceDir.opposite
        }
    }
}