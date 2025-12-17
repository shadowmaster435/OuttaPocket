package org.shadowmaster435.outtapocket.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.loot.LootTable
import net.minecraft.loot.context.LootWorldContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.structure.StructurePlacementData
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier
import net.minecraft.util.ItemScatterer
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import org.shadowmaster435.outtapocket.block.entity.NetherReactorCoreBlockEntity
import org.shadowmaster435.outtapocket.init.ModBlocks

class NetherReactorCoreBlock(settings: Settings) : BlockWithEntity(settings) {
    

    private val structure: HashMap<BlockPos, Int> = HashMap(mapOf(
        Pair(BlockPos(-1,-1,-1), 2),
        Pair(BlockPos(-1,-1,0), 1),
        Pair(BlockPos(-1,-1,1), 2),
        Pair(BlockPos(0,-1,-1), 1),
        Pair(BlockPos(0,-1,0), 1),
        Pair(BlockPos(0,-1,1), 1),
        Pair(BlockPos(1,-1,-1), 2),
        Pair(BlockPos(1,-1,0), 1),
        Pair(BlockPos(1,-1,1), 2),
        Pair(BlockPos(-1,0,-1), 1),
        Pair(BlockPos(-1,0,0), 0),
        Pair(BlockPos(-1,0,1), 1),
        Pair(BlockPos(0,0,-1), 0),
        Pair(BlockPos(0,0,1), 0),
        Pair(BlockPos(1,0,-1), 1),
        Pair(BlockPos(1,0,0), 0),
        Pair(BlockPos(1,0,1), 1),
        Pair(BlockPos(-1,1,-1), 0),
        Pair(BlockPos(-1,1,0), 1),
        Pair(BlockPos(-1,1,1), 0),
        Pair(BlockPos(0,1,-1), 1),
        Pair(BlockPos(0,1,0), 1),
        Pair(BlockPos(0,1,1), 1),
        Pair(BlockPos(1,1,-1), 0),
        Pair(BlockPos(1,1,0), 1),
        Pair(BlockPos(1,1,1), 0)
    ))

    init {
        defaultState = defaultState.with(STATE, ReactorState.DORMANT)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(STATE)
    }
    override fun getCodec(): MapCodec<out NetherReactorCoreBlock> {
        return createCodec<NetherReactorCoreBlock> { settings: Settings -> NetherReactorCoreBlock(settings) }
    }
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return NetherReactorCoreBlockEntity(pos, state)
    }

    override fun onUse(
        state: BlockState?,
        world: World,
        pos: BlockPos,
        player: PlayerEntity?,
        hit: BlockHitResult?
    ): ActionResult? {
        if (state?.get(STATE) == ReactorState.USED) return ActionResult.PASS
        if (world is ServerWorld && player is ServerPlayerEntity) {
            if (!world.isInBuildLimit(BlockPos(pos.x, pos.y + 32, pos.z))) {
                player.sendMessage(Text.translatable("outta_pocket.nether_reactor.too_high"), false)
                return ActionResult.PASS
            }
            var matchesStructure = true
            structure.forEach Start@{
                if (world.getBlockState(pos.add(it.key)).block != when(it.value) {
                        1 -> Blocks.COBBLESTONE
                        2 -> Blocks.GOLD_BLOCK
                        else -> Blocks.AIR
                    }) {
                    matchesStructure = false
                    return@Start
                }
            }
            if (matchesStructure) {
                val template = world.structureTemplateManager.getTemplateOrBlank(Identifier.of("outta_pocket:nether_spire"))
                template.place(world,pos.add(-8,-3,-8), pos, StructurePlacementData(), Random.createLocal(), NOTIFY_ALL_AND_REDRAW)
                val blockEntity = world.getBlockEntity(pos) as NetherReactorCoreBlockEntity
                world.setBlockState(pos, state?.with(STATE, ReactorState.ACTIVE))
                player.sendMessage(Text.translatable("outta_pocket.nether_reactor.success"), false)
                blockEntity.life = 900
                blockEntity.used = true
            } else {
                player.sendMessage(Text.translatable("outta_pocket.nether_reactor.pattern_error"), false)
            }
            return if (matchesStructure) ActionResult.PASS else ActionResult.SUCCESS_SERVER
        } else {
            return ActionResult.SUCCESS
        }

    }

    enum class ReactorState(val string: String) : StringIdentifiable {
        DORMANT("dormant"),
        ACTIVE("active"),
        USED("used");
        override fun asString(): String = string
    }

    override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>
    ): BlockEntityTicker<T?>? {
        return validateTicker<NetherReactorCoreBlockEntity, T>(
            type,
            ModBlocks.NETHER_REACTOR_CORE_BLOCK_ENTITY,
            BlockEntityTicker { world: World, pos: BlockPos, state: BlockState, blockEntity: NetherReactorCoreBlockEntity ->
                NetherReactorCoreBlockEntity.tick(
                    world,
                    pos,
                    state,
                    blockEntity
                )
            })
    }



    companion object {
        val STATE: EnumProperty<ReactorState> = EnumProperty.of("state", ReactorState::class.java)

    }
}