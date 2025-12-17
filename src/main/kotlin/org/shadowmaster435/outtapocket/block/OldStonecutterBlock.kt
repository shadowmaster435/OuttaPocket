package org.shadowmaster435.outtapocket.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.StonecutterBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.screen.StonecutterScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.stat.Stats
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.function.Function

class OldStonecutterBlock(settings: Settings) : Block(settings) {

    public override fun getCodec(): MapCodec<OldStonecutterBlock> {
        return CODEC
    }

    init {
        defaultState = stateManager.defaultState
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult?
    ): ActionResult {
        if (!world.isClient) {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
            player.incrementStat(Stats.INTERACT_WITH_STONECUTTER)
        }

        return ActionResult.SUCCESS
    }

    override fun createScreenHandlerFactory(
        state: BlockState?,
        world: World?,
        pos: BlockPos?
    ): NamedScreenHandlerFactory {
        return SimpleNamedScreenHandlerFactory(
            { syncId: Int, playerInventory: PlayerInventory?, _: PlayerEntity? ->
                StonecutterScreenHandler(
                    syncId,
                    playerInventory,
                    ScreenHandlerContext.create(world, pos)
                )
            }, Text.translatable("container.stonecutter")
        )
    }
    companion object {
        val CODEC: MapCodec<OldStonecutterBlock> = createCodec { settings: Settings -> OldStonecutterBlock(settings) }

    }
}
