package org.shadowmaster435.outtapocket.client.model

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.renderer.v1.Renderer
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableMesh
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView.BAKE_LOCK_UV
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter
import net.fabricmc.fabric.api.renderer.v1.model.FabricBlockStateModel
import net.fabricmc.fabric.api.util.TriState
import net.fabricmc.fabric.impl.renderer.RendererManager
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
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockRenderView
import org.shadowmaster435.outtapocket.Outtapocket
import org.shadowmaster435.outtapocket.block.AlternatorBlock
import org.shadowmaster435.outtapocket.block.GearBlock.Companion.TORQUE
import org.shadowmaster435.outtapocket.block.MotorBlock
import org.shadowmaster435.outtapocket.block.RelayBlock
import org.shadowmaster435.outtapocket.init.ModBlocks
import java.util.function.Predicate
import kotlin.math.max


@Environment(EnvType.CLIENT)
class GearModel : FabricBlockStateModel, BlockStateModel, BlockStateModel.Unbaked {

    val cw: SpriteIdentifier = SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(Outtapocket.MOD_ID,"block/gear_cw"))
    val ccw: SpriteIdentifier = SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(Outtapocket.MOD_ID,"block/gear_ccw"))
    val particle: SpriteIdentifier = SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(Outtapocket.MOD_ID,"block/gear_item"))
    val dot: SpriteIdentifier = SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(Outtapocket.MOD_ID,"block/gear_dot"))
    private val sprites: MutableList<Sprite> = mutableListOf()
    private lateinit var mesh: MutableMesh;
    override fun emitQuads(emitter: QuadEmitter, blockView: BlockRenderView, pos: BlockPos, state: BlockState?, random: Random?, cullTest: Predicate<Direction?>?) {
        if (sprites.isEmpty()) {
            val getter = MinecraftClient.getInstance().atlasManager
            sprites.add(getter.getSprite(cw))
            sprites.add(getter.getSprite(ccw))
            sprites.add(getter.getSprite(particle))
            sprites.add(getter.getSprite(dot))
        }
        for (dir in Direction.entries) {
            addSide(emitter, blockView, pos, dir)
        }
    }

    override fun addParts(random: Random?, parts: List<BlockModelPart?>?) {}
    fun isValidSide( blockView: BlockRenderView, pos: BlockPos, direction: Direction): Boolean {
        val state = blockView.getBlockState(pos.offset(direction))
        if (state.isOf(ModBlocks.MOTOR)) return MotorBlock.isValidSide(direction, state)
        if (state.isOf(ModBlocks.ALTERNATOR)) return AlternatorBlock.isValidSide(direction, state)
        if (state.isOf(ModBlocks.RELAY)) return RelayBlock.isValidSide(direction, state)
        return !(state.isAir || state.isOf(ModBlocks.GEAR)) && state.isSideSolidFullSquare(
            blockView, pos.offset(direction), direction.opposite
        )
    }
    fun addSide(emitter: QuadEmitter, blockView: BlockRenderView, pos: BlockPos, direction: Direction) {
        if (isValidSide(blockView, pos, direction)) {
            val state = blockView.getBlockState(pos)
            val torque = state.get(TORQUE)
            var alternate = (pos.x + pos.y + pos.z) % 2 == 0
            if (direction.axis.isHorizontal) {
                alternate = !alternate
            }
            val sprite = sprites[if (alternate) 1 else 0]
            val depthOfs = 0.985f + if (alternate) 0.005f else 0.0f

            val mI = 0.15625f * (sprite.uvScaleDelta * 8)
            val mX = 0.84375f * (sprite.uvScaleDelta * 8)
            val tDelt = ((torque * 16) - 1)
            val b = max(tDelt - 255, 0)
            val r = max((255 - b) - (max((512 - tDelt) - 256, 0)), 0)

            val col = ColorHelper.getArgb(r, 0, b)
            emitter
                .square(direction.opposite, -0.1875f, -0.1875f, 1.1875f, 1.1875f, depthOfs)
                .color(-1,-1,-1,-1)
                .spriteBake(sprite, 0)
                .uv(0, sprite.minU + mI , sprite.minV + mI)
                .uv(1, sprite.minU + mI,sprite.minV + mX)
                .uv(2, sprite.minU + mX,sprite.minV + mX)
                .uv(3, sprite.minU + mX,sprite.minV + mI)
                .ambientOcclusion(TriState.FALSE)
            .emit()
                .square(direction.opposite, 0.4375f, 0.4375f, 0.5625f, 0.5625f, depthOfs)
                .color(col,col,col,col)
                .spriteBake(sprites[3], BAKE_LOCK_UV)
                .ambientOcclusion(TriState.FALSE)
            .emit()
                .square(direction, -0.1875f, -0.1875f, 1.1875f, 1.1875f,1 - depthOfs)
                .color(-1,-1,-1,-1)
                .spriteBake(sprite, 0)
                .uv(0, sprite.minU + mI , sprite.minV + mI)
                .uv(1, sprite.minU + mI,sprite.minV + mX)
                .uv(2, sprite.minU + mX,sprite.minV + mX)
                .uv(3, sprite.minU + mX,sprite.minV + mI)
                .ambientOcclusion(TriState.FALSE)
            .emit()
                .square(direction, 0.4375f, 0.4375f, 0.5625f, 0.5625f, 1 - depthOfs)
                .color(col,col,col,col)
                .spriteBake(sprites[3], BAKE_LOCK_UV)
                .ambientOcclusion(TriState.FALSE)
            .emit()
        }
    }



    override fun particleSprite(): Sprite {
        return sprites[2]
    }

    override fun bake(baker: Baker): BlockStateModel {
        val renderer: Renderer = RendererManager.getRenderer()
        val builder: MutableMesh = renderer.mutableMesh()
        mesh = builder;
        return this
    }

    override fun resolve(resolver: ResolvableModel.Resolver?) {}


}