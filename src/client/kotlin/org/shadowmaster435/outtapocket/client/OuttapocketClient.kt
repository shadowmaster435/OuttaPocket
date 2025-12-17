package org.shadowmaster435.outtapocket.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap
import net.minecraft.client.render.BlockRenderLayer
import net.minecraft.client.render.RenderLayer
import org.shadowmaster435.outtapocket.client.init.ModBlockRenderLayers
import org.shadowmaster435.outtapocket.client.init.ModEntityRenderers
import org.shadowmaster435.outtapocket.client.init.ModelLoader
import org.shadowmaster435.outtapocket.init.ModBlocks


class OuttapocketClient : ClientModInitializer {

    override fun onInitializeClient() {
        ModelLoadingPlugin.register(ModelLoader)
        ModBlockRenderLayers.init()
        ModEntityRenderers.init()
    }
}
