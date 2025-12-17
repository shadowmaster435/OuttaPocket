package org.shadowmaster435.outtapocket.init

import net.minecraft.item.Item
import net.minecraft.item.ShovelItem
import net.minecraft.item.ToolMaterial
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.Identifier
import org.shadowmaster435.outtapocket.Outtapocket
import org.shadowmaster435.outtapocket.item.BugStickItem
import org.shadowmaster435.outtapocket.item.DiamondTipShovelItem
import org.shadowmaster435.outtapocket.item.FlailItem
import org.shadowmaster435.outtapocket.item.IceAxeItem
import org.shadowmaster435.outtapocket.item.PikeItem
import org.shadowmaster435.outtapocket.item.ProspectingPickItem
import java.util.function.Function

object ModItems {
    val FLAIL: FlailItem = register("flail", { settings: Item.Settings -> FlailItem(settings) }, Item.Settings())

    @JvmField
    val ICE_AXE: IceAxeItem = register("ice_axe", { settings: Item.Settings -> IceAxeItem(settings) }, Item.Settings())

    val BUG_STICK: BugStickItem = register("bug_stick", { settings: Item.Settings -> BugStickItem(settings) }, Item.Settings())
    val DIAMOND_TIP_SHOVEL: DiamondTipShovelItem = register("diamond_tip_shovel",
        {
            settings: Item.Settings -> DiamondTipShovelItem(settings)
        },
        Item.Settings())
    val PROSPECTING_PICK: ProspectingPickItem = register("prospecting_pick", { settings: Item.Settings -> ProspectingPickItem(settings) }, Item.Settings())
    val PIKE: PikeItem = register("pike", { settings: Item.Settings -> PikeItem(settings) }, Item.Settings())

    fun <I : Item> register(name: String, itemFactory: Function<Item.Settings, I>, settings: Item.Settings): I {
        val itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Outtapocket.MOD_ID, name))
        val item = itemFactory.apply(settings.registryKey(itemKey))
        Registry.register<Item, I>(Registries.ITEM, itemKey, item)
        return item
    }
    fun init() {

    }
}