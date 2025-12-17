package org.shadowmaster435.outtapocket.client.model

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView.BAKE_LOCK_UV
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter
import net.fabricmc.fabric.api.renderer.v1.model.FabricBlockStateModel
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.Baker
import net.minecraft.client.render.model.BlockModelPart
import net.minecraft.client.render.model.BlockStateModel
import net.minecraft.client.render.model.ResolvableModel
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockRenderView
import org.shadowmaster435.outtapocket.Outtapocket
import org.shadowmaster435.outtapocket.block.CheeseBlock.Companion.WEDGES
import java.math.BigInteger
import java.util.function.Predicate
import kotlin.math.abs


class CheeseModel : FabricBlockStateModel, BlockStateModel, BlockStateModel.Unbaked {
    val spriteId: SpriteIdentifier = SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(Outtapocket.MOD_ID,"block/cheese"))
    var sprite : Sprite? = null
    override fun emitQuads(
        emitter: QuadEmitter,
        blockView: BlockRenderView?,
        pos: BlockPos,
        state: BlockState,
        random: Random?,
        cullTest: Predicate<Direction?>
    ) {

        if (sprite == null) {
            val getter = MinecraftClient.getInstance().atlasManager
            sprite = getter.getSprite(spriteId)
        }
        val wedges = state.get(WEDGES)
        val tes = (wedges and 1) != 0
        val tws = ((wedges shr 1) and 1) != 0
        val ten = ((wedges shr 2) and 1) != 0
        val twn = ((wedges shr 3) and 1) != 0
        val bes = ((wedges shr 4) and 1) != 0
        val bws = ((wedges shr 5) and 1) != 0
        val ben = ((wedges shr 6) and 1) != 0
        val bwn = ((wedges shr 7) and 1) != 0


        if (tes) {
            bakeLockedCube(0.5f,0.5f,0.5f,0.5f, emitter, sprite!!)
        }
        if (ten) {
            bakeLockedCube(0f,0.5f,0.5f,0.5f, emitter, sprite!!)
        }
        if (tws) {
            bakeLockedCube(0.5f,0.5f,0f,0.5f, emitter, sprite!!)
        }
        if (twn) {
            bakeLockedCube(0f,0.5f,0f,0.5f, emitter, sprite!!)
        }
        if (bes) {
            bakeLockedCube(0.5f,0f,0.5f,0.5f, emitter, sprite!!)
        }
        if (ben) {
            bakeLockedCube(0f,0f,0.5f,0.5f, emitter, sprite!!)
        }
        if (bws) {
            bakeLockedCube(0.5f,0f,0f,0.5f, emitter, sprite!!)
        }
        if (bwn) {
            bakeLockedCube(0f,0f,0f,0.5f, emitter, sprite!!)
        }
    }
    override fun addParts(
        random: Random?,
        parts: List<BlockModelPart>
    ) {
    }

    override fun particleSprite(): Sprite {
        if (sprite == null) {
            val getter = MinecraftClient.getInstance().atlasManager
            sprite = getter.getSprite(spriteId)
        }
        return sprite!!
    }

    override fun bake(baker: Baker): BlockStateModel {
        return this
    }

    override fun resolve(resolver: ResolvableModel.Resolver) {

    }
    fun bakeLockedCube(xOfs: Float, yOfs: Float, zOfs: Float, size: Float, emitter: QuadEmitter, sprite: Sprite) {
        for (dir in Direction.entries) {

            when(dir) {
                Direction.UP -> {
                    emitter
                        .square(dir,size - xOfs, zOfs, (size - xOfs) + size, (zOfs) + size, size - (size - yOfs))
                        .color(-1,-1,-1,-1)
                        .spriteBake(sprite, BAKE_LOCK_UV)
                        .emit()
                }
                Direction.DOWN -> {
                    emitter
                        .square(dir,size - xOfs, size - zOfs, (size - xOfs) + size, (size - zOfs) + size , 1.0f - (size + yOfs))
                        .color(-1,-1,-1,-1)
                        .spriteBake(sprite, BAKE_LOCK_UV)
                        .emit()
                }
                Direction.NORTH -> emitter
                    .square(dir,size - (size - xOfs), size - yOfs, (size - (size - xOfs)) + size, (size - yOfs) + size,1.0f - (size + zOfs))
                    .color(-1,-1,-1,-1)
                    .spriteBake(sprite, BAKE_LOCK_UV)
                    .emit()
                Direction.SOUTH -> emitter
                    .square(dir,size - xOfs, size - yOfs, (size - xOfs) + size, (size - yOfs) + size,size - (size - zOfs))
                    .color(-1,-1,-1,-1)
                    .spriteBake(sprite, BAKE_LOCK_UV)
                    .emit()
                Direction.EAST -> emitter
                    .square(dir,zOfs, size - yOfs, zOfs + size, size - yOfs + size,size - (size - xOfs))
                    .color(-1,-1,-1,-1)
                    .spriteBake(sprite, BAKE_LOCK_UV)
                    .emit()
                Direction.WEST -> emitter
                    .square(dir,(size - zOfs), size - yOfs, (size - zOfs) + size, size - yOfs + size,size - (xOfs))
                    .color(-1,-1,-1,-1)
                    .spriteBake(sprite, BAKE_LOCK_UV)
                    .emit()

//                Direction.EAST -> emitter.square(dir,zOfs, yOfs, 1.0f - zOfs, yOfs + size,size + (xOfs - size))
//                    .color(-1,-1,-1,-1)
//                    .spriteBake(sprite, BAKE_LOCK_UV)
//                    .emit()
//                Direction.WEST -> emitter.square(dir,zOfs, yOfs, zOfs + size, yOfs + size,size + (xOfs - size))
//                    .color(-1,-1,-1,-1)
//                    .spriteBake(sprite, BAKE_LOCK_UV)
//                    .emit()

            }
        }
    }

}