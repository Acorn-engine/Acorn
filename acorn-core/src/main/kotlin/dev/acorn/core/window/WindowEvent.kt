package dev.acorn.core.window

sealed interface WindowEvent

/**
 * Fired when the logical window size changes
 */
data class WindowResized(val width: Int, val height: Int) : WindowEvent

/**
 * Fired when the framebuffer size changes
 */
data class FramebufferResized(val width: Int, val height: Int) : WindowEvent

data class ViewportChanged(val viewport: Viewport) : WindowEvent