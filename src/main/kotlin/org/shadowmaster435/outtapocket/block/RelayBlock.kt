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
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import org.shadowmaster435.outtapocket.init.ModBlocks

class RelayBlock(settings: Settings) : MechanicalBlock(settings) {




    override fun getCodec(): MapCodec<out RelayBlock> {
        return createCodec<RelayBlock> { settings: Settings -> RelayBlock(settings) }
    }

    init {
        defaultState = stateManager.defaultState.with(Properties.POWERED, false)
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
        val shape: Map<Direction, VoxelShape> = VoxelShapes.createFacingShapeMap(
            VoxelShapes.union(
                VoxelShapes.cuboid(0.0, 0.0, 0.125, 1.0, 1.0, 0.875),
                VoxelShapes.cuboid(0.375, 0.375, 0.0, 0.625, 0.625, 0.125),
                VoxelShapes.cuboid(0.375, 0.375, 0.875, 0.625, 0.625, 1.0)
            )
        )
        fun shouldPowerGear(sourceDir: Direction, motorState: BlockState): Boolean {
            return isValidPowerSide(sourceDir, motorState) && motorState.get(Properties.POWERED)
        }
        fun isValidPowerSide(sourceDir: Direction, motorState: BlockState): Boolean {
            return motorState.isOf(ModBlocks.RELAY) &&  motorState.get(FACING) == sourceDir
        }
        fun isValidSide(sourceDir: Direction, motorState: BlockState): Boolean {
            return motorState.isOf(ModBlocks.RELAY) && motorState.get(FACING).axis == sourceDir.axis
        }
    }
}