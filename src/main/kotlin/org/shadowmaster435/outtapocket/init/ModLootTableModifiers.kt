package org.shadowmaster435.outtapocket.init

import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.minecraft.item.Item
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.LootTables
import net.minecraft.loot.condition.RandomChanceLootCondition
import net.minecraft.loot.entry.GroupEntry
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.entry.LootPoolEntry
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.registry.RegistryKey


object ModLootTableModifiers {
    fun LootPool.Builder.chance(chance: Float): LootPool.Builder = this.rolls(ConstantLootNumberProvider.create(1f)).conditionally(RandomChanceLootCondition.builder(chance))
    fun LootPool.Builder.item(item: Item): LootPool.Builder = this.with(ItemEntry.builder(item))
    fun LootPool.Builder.items(vararg items: Item): LootPool.Builder = this.with(GroupEntry.create(
        *run {
            val lst = mutableListOf<LootPoolEntry.Builder<*>>()
            for (item in items) {
                lst.add(ItemEntry.builder(item))
            }
            lst.toTypedArray()
        }
    ))
    fun LootPool.Builder.roll(times: Int): LootPool.Builder = this.rolls(ConstantLootNumberProvider.create(times.toFloat()))
    fun init() {
        LootTableEvents.MODIFY.register { key, tableBuilder, source, _ ->
            if (source.isBuiltin) {
                val poolBuilder: LootPool.Builder = LootPool.builder()
                when(key) {
                    LootTables.BURIED_TREASURE_CHEST -> poolBuilder.chance(0.8f).item(ModItems.PIKE)
                    LootTables.ABANDONED_MINESHAFT_CHEST -> poolBuilder.chance(0.5f).item(ModItems.PROSPECTING_PICK)
                    LootTables.DESERT_PYRAMID_CHEST -> poolBuilder.roll(1).item(ModItems.DIAMOND_TIP_SHOVEL)
                    LootTables.JUNGLE_TEMPLE_CHEST -> poolBuilder.chance(0.6f).item(ModItems.BUG_STICK)
                    LootTables.IGLOO_CHEST_CHEST -> poolBuilder.roll(1).item(ModItems.ICE_AXE)
                    LootTables.SIMPLE_DUNGEON_CHEST -> poolBuilder.chance(0.5f).item(ModItems.FLAIL)
                }
                tableBuilder.pool(poolBuilder)
            }
        }
    }
}