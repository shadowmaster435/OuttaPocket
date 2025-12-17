package org.shadowmaster435.outtapocket.item

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.component.type.WeaponComponent
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ToolMaterial
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.shadowmaster435.outtapocket.entity.FlailProjectileEntity
import org.shadowmaster435.outtapocket.init.ModEntities
import org.shadowmaster435.outtapocket.util.withCustomModelData

class FlailItem(settings: Settings) : Item(
    settings

    .component(
            DataComponentTypes.ATTRIBUTE_MODIFIERS,
            AttributeModifiersComponent.builder().add(
                EntityAttributes.ATTACK_DAMAGE,
                EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID,5.0, EntityAttributeModifier.Operation.ADD_VALUE),
                AttributeModifierSlot.MAINHAND
            ).add(
                EntityAttributes.ATTACK_SPEED,
                EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID,-2.7, EntityAttributeModifier.Operation.ADD_VALUE),
                AttributeModifierSlot.MAINHAND
            ).build()
    )
    .enchantable(ToolMaterial.DIAMOND.enchantmentValue)
    .component(DataComponentTypes.WEAPON, WeaponComponent(1, 0f))
    .maxDamage(1024)
) {


    override fun use(world: World, user: PlayerEntity, hand: Hand?): ActionResult? {
        if (hand == Hand.OFF_HAND) return ActionResult.PASS
        if (!user.itemCooldownManager.isCoolingDown(user.mainHandStack)) {
            val flail = FlailProjectileEntity(ModEntities.FLAIL, world, user,
                user.mainHandStack.withCustomModelData("thrown")
            )
            user.swingHand(hand)
            FlailProjectileEntity.flails.add(flail)
            user.itemCooldownManager.set(user.mainHandStack, 10000)
            flail.setVelocity(user,user.pitch,user.headYaw,0f, 4f, 0f)
            flail.setPosition(user.entityPos.add(Vec3d(0.0, user.getEyeHeight(user.pose).toDouble() - 0.5,0.0)))
            world.spawnEntity(flail)
        }

        return super.use(world, user, hand)
    }

}