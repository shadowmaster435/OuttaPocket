package org.shadowmaster435.outtapocket.util

import net.minecraft.component.ComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.CustomModelDataComponent
import net.minecraft.component.type.NbtComponent
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import org.joml.Vector2f
import org.joml.Vector3d
import org.joml.Vector3f
import kotlin.math.sqrt

fun Entity.setPos(pos: Vec3d) {
    setPos(pos.x, pos.y, pos.z)
}

fun Direction.project(ofs: Double, pos: Vec3d): Vec3d {
    return when (this) {
        Direction.DOWN, Direction.UP -> Vec3d(pos.x, vector.y.toDouble() * ofs, pos.z)
        Direction.NORTH, Direction.SOUTH -> Vec3d(pos.x, pos.y, vector.z.toDouble() * ofs)
        Direction.WEST, Direction.EAST -> Vec3d(vector.x.toDouble() * ofs, pos.y, pos.z)
    }
}

fun Direction.raycast(range: Double, entity: Entity): HitResult {
    val sourcePos = entity.boundingBox.horizontalCenter
    val world = entity.entityWorld
    val ctx = RaycastContext(sourcePos, sourcePos.add(Vec3d(this.vector).multiply(range)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity)
    return world.raycast(ctx)
}

fun BlockSoundGroup.playBreakSound(entity: Entity, category: SoundCategory) {
    val volume = (this.getVolume() + 1.0F) / 2.0F
    val pitch = this.getPitch() * 0.8F
    val pos = entity.entityPos
    val world = entity.entityWorld
    val event = this.breakSound
    world.playSound(entity, pos.x, pos.y, pos.z, event, category, volume, pitch)
}

operator fun Vec3d.invoke(randomScale: Double, random: Random): Vec3d {
    return Vec3d(
        ((0.5 + random.nextDouble() * 0.5) * 2.0) * randomScale,
        ((0.5 + random.nextDouble() * 0.5) * 2.0) * randomScale,
        ((0.5 + random.nextDouble() * 0.5) * 2.0) * randomScale
    )
}

fun ItemStack.mergeNbtString(key: String, value: String): ItemStack {
    val originalNbt = this[DataComponentTypes.CUSTOM_DATA]?.copyNbt() ?: NbtCompound()

    this.applyComponentsFrom(
        ComponentMap.of(components, ComponentMap.builder()
                .add(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(run {
                    originalNbt.putString(key, value)
                    originalNbt
                }
            )).build()
        )
    )
    return this
}

fun ItemStack.withCustomModelData(data: String): ItemStack {
    this[DataComponentTypes.CUSTOM_MODEL_DATA] = CustomModelDataComponent(
        listOf<Float>(), listOf<Boolean>(),listOf<String>(
            data
        ), listOf()
    )
    return this
}

fun ItemStack.copyWithCustomModelData(data: String): ItemStack {
    val copy = this.copy()
    copy[DataComponentTypes.CUSTOM_MODEL_DATA] = CustomModelDataComponent(
        listOf<Float>(), listOf<Boolean>(),listOf<String>(
            data
        ), listOf()
    )
    return copy
}
fun Vector3f.getPitchToward(to: Vector3f): Float {
    val d = (x - to.x).toDouble()
    val e = (y - to.y).toDouble()
    val f = (z - to.z).toDouble()
    val g = sqrt(d * d + f * f)
    return Math.toRadians((-(MathHelper.atan2(e, g) * 180.0f / Math.PI.toFloat()))).toFloat()
}

fun Vector3f.getYawToward(to: Vector3f): Float {
    val d = (x - to.x).toDouble()
    val e = (z - to.z).toDouble()
    return Math.toRadians((MathHelper.atan2(e, d) * 180.0f / Math.PI.toFloat()) - 90.0f).toFloat()
}

fun Vector3f.getEulerAnglesToward(to: Vector3f): Vector2f {
    return Vector2f(getYawToward(to), getPitchToward(to))
}
fun Vector3f.angleVecTo(other: Vector3f): Vector3f {
    return Vector3f(other).sub(this).normalize()
}
fun Vector3d.angleVecTo(other: Vector3d): Vector3d {
    return Vector3d(other).sub(this).normalize()
}

fun Vec3d.angleVecTo(other: Vec3d): Vec3d {
    return other.subtract(this).normalize()
}