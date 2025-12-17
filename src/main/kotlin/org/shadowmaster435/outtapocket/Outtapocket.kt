package org.shadowmaster435.outtapocket

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.minecraft.block.SlimeBlock
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.loot.LootTables
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.shadowmaster435.outtapocket.init.ModBlocks
import org.shadowmaster435.outtapocket.init.ModItems
import org.shadowmaster435.outtapocket.init.ModLootTableModifiers

fun ItemGroup.Entries.add(items: Collection<ItemConvertible>) {
    items.forEach {
        this.add(it)
    }
}

fun ItemGroup.Entries.add(vararg items: ItemConvertible) {
    items.forEach {
        this.add(it)
    }
}

class Outtapocket : ModInitializer {
    val GROUP: ItemGroup? = FabricItemGroup.builder()
        .icon { ItemStack(ModBlocks.NETHER_REACTOR_CORE) }
        .displayName(Text.translatable("itemGroup.outta_pocket.tab"))
        .entries { _: ItemGroup.DisplayContext?, entries: ItemGroup.Entries ->
            entries.add(ModBlocks.NETHER_REACTOR_CORE)
            entries.add(ModBlocks.STONECUTTER)
            entries.add(ModBlocks.GLOWING_OBSIDIAN)
            entries.add(ModBlocks.CYAN_ROSE)
            entries.add(ModBlocks.RED_ROSE)
            entries.add(ModBlocks.GEAR)
            entries.add(ModBlocks.ALTERNATOR)
            entries.add(ModBlocks.MOTOR)
            entries.add(ModBlocks.RELAY)
            entries.add(ModBlocks.SIX_SIDED_HAY_BLOCK)
            entries.add(ModBlocks.BUGGED_SIX_SIDED_HAY_BLOCK)
            entries.add(ModBlocks.SIX_SIDED_PISTON)
            entries.add(ModBlocks.BUGGED_SIX_SIDED_PISTON)
            entries.add(ModBlocks.SIX_SIDED_STICKY_PISTON)
            entries.add(ModBlocks.CHEESE)
            entries.add(ModItems.BUG_STICK)
            entries.add(ModItems.DIAMOND_TIP_SHOVEL)
            entries.add(ModItems.PROSPECTING_PICK)
            entries.add(ModItems.FLAIL)
            entries.add(ModItems.PIKE)
            entries.add(ModItems.ICE_AXE)
            entries.add(ModBlocks.BUGGED_BUTTONS.values)

        }
        .build()
    override fun onInitialize() {
        Items.DIAMOND_SWORD
        ModBlocks.init()
        ModItems.init()
        ModLootTableModifiers.init()
        Registry.register(Registries.ITEM_GROUP, Identifier.of(MOD_ID, "outta_pocket"), GROUP);
    }

    companion object {
        const val MOD_ID = "outta_pocket"
    }
}
