package org.shadowmaster435.outtapocket.entity

import net.minecraft.block.BlockState
import net.minecraft.component.ComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.CustomModelDataComponent
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.FlyingItemEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LazyEntityReference
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ProjectileDeflection
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.thrown.ThrownEntity
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.storage.ReadView
import net.minecraft.storage.WriteView
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.shadowmaster435.outtapocket.MixinHelper
import org.shadowmaster435.outtapocket.Outtapocket
import org.shadowmaster435.outtapocket.util.angleVecTo
import org.shadowmaster435.outtapocket.util.mergeNbtString
import org.shadowmaster435.outtapocket.util.withCustomModelData
import java.util.*


class FlailProjectileEntity(type: EntityType<FlailProjectileEntity>, world: World, var thrower: LivingEntity? = null, val sourceStack: ItemStack = ItemStack.EMPTY) : ThrownEntity(type, world), FlyingItemEntity {
    var life = 0
    val piercedEntities = mutableListOf<Entity>()

    init {
        noClip = true
    }

    override fun readData(view: ReadView) {
        super.readData(view)
        life = view.getInt("life", 0)
        thrower = entityWorld.getEntity(UUID.fromString(view.getString("thrower", ""))) as LivingEntity?
    }

    override fun writeData(view: WriteView) {
        view.putInt("life", life)
        view.putString("thrower", thrower?.uuid?.toString() ?: "")
        super.writeData(view)
    }
    override fun initDataTracker(builder: DataTracker.Builder?) {
    }

    fun getDamage(entity: Entity): Float {
        val attributes = sourceStack.components.get(DataComponentTypes.ATTRIBUTE_MODIFIERS)
        val atrDamage = attributes?.applyOperations(1.0, EquipmentSlot.MAINHAND) ?: 0.0
        return EnchantmentHelper.getDamage(entityWorld as ServerWorld, sourceStack, entity, damageSources.thrown(entity, thrower), atrDamage.toFloat())
    }

    override fun deflect(
        deflection: ProjectileDeflection?,
        deflector: Entity?,
        lazyEntityReference: LazyEntityReference<Entity?>?,
        fromAttack: Boolean
    ): Boolean {
        return false
    }

    override fun hitOrDeflect(hitResult: HitResult): ProjectileDeflection? {
        this.onCollision(hitResult)

        return ProjectileDeflection.NONE
    }

    override fun collidesWith(entity: Entity?): Boolean {
        return thrower !== entity
    }

    override fun tickLeftOwner() {
        super.tickLeftOwner()
    }

    override fun onBlockCollision(state: BlockState) {
        if (!MixinHelper.leftOwner(this)) hit(thrower)

        life = 3
    }

    fun lerpBack() {
        val throwerPos = thrower?.entityPos?.add(
            Vec3d(0.0, thrower?.getEyeHeight(thrower?.pose)?.toDouble()!!, 0.0),
        ) ?: entityPos
        val vel = entityPos.angleVecTo(throwerPos)
        velocity = vel
        velocityDirty = true
    }


    override fun tick() {
        super.tick()
        if (life < 3) velocity = velocity.multiply(0.2)
        else {
            if (life == 8) piercedEntities.clear()
            if (!entityWorld.isClient) {
                tickCheckInsideOwner()
                lerpBack()
            }
        }
        if (life < 50) life += 1
        else hit(thrower)
    }
    override fun applyGravity() {

    }

    override fun onCollision(hitResult: HitResult) {
		if (hitResult.type == HitResult.Type.ENTITY) {
            val result = hitResult as EntityHitResult
            val other = result.entity
            hit(other)
            piercedEntities.add(other)
            velocity = velocity.multiply(5.0)
            if (entityWorld is ServerWorld && other !== thrower) {
                sourceStack.damage(1, thrower, Hand.MAIN_HAND)
                other.damage(
                    entityWorld as ServerWorld,
                    this.damageSources.thrown(entity, thrower),
                    getDamage(other)
                )
            }
        }
    }

    fun tickCheckInsideOwner() {
        val collisions = entityWorld.getOtherEntities(null, boundingBox)
        for (other in collisions) {
            hit(other)
        }
    }

    fun hit(other: Entity?) {
        if (other === thrower && life >= 3) {
            if (thrower is PlayerEntity) {
                (thrower as PlayerEntity).itemCooldownManager.set(sourceStack, 0)
            }
            sourceStack.withCustomModelData("normal")
            remove(RemovalReason.DISCARDED)
        }
    }

    override fun canHit(entity: Entity?): Boolean {
        return entity !== thrower && !piercedEntities.contains(entity)
    }
    override fun getStack(): ItemStack {
        return displayStack
    }
    companion object {

        val flails = mutableListOf<FlailProjectileEntity>()

        val displayStack: ItemStack = run {
            val snowball = Items.SNOWBALL.defaultStack
            snowball[DataComponentTypes.ITEM_MODEL] = Identifier.of(Outtapocket.MOD_ID, "flail")
            snowball[DataComponentTypes.CUSTOM_MODEL_DATA] = CustomModelDataComponent(
                listOf<Float>(), listOf<Boolean>(),listOf<String>(
                    "projectile",
                ), listOf()
            )
            snowball
        }
    }
}