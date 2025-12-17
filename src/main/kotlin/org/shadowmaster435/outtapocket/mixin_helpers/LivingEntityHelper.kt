package org.shadowmaster435.outtapocket.mixin_helpers

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import org.shadowmaster435.outtapocket.entity.FlailProjectileEntity
import org.shadowmaster435.outtapocket.util.withCustomModelData

object LivingEntityHelper {

    @JvmStatic
    fun dropFlail(stack: ItemStack, entity: LivingEntity) {
        FlailProjectileEntity.flails.forEach {
            if (it.thrower === entity) {
                it.remove(Entity.RemovalReason.DISCARDED)
                stack.withCustomModelData("normal")

                if (entity is PlayerEntity) {
                    entity.itemCooldownManager.set(stack, 0)
                }
            }
        }
    }
}