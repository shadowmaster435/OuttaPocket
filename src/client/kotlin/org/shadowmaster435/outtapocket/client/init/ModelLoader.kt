package org.shadowmaster435.outtapocket.client.init

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier
import net.minecraft.client.render.model.BlockStateModel
import org.shadowmaster435.outtapocket.client.model.CheeseModel
import org.shadowmaster435.outtapocket.client.model.GearModel
import org.shadowmaster435.outtapocket.init.ModBlocks

object ModelLoader : ModelLoadingPlugin {
    override fun initialize(pluginContext: ModelLoadingPlugin.Context) {

        pluginContext.modifyBlockModelOnLoad().register(
            (fun(original: BlockStateModel.UnbakedGrouped, ctx: ModelModifier.OnLoadBlock.Context): BlockStateModel.UnbakedGrouped? {
                return when(ctx.state().block) {
                    ModBlocks.GEAR -> BlockStateModel.CachedUnbaked(GearModel())
                    ModBlocks.CHEESE -> BlockStateModel.CachedUnbaked(CheeseModel())
                    else -> original
                }
            })
        )
    }


}