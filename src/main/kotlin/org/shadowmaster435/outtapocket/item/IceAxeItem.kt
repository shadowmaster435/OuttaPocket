package org.shadowmaster435.outtapocket.item

import net.minecraft.block.BlockState
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.NbtComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import org.shadowmaster435.outtapocket.util.MathUtil
import org.shadowmaster435.outtapocket.util.playBreakSound
import org.shadowmaster435.outtapocket.util.raycast

class IceAxeItem(settings: Settings) : Item(settings.maxCount(1)) {

    companion object {
        @JvmStatic
        fun update(entity: LivingEntity) {

            if (entity is PlayerEntity) {
                val state = getNeighborState(entity)
                val component = entity[DataComponentTypes.CUSTOM_DATA] ?: NbtComponent.of(NbtCompound())
                val prevVel = component.copyNbt().getDouble("hook_last_vel", entity.velocity.y)
                if (prevVel > 0.25 && !entity.isOnGround && state.second != null) {
                    entity.swingHand(Hand.OFF_HAND)
                    state.second!!.soundGroup.playBreakSound(entity, SoundCategory.PLAYERS)
                }
                if (entity.velocity.y < 0 && !entity.isOnGround && state.second != null) {
                    val dir = state.first
                    val hit = dir.raycast(0.75, entity)
                    if (prevVel < -0.01) updateHookEffect(entity, state, hit)

                    if (hit.type == HitResult.Type.BLOCK) {

                        val yVel = prevVel * 0.85
                        entity.velocity = if (entity.isSneaking) {
                            entity.velocity.withAxis(Direction.Axis.Y, -0.15)
                        } else entity.velocity.withAxis(Direction.Axis.Y, yVel)
                        entity.fallDistance = 0.0
                        if (prevVel > -0.01) {
                            entity.isOnGround = true
                            entity.isJumping = false
                        }

                    }
                }
                updateHookState(entity)


            }

        }

        fun updateHookState(entity: Entity) {
            val component = entity[DataComponentTypes.CUSTOM_DATA] ?: NbtComponent.of(NbtCompound())
            val nbt = component.copyNbt()
            val cooldown = nbt.getInt("hook_cooldown", 0)
            nbt.putDouble("hook_last_vel", entity.velocity.y)
            if (cooldown > 0) nbt.putInt("hook_cooldown", cooldown - 1)
            else nbt.putInt(
                "hook_cooldown",
                if (entity.isSneaking) 6
                else if (entity.velocity.y < -.3) 0
                else if (entity.velocity.y < -.2) 1
                else if (entity.velocity.y < -.15) 2
                else if (entity.velocity.y < -.1) 3
                else 6
            )
            entity.setComponent(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt))
        }

        fun updateHookEffect(entity: Entity, statePair: Pair<Direction, BlockState?>, hit: HitResult) {
            val state = statePair.second ?: return
            val component = (entity[DataComponentTypes.CUSTOM_DATA] ?: NbtComponent.of(NbtCompound()))
            val nbt = component.copyNbt()
            if (nbt.getInt("hook_cooldown", 1) == 0 && nbt.getDouble("hook_last_vel", entity.velocity.y) < 0.02) {
                val world = entity.entityWorld
                val ofs = hit.pos.add(Vec3d(statePair.first.opposite.vector).multiply(0.05))
                if (hit.type != HitResult.Type.BLOCK) return
                (1..Random.createLocal().nextBetween(1, 6)).forEach { _ ->
                    val randVel = MathUtil.randomVec3d(entity.velocity.y)
                    world.addParticleClient(
                        BlockStateParticleEffect(ParticleTypes.BLOCK, state),
                        ofs.x,
                        ofs.y,
                        ofs.z,
                        randVel.x,
                        randVel.y,
                        randVel.z
                    )
                }
                state.soundGroup.playBreakSound(entity, SoundCategory.PLAYERS)
            }

        }

        fun getNeighborState(entity: Entity): Pair<Direction, BlockState?> {
            val world = entity.entityWorld
            val pos = entity.blockPos
            for (dir in Direction.entries) {
                if (dir.axis.isVertical) continue
                val ofs = pos.offset(dir)
                val state = world.getBlockState(ofs)
                if (state.isSideSolidFullSquare(world, ofs, dir.opposite)) return Pair(dir, state)
            }
            return Pair(Direction.UP, null)
        }
    }
}