package org.shadowmaster435.outtapocket.init

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import org.shadowmaster435.outtapocket.Outtapocket
import org.shadowmaster435.outtapocket.entity.FlailProjectileEntity

object ModEntities {
    val FLAIL: EntityType<FlailProjectileEntity> = Registry.register(
        Registries.ENTITY_TYPE, Identifier.of(Outtapocket.MOD_ID, "flail"),
        EntityType.Builder.create(
            { type, world -> FlailProjectileEntity(type, world) }, SpawnGroup.CREATURE)
            .dimensions(0.25f, 0.25f).build(keyOfEntity("flail"))
    )

    fun keyOfEntity(name: String): RegistryKey<EntityType<*>> {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Outtapocket.MOD_ID, name))
    }

    fun init() {


    }
}