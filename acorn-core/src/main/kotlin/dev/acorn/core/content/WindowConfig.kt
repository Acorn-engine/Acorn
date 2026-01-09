package dev.acorn.core.content

enum class WindowScaleMode {
    Fit,
    Stretch
}

/**
 * The configuration for the main game window
 *
 * @param width The width of the window
 * @param height The height of the window
 * @param title The title bar of the window
 * @param fullscreen Whether the application is fullscreen by default
 * @param virtualWidth The virtual scaled/fit width of the screen
 * @param virtualHeight The virtual scaled/fit height of the screen
 * @param scaleMode The scale mode of the window (stretch/fit)
*/
data class WindowConfig(
    var width: Int = 1280,
    var height: Int = 720,
    var title: String = "Acorn Game",
    val fullscreen: Boolean = false,

    var virtualWidth: Int = 1280,
    var virtualHeight: Int = 720,

    var scaleMode: WindowScaleMode = WindowScaleMode.Fit,
)