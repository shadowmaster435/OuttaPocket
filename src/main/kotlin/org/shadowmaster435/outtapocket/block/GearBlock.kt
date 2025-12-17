package org.shadowmaster435.outtapocket.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.RedstoneWireBlock
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockRenderView
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import net.minecraft.world.block.WireOrientation
import net.minecraft.world.tick.ScheduledTickView
import org.shadowmaster435.outtapocket.init.ModBlocks
import kotlin.math.max



class GearBlock(settings: Settings) : Block(settings) {

    init {
        defaultState = stateManager.defaultState.with(TORQUE, 1)
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState>) {
        super.appendProperties(builder.add(TORQUE))
    }
    fun BlockState.isOf(vararg blocks: Block): Boolean {
        for (block in blocks) {
            if (this.isOf(block)) return true
        }
        return false
    }


    override fun afterBreak(
        world: World,
        player: PlayerEntity,
        pos: BlockPos,
        state: BlockState,
        blockEntity: BlockEntity?,
        tool: ItemStack?
    ) {
        for (dir in Direction.entries) {
            val ofsPos = pos.add(dir.vector)
            val ofsState = world.getBlockState(ofsPos)
            if ((ofsState.isOf(this))) continue
            for (ofs in cornerMap[dir]!!) {
                val cornerState = world.getBlockState(pos.add(ofs))
                if (!cornerState.isOf(this)) continue
                world.scheduleBlockTick(pos.add(ofs),cornerState.block,1)
            }
        }
        super.afterBreak(world, player, pos, state, blockEntity, tool)
    }
    override fun neighborUpdate(
        state: BlockState,
        world: World,
        pos: BlockPos,
        sourceBlock: Block,
        wireOrientation: WireOrientation?,
        notify: Boolean
    ) {
        world.setBlockState(pos,state.with(TORQUE, getHighestTorqueAt(world, pos)))
        for (dir in Direction.entries) {
            val ofsPos = pos.offset(dir)
            val ofsState = world.getBlockState(ofsPos)
            if (ofsState.isOf(ModBlocks.ALTERNATOR, ModBlocks.RELAY)) {
                world.scheduleBlockTick(ofsPos, ofsState.block,1)
            }
        }
    }

    override fun prepare(state: BlockState, world: WorldAccess, pos: BlockPos, flags: Int, maxUpdateDepth: Int) {
        for (dir in Direction.entries) {
            val ofsPos = pos.add(dir.vector)
            val ofsState = world.getBlockState(ofsPos)
            if ((ofsState.isOf(this))) continue
            for (ofs in cornerMap[dir]!!) {
                val cornerState = world.getBlockState(pos.add(ofs))
                if (!cornerState.isOf(this)) continue
                world.replaceWithStateForNeighborUpdate(
                    dir.opposite,ofsPos,ofsPos.add(ofs),cornerState,flags,maxUpdateDepth
                )
            }
        }

        super.prepare(state, world, pos, flags, maxUpdateDepth)
    }


    private fun updateNeighbors(world: World, pos: BlockPos) {
        if (world.getBlockState(pos).isOf(this)) {
            world.updateNeighbors(pos, this)

            for (direction in Direction.entries) {
                val ofs = pos.offset(direction)
                if (world.getBlockState(ofs).isOf(this)) continue
                world.updateNeighbors(ofs, this)
            }
        }
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        world: WorldView,
        tickView: ScheduledTickView?,
        pos: BlockPos,
        direction: Direction,
        neighborPos: BlockPos,
        neighborState: BlockState,
        random: Random?
    ): BlockState {

        return if (hasValidSide(world,pos)) getState(world, pos) else Blocks.AIR.defaultState
    }

    override fun onBlockAdded(
        state: BlockState?,
        world: World,
        pos: BlockPos,
        oldState: BlockState?,
        notify: Boolean
    ) {
        updateNeighbors(world,pos)
    }

