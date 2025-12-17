package org.shadowmaster435.outtapocket.item

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.component.type.WeaponComponent
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.Item
import net.minecraft.item.ToolMaterial
import net.minecraft.util.Identifier

class PikeItem(settings: Settings) : Item(
    settings.component(
        DataComponentTypes.ATTRIBUTE_MODIFIERS,
        AttributeModifiersComponent.builder().add(
            EntityAttributes.ENTITY_INTERACTION_RANGE,
            EntityAttributeModifier(Identifier.ofVanilla("entity_interaction_range"),3.0, EntityAttributeModifier.Operation.ADD_VALUE),
            AttributeModifierSlot.MAINHAND
        ).add(
            EntityAttributes.ATTACK_DAMAGE,
            EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID,5.5, EntityAttributeModifier.Operation.ADD_VALUE),
            AttributeModifierSlot.MAINHAND
        ).add(
            EntityAttributes.ATTACK_SPEED,
            EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID,-2.7, EntityAttributeModifier.Operation.ADD_VALUE),
            AttributeModifierSlot.MAINHAND
        ).build()
    )
        .enchantable(ToolMaterial.DIAMOND.enchantmentValue)
        .component(DataComponentTypes.WEAPON, WeaponComponent(1, 0f))
        .maxDamage(ToolMaterial.DIAMOND.durability)
)