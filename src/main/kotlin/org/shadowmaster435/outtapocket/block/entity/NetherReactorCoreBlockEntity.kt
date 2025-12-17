package org.shadowmaster435.outtapocket.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.ZombifiedPiglinEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.storage.ReadView
import net.minecraft.storage.WriteView
import net.minecraft.util.ItemScatterer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import org.shadowmaster435.outtapocket.block.NetherReactorCoreBlock
import org.shadowmaster435.outtapocket.init.ModBlocks
class NetherReactorCoreBlockEntity(pos: BlockPos, state: BlockState?, var life: Int = 0, var used: Boolean = false, var progress: Int = 0) : BlockEntity(ModBlocks.NETHER_REACTOR_CORE_BLOCK_ENTITY, pos, state) {


    companion object {
        fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: NetherReactorCoreBlockEntity) {
            if (blockEntity.life > 0) {
                blockEntity.doReaction(world, pos)
            } else {
                if (blockEntity.used && world.getBlockState(pos).get(NetherReactorCoreBlock.STATE) != NetherReactorCoreBlock.ReactorState.USED) {
                    blockEntity.endReaction(world, pos, state)
                }
            }
        }
    }

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


    
    fun doReaction(world: World, pos: BlockPos) {
        if (life > 0 && life % 100 == 0) {
            val rand = Random.createLocal()
            (0..6).forEach { _ ->
                val randomPos = BlockPos(rand.nextBetween(-7, 7),-1,rand.nextBetween(-7, 7)).add(pos)
                if (randomPos.getManhattanDistance(pos) > 2)  spawnItem(world, randomPos)
            }

            (0..Random.createLocal().nextBetween(0,2)).forEach { _ ->
                val randomEntityPos = BlockPos(rand.nextBetween(-6,6),-1,rand.nextBetween(-7, 7)).add(pos)
                if (randomEntityPos.getManhattanDistance(pos) > 2) spawnEntity(world, randomEntityPos)
            }
        }
        if (life > 0 && life % 150 == 0) {
            when(progress) {
                0 -> replaceLayer(world, pos, replaceGold = false, useObsidian = false, layer = -1)
                1 -> replaceLayer(world, pos, replaceGold = false, useObsidian = false, layer = 0)
                2 -> replaceLayer(world, pos, replaceGold = false, useObsidian = false, layer = 1)
                3 -> replaceLayer(world, pos, replaceGold = true, useObsidian = false, layer = -1)
                4 -> replaceLayer(world, pos, replaceGold = true, useObsidian = true, layer = 1)
                5 -> replaceLayer(world, pos, replaceGold = true, useObsidian = true, layer = 0)
            }
            progress += 1
        }
        life -= 1
    }

    fun replaceLayer(world: World, pos: BlockPos, replaceGold: Boolean, useObsidian: Boolean, layer: Int) {
        for (x in -1..1) {
            for (z in -1..1) {
                val p = pos.add(x, layer, z)
                if ((world.isAir(p) && !useObsidian) || world.getBlockState(p).isOf(ModBlocks.NETHER_REACTOR_CORE)) continue
                val block = (if (useObsidian) Blocks.OBSIDIAN else ModBlocks.GLOWING_OBSIDIAN).defaultState
                if (((world.isAir(p) && useObsidian) || (world.getBlockState(p).isOf(Blocks.GOLD_BLOCK)) && replaceGold) || world.getBlockState(p).isOf(Blocks.COBBLESTONE) || world.getBlockState(p).isOf(
                        ModBlocks.GLOWING_OBSIDIAN)) {
                    world.setBlockState(p, block)
                }
            }
        }
    }

    fun endReaction(world: World, pos: BlockPos, state: BlockState) {
        for (x in pos.x - 8..pos.x + 9) {
            for (y in pos.y - 1..pos.y + 32) {
                for (z in pos.z - 8..pos.z + 9) {
                    val p = BlockPos(x,y,z)
                    if (Random.createLocal().nextDouble() > 0.8 && world.getBlockState(p).isOf(Blocks.NETHERRACK)) {
                        world.setBlockState(BlockPos(x,y,z), Blocks.AIR.defaultState)
                    }
                }
            }
        }
        world.setBlockState(pos, state.with(NetherReactorCoreBlock.STATE, NetherReactorCoreBlock.ReactorState.USED))
        replaceLayer(world, pos, replaceGold = true, useObsidian = true, layer = -1)
        used = true
    }

    fun spawnItem(world: World, pos: BlockPos) {
        val itemStack = if (Math.random() < 0.75) {
            ItemStack(if (Math.random() > 0.5) Items.QUARTZ else Items.GLOWSTONE_DUST,
                Random.createLocal().nextBetween(2,6)
            )
        } else {
            ItemStack(arrayOf(
                Items.RED_MUSHROOM,
                Items.BROWN_MUSHROOM,
                Items.CRIMSON_FUNGUS,
                Items.SOUL_SAND,
                Items.SOUL_SOIL,
                Items.WARPED_FUNGUS,
                Items.PUMPKIN_SEEDS,
                Items.MELON_SEEDS,
                Items.CACTUS,
                Items.SUGAR_CANE,
            ).random(), Random.createLocal().nextBetween(0,2))
        }
        val p = pos.toBottomCenterPos()
        ItemScatterer.spawn(world, p.x, p.y, p.z, itemStack)

    }

    fun spawnEntity(world: World, pos: BlockPos) {
        val p = pos.toBottomCenterPos()
        val piglin = ZombifiedPiglinEntity(EntityType.ZOMBIFIED_PIGLIN, world)
        piglin.setPos(p.x, p.y, p.z)
        world.spawnEntity(piglin)

    }

    override fun readData(view: ReadView) {
        this.life = view.getInt("life",0)
        this.used = view.getBoolean("used",false)
        super.readData(view)
    }

    override fun writeData(view: WriteView) {
        view.putInt("life", life)
        view.putBoolean("used", used)
        super.writeData(view)
    }


}