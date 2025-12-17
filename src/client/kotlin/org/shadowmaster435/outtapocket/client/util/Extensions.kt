package org.shadowmaster435.outtapocket.client.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.player.PlayerEntity

val mcClient: MinecraftClient get() = MinecraftClient.getInstance()
val tDelta: Float get() = mcClient.renderTickCounter.getTickProgress(false)
val clPlayer: ClientPlayerEntity? get() =mcClient.player
