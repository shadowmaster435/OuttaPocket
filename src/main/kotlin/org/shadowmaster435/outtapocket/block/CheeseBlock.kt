package org.shadowmaster435.outtapocket.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class CheeseBlock(settings: Settings) : Block(settings) {

    init {
        defaultState = stateManager.defaultState.with(WEDGES, 255)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult? {
        if (player.mainHandStack.isEmpty) {
            val hovered = getHoveredWedge(hit)
            if (hovered > -1) {
                player.hungerManager.add(1, 0.2f)
                spawnBreakParticles(world,player,pos,state.with(WEDGES, 1 shl hovered))
                world.playSound(player, pos, SoundEvents.ENTITY_GENERIC_EAT.value(), SoundCategory.PLAYERS)
                val prevWedges = state.get(WEDGES)
                val newWedges = prevWedges and (1 shl hovered).inv()
                world.setBlockState(pos, if (newWedges > 0) state.with(WEDGES, newWedges) else Blocks.AIR.defaultState)
            }
        }
        return super.onUse(state, world, pos, player, hit)
    }

    fun getHoveredWedge(hit: BlockHitResult): Int {
        if (hit.type != HitResult.Type.MISS) {
            val blockPos = hit.blockPos
            val hitPos = hit.pos.add(Vec3d(hit.side.opposite.vector).multiply(0.01))
            return if (hitPos.y > blockPos.y + 0.5) {
                 if (hitPos.x > blockPos.x + 0.5) {
                    if (hitPos.z > blockPos.z + 0.5) {
                        7 // top east south
                    } else {
                        6 // top west south
                    }
                } else {
                    if (hitPos.z > blockPos.z + 0.5) {
                        5 // top east north
                    } else {
                        4 // top west north
                    }
                }
            } else {
                return if (hitPos.x > blockPos.x + 0.5) {
                    if (hitPos.z > blockPos.z + 0.5) {
                        3 // bottom east south
                    } else {
                        2 // bottom west south
                    }
                } else {
                    if (hitPos.z > blockPos.z + 0.5) {
                        1 // bottom east north
                    } else {
                        0 // bottom west north
                    }
                }
            }
        }
        return -1
    }

    val shapes = arrayOf(
        VoxelShapes.cuboid(0.0,0.0,0.0,0.5,0.5,0.5),
        VoxelShapes.cuboid(0.0,0.0,0.5,0.5,0.5,1.0),
        VoxelShapes.cuboid(0.5,0.0,0.0,1.0,0.5,0.5),
        VoxelShapes.cuboid(0.5,0.0,0.5,1.0,0.5,1.0),
        VoxelShapes.cuboid(0.0,0.5,0.0,0.5,1.0,0.5),
        VoxelShapes.cuboid(0.0,0.5,0.5,0.5,1.0,1.0),
        VoxelShapes.cuboid(0.5,0.5,0.0,1.0,1.0,0.5),
        VoxelShapes.cuboid(0.5,0.5,0.5,1.0,1.0,1.0),
    )
    private val emtpy = VoxelShapes.empty()
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext?
    ): VoxelShape? {


        val wedges = state.get(WEDGES)
        val tes = if ((wedges and 1) != 0) shapes[0] else emtpy
        val tws = if (((wedges shr 1) and 1) != 0) shapes[1] else emtpy
        val ten = if (((wedges shr 2) and 1) != 0) shapes[2] else emtpy
        val twn = if (((wedges shr 3) and 1) != 0) shapes[3] else emtpy
        val bes = if (((wedges shr 4) and 1) != 0) shapes[4] else emtpy
        val bws = if (((wedges shr 5) and 1) != 0) shapes[5] else emtpy
        val ben = if (((wedges shr 6) and 1) != 0) shapes[6] else emtpy
        val bwn = if (((wedges shr 7) and 1) != 0) shapes[7] else emtpy
        return VoxelShapes.union(tes, tws, ten, twn, bes, bws, ben, bwn)
    }
    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        super.appendProperties(builder.add(WEDGES))
    }
    companion object {
        val WEDGES: IntProperty = IntProperty.of("wedges",0,255)
    }


}
