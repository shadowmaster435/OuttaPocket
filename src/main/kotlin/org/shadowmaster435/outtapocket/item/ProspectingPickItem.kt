package org.shadowmaster435.outtapocket.item

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.ShovelItem
import net.minecraft.registry.tag.BlockTags
import net.minecraft.screen.EnchantmentScreenHandler
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import org.shadowmaster435.outtapocket.init.ModSounds
import java.util.stream.Stream

class ProspectingPickItem(settings: Settings) : Item(settings.maxDamage(1024).maxCount(1)) {
    
    
    override fun useOnBlock(context: ItemUsageContext): ActionResult? {
        val world = context.world
        val pos = context.blockPos
        val side = context.side
        prospect(pos, world, side, context.player)
        context.stack.willBreakNextUse()
        context.stack.damage(1, context.player, context.hand)
        return ActionResult.SUCCESS
    }
    
    fun prospect(pos: BlockPos, world: World, dir: Direction, player: PlayerEntity?) {
        SoundEvents.ENTITY_ITEM_BREAK
        if (player == null) return
        val state = world.getBlockState(pos)
        val states = world.getStatesInBox(box.offset(pos.offset(dir.opposite, 2)))
        val sound = getProspectSound(state, states)
        if (sound != null) world.playSound(player, pos, sound, SoundCategory.PLAYERS)
        val soundGroup = state.soundGroup
        world.playSound(player, pos, soundGroup.breakSound, SoundCategory.PLAYERS, (soundGroup.getVolume() + 1.0f) / 2.0f, soundGroup.getPitch() * 0.8f)
    }

    fun getProspectSound(sourceState: BlockState, stateStream: Stream<BlockState?>?): SoundEvent? {
        if (stateStream == null) return null
        val states = stateStream.toList().toMutableList()
        states.add(sourceState)
        var highestPrio = blockPriority.size
        var sound: SoundEvent? = null
        for (state in states) {
            if (state == null) continue
            if (state.isOf(Blocks.ANCIENT_DEBRIS)) {
                return ModSounds.PROSPECTED_ANCIENT_DEBRIS
            }
            for (prio in 0..<blockPriority.size) {
                val entry = blockPriority[prio]
                val blocks = entry.first
                for (block in blocks) {
                    if (prio < highestPrio && state.isOf(block)) {
                        highestPrio = prio
                        sound = entry.second
                    }
                }
            }
            if (highestPrio == 0) {
                return sound
            }
        }
        return sound
    }
    companion object {
        private val box = Box(-1.0,-3.0,-3.0, 1.0, 1.0, 1.0)
        private val blockPriority = arrayOf(
            Pair(listOf(Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE), ModSounds.PROSPECTED_DIAMOND),
            Pair(listOf(Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE), ModSounds.PROSPECTED_IRON),
            Pair(listOf(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE), ModSounds.PROSPECTED_COAL),
            Pair(listOf(Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE), ModSounds.PROSPECTED_GOLD),
            Pair(listOf(Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE), ModSounds.PROSPECTED_LAPIS),
            Pair(listOf(Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE), ModSounds.PROSPECTED_REDSTONE),
            Pair(listOf(Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE), ModSounds.PROSPECTED_COPPER),
            Pair(listOf(Blocks.NETHER_QUARTZ_ORE), ModSounds.PROSPECTED_QUARTZ),
            Pair(listOf(Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE), ModSounds.PROSPECTED_EMERALD)
        )
    }
}