package org.shadowmaster435.outtapocket.client.init

import net.minecraft.client.render.entity.EntityRendererFactories
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import org.shadowmaster435.outtapocket.client.entity_renderer.FlailProjectileRenderer
import org.shadowmaster435.outtapocket.init.ModEntities

object ModEntityRenderers {

    fun init() {
        EntityRendererFactories.register(ModEntities.FLAIL, { entityRendererFactoryContext ->
            FlailProjectileRenderer(entityRendererFactoryContext)
        })
    }
}