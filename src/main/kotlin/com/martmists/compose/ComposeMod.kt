package com.martmists.compose

import net.fabricmc.api.ModInitializer
import kotlin.concurrent.thread

object ComposeMod : ModInitializer {
    @JvmStatic
    val EXECUTOR = AsyncTaskExecutor()

    override fun onInitialize() {
        thread(name = "Compose Task Executor Thread", isDaemon = true, start = true, block = EXECUTOR::process)
    }
}
