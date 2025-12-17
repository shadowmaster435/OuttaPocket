package org.shadowmaster435.outtapocket.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.shadowmaster435.outtapocket.init.ModBlocks
import org.shadowmaster435.outtapocket.util.LateInitBlockRef


open class BuggedBlock(settings: Settings, val original: Block, private val next: LateInitBlockRef?) : Block(settings) {

    constructor(settings: Settings, original: Block) : this(settings, original, null)
    constructor(settings: Settings, original: Block, next: Block) : this(settings, original,
        LateInitBlockRef(
        Registries.BLOCK.getKey(next).get().value,
            next
        ))


    override fun getStateManager(): StateManager<Block?, BlockState?>? {
        return super.getStateManager()
    }

    open fun getNext(world: World, pos: BlockPos): BlockState? {

        if (world.getBlockState(pos)?.isOf(original) == true) {

            return this.defaultState
        }
        return if (next != null) {
            next.get().defaultState
        } else {
            original.defaultState
        }
    }

    fun getTrueOriginal(): Block {
        var result = original
        while (result is BuggedBlock) {
            result = result.original
        }
        return result
    }

}