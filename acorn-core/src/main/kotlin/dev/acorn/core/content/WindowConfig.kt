package dev.acorn.core.content

/**
 * The configuration for the main game window
 *
 * @param width The width of the window
 * @param height The height of the window
 * @param title The title bar of the window
 * @param fullscreen Whether the application is fullscreen by default
*/
data class WindowConfig(
    var width: Int = 1280,
    var height: Int = 720,
    var title: String = "Acorn Game",
    val fullscreen: Boolean = false
)