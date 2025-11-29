package dev.acorn.core.gameobject

data class WindowConfig(
    var width: Int = 1280,
    var height: Int = 720,
    var title: String = "Acorn Game",
    val fullscreen: Boolean = false
)

interface GameContext {
    val window: WindowConfig
}