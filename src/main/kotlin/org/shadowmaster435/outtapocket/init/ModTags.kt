package org.shadowmaster435.outtapocket.init

import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier


object ModTags {

    val SHOVEL_PICKAXE: TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, Identifier.of("outta_pocket", "mineable/shovel_pickaxe"))
}