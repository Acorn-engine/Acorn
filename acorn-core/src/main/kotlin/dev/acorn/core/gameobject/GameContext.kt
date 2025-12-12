package dev.acorn.core.gameobject

import dev.acorn.core.TextureHandle

data class WindowConfig(
    var width: Int = 1280,
    var height: Int = 720,
    var title: String = "Acorn Game",
    val fullscreen: Boolean = false
)

interface TextureService {
    fun load(path: String): TextureHandle
}

interface GameContext {
    val window: WindowConfig
    val textures: TextureService
}