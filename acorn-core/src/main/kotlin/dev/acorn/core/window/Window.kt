package dev.acorn.core.window

import dev.acorn.core.events.EventBus

/**
 * Runtime window state exposed go games
 *
 * - width/height are logical window units (points on macOS Retina)
 * - framebufferWidth/framebufferHeight are real pixel dimensions
 */
interface Window {
    val width: Int
    val height: Int

    val framebufferWidth: Int
    val framebufferHeight: Int

    val events: EventBus<WindowEvent>
}