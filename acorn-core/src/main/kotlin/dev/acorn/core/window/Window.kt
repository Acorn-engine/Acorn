package dev.acorn.core.window

import dev.acorn.core.events.EventBus

/**
 * Runtime window state exposed go games
 *
 * - width/height are logical window units (points on macOS Retina)
 * - framebufferWidth/framebufferHeight are real pixel dimensions
 * - icon, set to a resource path in your game, example: "mylogo.png", this is not currently available on MacOS
 */
interface Window {
    val width: Int
    val height: Int

    val framebufferWidth: Int
    val framebufferHeight: Int

    val virtualWidth: Int
    val virtualHeight: Int

    val viewport: Viewport

    val events: EventBus<WindowEvent>

    var icon: String?
}