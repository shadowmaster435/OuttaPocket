package org.shadowmaster435.outtapocket.item

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.CampfireBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.ToolMaterial
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldEvents
import net.minecraft.world.event.GameEvent
import org.shadowmaster435.outtapocket.init.ModTags
import org.shadowmaster435.outtapocket.mixin.ShovelItemAccessor

class DiamondTipShovelItem(settings: Settings) : Item(settings.tool(ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 1024, 7.0f, 3.0f, 14, ItemTags.WOODEN_TOOL_MATERIALS), ModTags.SHOVEL_PICKAXE,1.5F, -3.0f, 0.0f)) {

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val blockPos = context.blockPos
        val blockState = world.getBlockState(blockPos)
        if (context.side == Direction.DOWN) {
            return ActionResult.PASS
        } else {
            val playerEntity = context.player
            val blockState2 = ShovelItemAccessor.getPathStates()[blockState.block]
            var blockState3: BlockState? = null
            if (blockState2 != null && world.getBlockState(blockPos.up()).isAir) {
                world.playSound(
                    playerEntity,
                    blockPos,
                    SoundEvents.ITEM_SHOVEL_FLATTEN,
                    SoundCategory.BLOCKS,
                    1.0f,
                    1.0f
                )
                blockState3 = blockState2
            } else if (blockState.block is CampfireBlock && blockState.get(CampfireBlock.LIT)) {
                if (!world.isClient) {
                    world.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, blockPos, 0)
                }

                CampfireBlock.extinguish(context.player, world, blockPos, blockState)
                blockState3 = blockState.with(CampfireBlock.LIT, false)
            }

            if (blockState3 != null) {
                if (!world.isClient) {
                    world.setBlockState(blockPos, blockState3, Block.NOTIFY_ALL_AND_REDRAW)
                    world.emitGameEvent(
                        GameEvent.BLOCK_CHANGE,
                        blockPos,
                        GameEvent.Emitter.of(playerEntity, blockState3)
                    )
                    if (playerEntity != null) {
                        context.stack.damage(1, playerEntity, context.hand.equipmentSlot)
                    }
                }
                return ActionResult.SUCCESS
            } else {
                return ActionResult.PASS
            }
        }
    }
}