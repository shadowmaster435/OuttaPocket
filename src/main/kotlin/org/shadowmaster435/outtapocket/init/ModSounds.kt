package org.shadowmaster435.outtapocket.init

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import org.shadowmaster435.outtapocket.Outtapocket

object ModSounds {

    val PROSPECTED_COAL = register("prospected_coal")
    val PROSPECTED_GOLD = register("prospected_gold")
    val PROSPECTED_COPPER = register("prospected_copper")
    val PROSPECTED_IRON = register("prospected_iron")
    val PROSPECTED_QUARTZ = register("prospected_quartz")
    val PROSPECTED_ANCIENT_DEBRIS = register("prospected_ancient_debris")
    val PROSPECTED_DIAMOND = register("prospected_diamond")
    val PROSPECTED_LAPIS = register("prospected_lapis")
    val PROSPECTED_REDSTONE = register("prospected_redstone")
    val PROSPECTED_EMERALD = register("prospected_emerald")

    private fun register(name: String): SoundEvent {
        val id = Identifier.of(Outtapocket.MOD_ID, name)
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id))
    }

}