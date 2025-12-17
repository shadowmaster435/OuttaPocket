package org.shadowmaster435.outtapocket.item

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ButtonBlock
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.LoreComponent
import net.minecraft.component.type.TooltipDisplayComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import org.shadowmaster435.outtapocket.block.BuggedBlock
import org.shadowmaster435.outtapocket.init.ModBlocks

class BugStickItem(settings: Settings) : Item(settings.component(
    DataComponentTypes.LORE, LoreComponent(listOf(
        Text.translatable("item.outta_pocket.bug_stick.flavor_text_0"),
        Text.translatable("item.outta_pocket.bug_stick.flavor_text_1")

    ))
)) {
    fun BlockState.isOf(blocks: Collection<Block>): BlockState? {
        for (block in blocks) {
            if (this.isOf(block)) return this
        }
        return null
    }


    override fun isUsedOnRelease(stack: ItemStack?): Boolean {
        return false
    }



    override fun useOnBlock(context: ItemUsageContext): ActionResult? {
        val pos = context.blockPos
        val world = context.world
        val state = world.getBlockState(pos)
        var block: BuggedBlock? = null
        for (b in ModBlocks.BUGGED_BLOCKS) {
            if (state.isOf(b.getTrueOriginal()) || state.isOf(b)) {
                block = b
                break
            }
        }
        if (block != null) {
            val next = block.getNext(world, pos)
            if (next != null) {
                world.setBlockState(pos, next)
            }
            else {
                Block.dropStacks(block.original.defaultState, world, pos)
                world.setBlockState(pos, Blocks.AIR.defaultState)
            }
        }
        return ActionResult.SUCCESS
    }

}