    override fun onBroken(world: WorldAccess, pos: BlockPos, state: BlockState?) {
        updateNeighbors(world as World,pos)
        super.onBroken(world, pos, state)
    }


    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext?
    ): VoxelShape? {
        val validDirs = getValidSides(world,pos)

        var finalShape = VoxelShapes.empty()
        for (dir in validDirs) {
            finalShape = VoxelShapes.union(finalShape, shape[dir])
        }
        return finalShape
    }
    override fun onStateReplaced(state: BlockState?, world: ServerWorld, pos: BlockPos, moved: Boolean) {
        updateNeighbors(world,pos)
        val newState = world.getBlockState(pos)
        if (newState.isAir) {
            for (dir in Direction.entries) {
                val ofsPos = pos.add(dir.vector)
                val ofsState = world.getBlockState(ofsPos)
                if ((ofsState.isOf(this))) continue
                for (ofs in cornerMap[dir]!!) {
                    val cornerState = world.getBlockState(pos.add(ofs))
                    if (!cornerState.isOf(this)) continue
                    world.updateNeighbor(pos.add(ofs), this,null)
                }
            }
        }
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return getState(ctx.world, ctx.blockPos)
    }

    fun getState(world: WorldView, blockPos: BlockPos): BlockState {
        return ModBlocks.GEAR.defaultState.with(TORQUE, getHighestTorqueAt(world, blockPos))
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        return hasValidSide(world,pos)
    }

    companion object {
        val shape = VoxelShapes.createFacingShapeMap(
            VoxelShapes.cuboid(0.0,0.0,0.0,1.0,1.0,0.05)
        )

        val TORQUE: IntProperty = IntProperty.of("torque", 0, 31)
        fun hasValidSide(blockView: WorldView, pos: BlockPos): Boolean {
            for (dir in Direction.entries) {
                val state = blockView.getBlockState(pos.offset(dir))
                if (!(state.isAir || state.isOf(ModBlocks.GEAR)) && state.isSideSolidFullSquare(
                        blockView, pos.offset(dir), dir.opposite
                    )) return true
            }
            return false
        }

        fun getValidSides(blockView: BlockView, pos: BlockPos): MutableList<Direction> {
            val list = mutableListOf<Direction>()
            for (dir in Direction.entries) {
                val state = blockView.getBlockState(pos.offset(dir))
                if (!(state.isAir || state.isOf(ModBlocks.GEAR)) && state.isSideSolidFullSquare(blockView, pos.offset(dir), dir.opposite)) list.add(dir)
            }
            return list
        }
        private val cornerMap: Map<Direction, MutableList<BlockPos>> = hashMapOf( // possibly bad but i was burnt out when i made this
            Pair(Direction.UP, mutableListOf(
                BlockPos(1,1,0),
                BlockPos(-1,1,0),
                BlockPos(0,1,1),
                BlockPos(0,1,-1)
            )),
            Pair(Direction.DOWN, mutableListOf(
                BlockPos(1,-1,0),
                BlockPos(-1,-1,0),
                BlockPos(0,-1,1),
                BlockPos(0,-1,-1)
            )),
            Pair(Direction.NORTH, mutableListOf(
                BlockPos(0,1,-1),
                BlockPos(0,-1,-1),
                BlockPos(1,0,-1),
                BlockPos(-1,0,-1)
            )),
            Pair(Direction.SOUTH, mutableListOf(
                BlockPos(0,1,1),
                BlockPos(0,-1,1),
                BlockPos(1,0,1),
                BlockPos(-1,0,1)
            )),
            Pair(Direction.EAST, mutableListOf(
                BlockPos(1,1,0),
                BlockPos(1,-1,0),
                BlockPos(1,0,1),
                BlockPos(1,0,-1)
            )),
            Pair(Direction.WEST, mutableListOf(
                BlockPos(-1,1,0),
                BlockPos(-1,-1,0),
                BlockPos(-1,0,1),
                BlockPos(-1,0,-1)
            ))
        )

        private val faceMap: Map<Direction, MutableList<BlockPos>> = hashMapOf( // i know this is terrible i was burnt out when i made this
            Pair(Direction.UP, mutableListOf(
                BlockPos(1,1,0),
                BlockPos(-1,1,0),
                BlockPos(0,1,1),
                BlockPos(0,1,-1),
                BlockPos(1,0,0),
                BlockPos(-1,0,0),
                BlockPos(0,0,1),
                BlockPos(0,0,-1)
            )),
            Pair(Direction.DOWN, mutableListOf(
                BlockPos(1,-1,0),
                BlockPos(-1,-1,0),
                BlockPos(0,-1,1),
                BlockPos(0,-1,-1),
                BlockPos(1,0,0),
                BlockPos(-1,0,0),
                BlockPos(0,0,1),
                BlockPos(0,0,-1),
            )),
            Pair(Direction.NORTH, mutableListOf(
                BlockPos(0,1,-1),
                BlockPos(0,-1,-1),
                BlockPos(1,0,-1),
                BlockPos(-1,0,-1),
                BlockPos(1,-1,0),
                BlockPos(-1,1,0),
                BlockPos(1,1,0),
                BlockPos(-1,-1,0),
            )),
            Pair(Direction.SOUTH, mutableListOf(
                BlockPos(0,1,1),
                BlockPos(0,-1,1),
                BlockPos(1,0,1),
                BlockPos(-1,0,1),
                BlockPos(1,-1,0),
                BlockPos(-1,1,0),
                BlockPos(1,1,0),
                BlockPos(-1,-1,0),
            )),
            Pair(Direction.EAST, mutableListOf(
                BlockPos(1,1,0),
                BlockPos(1,-1,0),
                BlockPos(1,0,1),
                BlockPos(1,0,-1),
                BlockPos(0,1,0),
                BlockPos(0,-1,0),
                BlockPos(0,0,1),
                BlockPos(0,0,-1),
            )),
            Pair(Direction.WEST, mutableListOf(
                BlockPos(-1,1,0),
                BlockPos(-1,-1,0),
                BlockPos(-1,0,1),
                BlockPos(-1,0,-1),
                BlockPos(0,1,0),
                BlockPos(0,-1,0),
                BlockPos(0,0,1),
                BlockPos(0,0,-1),
            ))
        )
        fun getHighestTorqueAt(world: WorldView, pos: BlockPos): Int {
            var result = 0
            for (dir in Direction.entries) {
                if (
                    MotorBlock.shouldPowerGear(dir,world.getBlockState(pos.add(dir.vector))) ||
                    RelayBlock.shouldPowerGear(dir,world.getBlockState(pos.add(dir.vector)))
                ) return 31

                for (ofs in faceMap[dir]!!) {
                    val state = world.getBlockState(pos.add(ofs))
                    if (!state.isOf(ModBlocks.GEAR)) continue
                    result = max(result, state.get(TORQUE))
                }
            }
            return max(result - 1, 0)
        }
    }

}
