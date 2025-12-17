package org.shadowmaster435.outtapocket.init

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.FlowerBlock
import net.minecraft.block.MapColor
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.enums.NoteBlockInstrument
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import org.shadowmaster435.outtapocket.Outtapocket
import org.shadowmaster435.outtapocket.block.AlternatorBlock
import org.shadowmaster435.outtapocket.block.BuggedBlock
import org.shadowmaster435.outtapocket.block.BuggedButtonBlock
import org.shadowmaster435.outtapocket.block.CheeseBlock
import org.shadowmaster435.outtapocket.block.CheeseBlock.Companion.WEDGES
import org.shadowmaster435.outtapocket.block.GearBlock
import org.shadowmaster435.outtapocket.block.MotorBlock
import org.shadowmaster435.outtapocket.block.NetherReactorCoreBlock
import org.shadowmaster435.outtapocket.block.OldStonecutterBlock
import org.shadowmaster435.outtapocket.block.RelayBlock
import org.shadowmaster435.outtapocket.block.entity.NetherReactorCoreBlockEntity
import org.shadowmaster435.outtapocket.util.LateInitBlockRef
import java.util.function.Function

object ModBlocks {

    val BUGGED_BLOCKS = mutableListOf<BuggedBlock>()


    val NETHER_REACTOR_CORE: NetherReactorCoreBlock = register(
        "nether_reactor_core",
        { settings: AbstractBlock.Settings -> NetherReactorCoreBlock(settings) },
        AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE).strength(3f, 6f).requiresTool(),
        true
    )

    val GLOWING_OBSIDIAN: Block = register(
        "glowing_obsidian",
        { settings: AbstractBlock.Settings -> Block(settings) },
        AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE).strength(50f, 1200f).requiresTool(),
        true
    )

    @JvmField
    val GEAR: GearBlock = register(
        "gear",
        { settings: AbstractBlock.Settings -> GearBlock(settings) },
        AbstractBlock.Settings.create().sounds(BlockSoundGroup.METAL).breakInstantly().mapColor(MapColor.STONE_GRAY).noCollision().nonOpaque()
            .pistonBehavior(PistonBehavior.BLOCK),
        true
    )

    @JvmField
    val MOTOR: MotorBlock = register(
        "motor",
        { settings: AbstractBlock.Settings -> MotorBlock(settings) },
        AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.STONE_GRAY)
            .strength(1.5F)
            .nonOpaque(),
        true
    )

    @JvmField
    val ALTERNATOR: AlternatorBlock = register(
        "alternator",
        { settings: AbstractBlock.Settings -> AlternatorBlock(settings) },
        AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.STONE_GRAY)
            .strength(1.5F)
            .nonOpaque(),
        true
    )

    val CHEESE: CheeseBlock = register(
        "cheese",
        { settings: AbstractBlock.Settings -> CheeseBlock(settings) },
        AbstractBlock.Settings.create().sounds(BlockSoundGroup.FUNGUS)
            .mapColor(MapColor.PALE_YELLOW)
            .strength(0.1f)
            .solidBlock {
                    state, _, _ ->
                state.get(WEDGES) == 255
            },
        true
    )

    val SIX_SIDED_PISTON: BuggedBlock = register(
        "six_sided_piston",
        { settings: AbstractBlock.Settings -> BuggedBlock(settings, Blocks.PISTON, LateInitBlockRef(Identifier.of(
            Outtapocket.MOD_ID, "bugged_six_sided_piston")))},
        AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.STONE_GRAY)
            .strength(1.5F)
            .nonOpaque(),
        true
    )

    val BUGGED_SIX_SIDED_PISTON: BuggedBlock = register(
        "bugged_six_sided_piston",
        { settings: AbstractBlock.Settings -> BuggedBlock(settings, SIX_SIDED_PISTON, Blocks.PISTON)},
        AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.STONE_GRAY)
            .strength(1.5F)
            .nonOpaque(),
        true
    )

    val SIX_SIDED_HAY_BLOCK: BuggedBlock = register(
        "six_sided_hay_block",
        { settings: AbstractBlock.Settings -> BuggedBlock(settings, Blocks.HAY_BLOCK, LateInitBlockRef(Identifier.of(
            Outtapocket.MOD_ID, "bugged_six_sided_hay_block")))},
        AbstractBlock.Settings.create().mapColor(MapColor.YELLOW).instrument(NoteBlockInstrument.BANJO).strength(0.5F).sounds(BlockSoundGroup.GRASS),
        true
    )

    val BUGGED_SIX_SIDED_HAY_BLOCK: BuggedBlock = register(
        "bugged_six_sided_hay_block",
        { settings: AbstractBlock.Settings -> BuggedBlock(settings, SIX_SIDED_HAY_BLOCK, LateInitBlockRef(Identifier.ofVanilla("hay_block")))},
        AbstractBlock.Settings.create().mapColor(MapColor.YELLOW).instrument(NoteBlockInstrument.BANJO).strength(0.5F).sounds(BlockSoundGroup.GRASS),
        true
    )

    @JvmField
    val SIX_SIDED_STICKY_PISTON: BuggedBlock = register(
        "six_sided_sticky_piston",
        { settings: AbstractBlock.Settings -> BuggedBlock(settings, Blocks.STICKY_PISTON) },
        AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.STONE_GRAY)
            .strength(1.5F)
            .nonOpaque(),
        true
    )

    @JvmField
    val RELAY: RelayBlock = register(
        "relay",
        { settings: AbstractBlock.Settings -> RelayBlock(settings) },
        AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.STONE_GRAY)
            .strength(1.5F)
            .nonOpaque(),
        true
    )
    @JvmField
    val STONECUTTER: OldStonecutterBlock = register(
        "stonecutter",
        { settings: AbstractBlock.Settings -> OldStonecutterBlock(settings) },
        AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE).strength(3.5F).requiresTool(),
        true
    )
    val NETHER_REACTOR_CORE_BLOCK_ENTITY: BlockEntityType<NetherReactorCoreBlockEntity> =
        register<BlockEntityType<NetherReactorCoreBlockEntity>>(
            "nether_reactor",
            FabricBlockEntityTypeBuilder.create(FabricBlockEntityTypeBuilder.Factory { pos: BlockPos, state: BlockState ->
                NetherReactorCoreBlockEntity(
                    pos,
                    state
                )
            }, NETHER_REACTOR_CORE).build()
        )

    val PAEONIA: FlowerBlock = register(
        "small_paeony",
        { settings: AbstractBlock.Settings -> FlowerBlock(StatusEffects.RESISTANCE, 5.0f, settings) },
        AbstractBlock.Settings.create()
            .sounds(BlockSoundGroup.GRASS)
            .breakInstantly()
            .offset(AbstractBlock.OffsetType.XZ)
            .pistonBehavior(PistonBehavior.DESTROY)
            .mapColor(MapColor.DARK_GREEN)
            .noCollision(),
        true
    )


    val RED_ROSE: FlowerBlock = register(
        "red_rose",
        { settings: AbstractBlock.Settings -> FlowerBlock(StatusEffects.NIGHT_VISION, 5.0f, settings) },
        AbstractBlock.Settings.create()
            .sounds(BlockSoundGroup.GRASS)
            .breakInstantly()
            .offset(AbstractBlock.OffsetType.XZ)
            .pistonBehavior(PistonBehavior.DESTROY)
            .mapColor(MapColor.DARK_GREEN)
            .noCollision(),
        true
    )

    val CYAN_ROSE: FlowerBlock = register(
        "cyan_rose",
        { settings: AbstractBlock.Settings -> FlowerBlock(StatusEffects.NIGHT_VISION, 5.0f, settings) },
        AbstractBlock.Settings.create()
            .sounds(BlockSoundGroup.GRASS)
            .breakInstantly()
            .offset(AbstractBlock.OffsetType.XZ)
            .pistonBehavior(PistonBehavior.DESTROY)
            .mapColor(MapColor.DARK_GREEN)
            .noCollision(),
        true
    )

    val BUGGED_BUTTONS = fun(): Map<Block, BuggedButtonBlock> {
        val map = mutableMapOf<Block, BuggedButtonBlock>()
        val names = listOf(
            Pair("stone", Blocks.STONE_BUTTON),
            Pair("polished_blackstone", Blocks.POLISHED_BLACKSTONE_BUTTON),
            Pair( "warped",Blocks.WARPED_BUTTON),
            Pair("crimson",Blocks.CRIMSON_BUTTON),
            Pair( "cherry",Blocks.CHERRY_BUTTON),
            Pair( "bamboo",Blocks.BAMBOO_BUTTON),
            Pair( "oak", Blocks.OAK_BUTTON),
            Pair( "dark_oak", Blocks.DARK_OAK_BUTTON),
            Pair("pale_oak", Blocks.PALE_OAK_BUTTON),
            Pair("mangrove", Blocks.MANGROVE_BUTTON),
            Pair("birch", Blocks.BIRCH_BUTTON),
            Pair("spruce", Blocks.SPRUCE_BUTTON),
            Pair("jungle",Blocks.JUNGLE_BUTTON),

            Pair("acacia",Blocks.ACACIA_BUTTON)
        )
        for (i in 0..<names.size) {
            val pair = names[i]
            map[pair.second] = register(
                "bugged_" + pair.first + "_button",
                {
                        settings: AbstractBlock.Settings -> BuggedButtonBlock(settings, when(i) {
                        2,3 -> 2
                        4 -> 3
                        5 -> 4
                        else -> if (i > 1) 1 else 0

                },  pair.second)
                },
                AbstractBlock.Settings.create().strength(0.5F).sounds(if (i > 5) BlockSoundGroup.WOOD else when(i) {
                    2,3 -> BlockSoundGroup.NETHER_WOOD
                    4 -> BlockSoundGroup.CHERRY_WOOD
                    5 -> BlockSoundGroup.BAMBOO_WOOD
                    else -> BlockSoundGroup.STONE
                }), true)
            Blocks.STONE_BUTTON
        }
        return map.toMap()
    }.invoke()

    private fun <B : Block> register(
        name: String,
        blockFactory: Function<AbstractBlock.Settings, B>,
        settings: AbstractBlock.Settings,
        shouldRegisterItem: Boolean
    ): B {
        val blockKey = keyOfBlock(name)
        val block = blockFactory.apply(settings.registryKey(blockKey))
        if (shouldRegisterItem) {
            val itemKey = keyOfItem(name)
            val blockItem = BlockItem(block, Item.Settings().registryKey(itemKey))
            Registry.register(Registries.ITEM, itemKey, blockItem)
        }
        if (block is BuggedBlock) {
            BUGGED_BLOCKS.add(block)
        }
        return Registry.register<Block, B>(Registries.BLOCK, blockKey, block)
    }

    private fun keyOfBlock(name: String): RegistryKey<Block> {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Outtapocket.MOD_ID, name))
    }

    private fun keyOfItem(name: String): RegistryKey<Item> {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Outtapocket.MOD_ID, name))
    }
    fun <T : BlockEntityType<*>> register(path: String, blockEntityType: T): T {
        return Registry.register<BlockEntityType<*>, T>(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Outtapocket.MOD_ID, path),
            blockEntityType
        )
    }
    fun init() {
        Blocks.HAY_BLOCK
    }

}