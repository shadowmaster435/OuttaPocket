package org.shadowmaster435.outtapocket.client.entity_renderer

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.command.OrderedRenderCommandQueue
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.client.render.entity.state.FlyingItemEntityRenderState
import net.minecraft.client.render.state.CameraRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.FishingRodItem
import net.minecraft.util.Arm
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import org.shadowmaster435.outtapocket.client.util.clPlayer
import org.shadowmaster435.outtapocket.client.util.tDelta
import org.shadowmaster435.outtapocket.entity.FlailProjectileEntity
import org.shadowmaster435.outtapocket.util.getPitchToward
import org.shadowmaster435.outtapocket.util.getYawToward

class FlailProjectileRenderer(context: EntityRendererFactory.Context) : FlyingItemEntityRenderer<FlailProjectileEntity>(context, 1f, false) {



    override fun render(
        state: FlyingItemEntityRenderState,
        matrixStack: MatrixStack,
        orderedRenderCommandQueue: OrderedRenderCommandQueue,
        cameraRenderState: CameraRenderState
    ) {
        val g: Float = clPlayer!!.getHandSwingProgress(tDelta)
        val h = MathHelper.sin(MathHelper.sqrt(g) * Math.PI.toFloat())
        val handPos = getHandPos(clPlayer!! as PlayerEntity, h, tDelta)
        val pos = handPos.subtract(Vec3d(state.x, state.y, state.z))
        matrixStack.push()

        orderedRenderCommandQueue.submitCustom(matrixStack, RenderLayer.getLines(), { entry, buff ->
            buff.vertex(entry, (state.width * 0.25).toFloat(), (state.height * 0.25).toFloat(), (state.width * 0.25).toFloat()).color(0f,0f,0f,1f).normal(entry, pos.normalize().toVector3f())
            buff.vertex(entry,pos.toVector3f()).color(0f,0f,0f,1f).normal(entry, pos.negate().normalize().toVector3f())
//            renderChain(buff, matrixStack, p, state.light)
        })
        matrixStack.pop()

        super.render(state, matrixStack, orderedRenderCommandQueue, cameraRenderState)
    }

    fun renderChain(buff: VertexConsumer, matrixStack: MatrixStack, entityPos: Vector3f, light: Int) {
        val playerPos = clPlayer!!.getLerpedPos(tDelta).add(0.0, clPlayer!!.getEyeHeight(clPlayer!!.pose).toDouble(), 0.0).toVector3f()
        val endPos = playerPos.add(playerPos).sub(entityPos)
        renderChainLink(buff, matrixStack.peek(), light)
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(
            endPos.getPitchToward(Vector3f(0f))
        ))
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(
            endPos.getYawToward(Vector3f(0f))
        ))

        matrixStack.push()
        var dist = endPos.length()
        matrixStack.translate(0f,endPos.length(),0f)
        while (dist > 0.125f) {
            if (((dist - 0.125f) % 0.25f) == 0f) {
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90f))
            }
            matrixStack.translate(0f, 0.125f, 0f)

            renderChainLink(buff, matrixStack.peek(), light)
            dist -= 0.125f
        }
        matrixStack.pop()
    }

    fun renderChainLink(buff: VertexConsumer, matricesEntry: MatrixStack.Entry, light: Int) {
        vertex(buff, matricesEntry, -0.09375f, -0.09375f, 0f, 0.0625f, light);
        vertex(buff, matricesEntry, 0.09375f, -0.09375f, 0.1875f, 0.0625f, light);
        vertex(buff, matricesEntry, 0.09375f, 0.09375f, 0.1875f, 0.25f, light);
        vertex(buff, matricesEntry, -0.09375f, 0.09375f, 0f, 0.25f, light);
//        buff.vertex(matricesEntry, -0.09375f, -0.09375f, 0f).texture(0f, 0.0625f).light(light).overlay(0).normal(matricesEntry, 0f,1f,0f).color(1f,1f,1f,1f)
//        buff.vertex(matricesEntry, -0.09375f, 0.09375f, 0f).texture(0.1875f, 0.25f).light(light).overlay(0).normal(matricesEntry, 0f,1f,0f).color(1f,1f,1f,1f)
//        buff.vertex(matricesEntry, 0.09375f, 0.09375f, 0f).texture(0.1875f, 0.25f).light(light).overlay(0).normal(matricesEntry, 0f,1f,0f).color(1f,1f,1f,1f)
//        buff.vertex(matricesEntry, 0.09375f, -0.09375f, 0f).texture(0f, 0.0625f).light(light).overlay(0).normal(matricesEntry, 0f,1f,0f).color(1f,1f,1f,1f)
//
//        buff.vertex(matricesEntry, 0.09375f, -0.09375f, 0f).texture(0f, 0.0625f).light(light).overlay(0).normal(matricesEntry, 0f,1f,0f).color(1f,1f,1f,1f)
//        buff.vertex(matricesEntry, 0.09375f, 0.09375f, 0f).texture(0.1875f, 0.25f).light(light).overlay(0).normal(matricesEntry, 0f,1f,0f).color(1f,1f,1f,1f)
//        buff.vertex(matricesEntry, -0.09375f, 0.09375f, 0f).texture(0.1875f, 0.25f).light(light).overlay(0).normal(matricesEntry, 0f,1f,0f).color(1f,1f,1f,1f)
//        buff.vertex(matricesEntry, -0.09375f, -0.09375f, 0f).texture(0f, 0.0625f).light(light).overlay(0).normal(matricesEntry, 0f,1f,0f).color(1f,1f,1f,1f)
    }


    private fun vertex(
        vertexConsumer: VertexConsumer,
        matrix: MatrixStack.Entry?,
        x: Float,
        y: Float,
        u: Float,
        v: Float,
        light: Int
    ) {
        vertexConsumer.vertex(matrix, x, y, 0.0f)
            .color(255, 255, 255, 255)
            .texture(u, v)
            .overlay(OverlayTexture.DEFAULT_UV)
            .light(light)
            .normal(matrix, 0.0f, 1.0f, 0.0f)
    }
    fun getArmHoldingRod(player: PlayerEntity): Arm? {
        return if (player.mainHandStack.item is FishingRodItem) player.mainArm else player.mainArm.opposite
    }
    private fun getHandPos(player: PlayerEntity, f: Float, tickProgress: Float): Vec3d {
        val i = 1
        if (this.dispatcher.gameOptions.perspective.isFirstPerson && player === MinecraftClient.getInstance().player
        ) {
            val m = 960.0 / this.dispatcher.gameOptions.fov.getValue()
            val vec3d =
                this.dispatcher.camera!!.getProjection().getPosition(i * 0.635f, -0.5f).multiply(m).rotateY(f * 0.635f)
                    .rotateX(-f * 0.7f)
            return player.getCameraPosVec(tickProgress).add(vec3d)
        } else {
            val g = MathHelper.lerp(tickProgress, player.lastBodyYaw, player.bodyYaw) * (Math.PI / 180.0).toFloat()
            val d = MathHelper.sin(g).toDouble()
            val e = MathHelper.cos(g).toDouble()
            val h = player.getScale()
            val j = i * 0.35 * h
            val k = 0.625 * h
            val l = if (player.isInSneakingPose) -0.1875f else 0.0f
            return player.getCameraPosVec(tickProgress).add(-e * j - d * k, l - 0.625 * h, -d * j + e * k)
        }
    }
    companion object {
        val CHAIN = Identifier.ofVanilla("textures/block/stone.png")
        val LAYER: RenderLayer? = RenderLayer.getDebugQuads()

    }
}