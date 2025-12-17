package org.shadowmaster435.outtapocket.block

import net.minecraft.block.*
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.WorldView
import org.shadowmaster435.outtapocket.block.GearBlock.Companion.TORQUE
import org.shadowmaster435.outtapocket.init.ModBlocks

abstract class MechanicalBlock(settings: Settings) : FacingBlock(settings) {

    fun isRedstonePoweredAtInput(world: WorldView, pos: BlockPos): Boolean {
        val dir = world.getBlockState(pos).get(FACING)
        return world.isEmittingRedstonePower(pos.offset(dir.opposite), dir)
    }

    fun isGearPoweredAtInput(world: WorldView, pos: BlockPos): Boolean {
        if (!isGearAtShaft(world, pos)) return false
        val dir = world.getBlockState(pos).get(FACING)
        return world.getBlockState(pos.offset(dir)).get(TORQUE) > 0
    }

    fun isGearAtShaft(world: WorldView, pos: BlockPos): Boolean {
        return world.getBlockState(pos.offset(world.getBlockState(pos).get(FACING))).isOf(ModBlocks.GEAR)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder.add(FACING, Properties.POWERED))
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return this.defaultState.with(FACING, if (ctx.player?.isSneaking == true) ctx.playerLookDirection else ctx.playerLookDirection.opposite)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState? {
        return state.with(FACING, rotation.rotate(state.get(FACING)))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState? {
        return state.rotate(mirror.getRotation(state.get(FACING)))
    }
    companion object {
        val shape: Map<Direction, VoxelShape> = VoxelShapes.createFacingShapeMap(
            VoxelShapes.union(
                VoxelShapes.cuboid(0.0, 0.0, 0.125, 1.0, 1.0, 1.0),
                VoxelShapes.cuboid(0.375, 0.375, 0.0, 0.625, 0.625, 0.125)
            )
        )
    }

}
