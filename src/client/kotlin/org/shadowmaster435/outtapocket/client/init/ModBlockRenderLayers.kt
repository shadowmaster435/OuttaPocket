package org.shadowmaster435.outtapocket.client.init

import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap
import net.minecraft.client.render.BlockRenderLayer
import org.shadowmaster435.outtapocket.init.ModBlocks

object ModBlockRenderLayers {

    fun init() {
        BlockRenderLayerMap.putBlock(ModBlocks.GEAR, BlockRenderLayer.CUTOUT_MIPPED)
        BlockRenderLayerMap.putBlock(ModBlocks.CYAN_ROSE, BlockRenderLayer.CUTOUT_MIPPED)
        BlockRenderLayerMap.putBlock(ModBlocks.RED_ROSE, BlockRenderLayer.CUTOUT_MIPPED)
        BlockRenderLayerMap.putBlock(ModBlocks.PAEONIA, BlockRenderLayer.CUTOUT_MIPPED)
    }

}