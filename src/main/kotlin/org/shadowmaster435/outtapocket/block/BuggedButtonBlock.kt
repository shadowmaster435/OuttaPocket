package org.shadowmaster435.outtapocket.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.WallMountedBlock
import net.minecraft.block.enums.BlockFace
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockView
import net.minecraft.world.World
import org.shadowmaster435.outtapocket.init.ModItems


class BuggedButtonBlock(settings: Settings, val type: Int, original: Block) : BuggedBlock(settings, original, original) {


    override fun onUseWithItem(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand?,
        hit: BlockHitResult
    ): ActionResult  {
        return if (player.getStackInHand(hand).item == ModItems.BUG_STICK) ActionResult.PASS else use(state,world,pos,player)
    }

    override fun getNext(world: World, pos: BlockPos): BlockState? {
        if (world.getBlockState(pos)?.isOf(original) == true) {
            return this.defaultState
        }
        for(dir in Direction.entries) {
            if (WallMountedBlock.canPlaceAt(world,pos,dir)) {
                return if (dir.axis.isVertical) {
                    original.defaultState.with(WallMountedBlock.FACE, if (dir == Direction.UP) BlockFace.CEILING else BlockFace.FLOOR)
                } else {
                    original.defaultState.with(WallMountedBlock.FACING, dir.opposite).with(WallMountedBlock.FACE, BlockFace.WALL)
                }
            }
        }
        return null
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        return use(state,world,pos,player)
    }

    init {
        defaultState = stateManager.defaultState.with(Properties.POWERED, false)
    }

    fun use(blockState: BlockState, world: World, pos: BlockPos, player: PlayerEntity) : ActionResult {
        val shouldPass = player.isSneaking || blockState.get(Properties.POWERED)
        if (!shouldPass) {
            world.scheduleBlockTick(pos, blockState.block, if (type != 0) 30 else 20)
            world.playSound(
                player,
                pos,
                when(type) {
                    1 -> SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON
                    2 -> SoundEvents.BLOCK_NETHER_WOOD_BUTTON_CLICK_ON
                    3 -> SoundEvents.BLOCK_CHERRY_WOOD_BUTTON_CLICK_ON
                    4 -> SoundEvents.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON
                    else -> SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON
                },
                SoundCategory.BLOCKS
            )
            world.setBlockState(pos, blockState.with(Properties.POWERED, true))
        }
        return if (shouldPass) ActionResult.PASS else ActionResult.SUCCESS
    }

    override fun getWeakRedstonePower(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        direction: Direction?
    ): Int {
        return if (state.get(Properties.POWERED)) 15 else 0
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        super.appendProperties(builder.add(Properties.POWERED))
    }
    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random?) {
        if (state.get(Properties.POWERED)) {
            world.setBlockState(pos, state.with(Properties.POWERED, false))
            world.playSound(
                null,
                pos,
                when(type) {
                    1 -> SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF
                    2 -> SoundEvents.BLOCK_NETHER_WOOD_BUTTON_CLICK_OFF
                    3 -> SoundEvents.BLOCK_CHERRY_WOOD_BUTTON_CLICK_OFF
                    4 -> SoundEvents.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF
                    else -> SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF
                },
                SoundCategory.BLOCKS
            )
        }
        super.scheduledTick(state, world, pos, random)
    }

}