package org.shadowmaster435.outtapocket.util

import net.minecraft.block.Block
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier


data class LateInitBlockRef(val key: Identifier, private var block: Block? = null) {
    fun get(): Block {
        if (block == null) {
            block = Registries.BLOCK.get(key)
        }
        return block!!
    }
}