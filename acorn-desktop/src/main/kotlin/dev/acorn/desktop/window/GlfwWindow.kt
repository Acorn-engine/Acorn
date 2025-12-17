package dev.acorn.desktop.window

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil.NULL

/**
 * Small wrapper around a GLFW window
 * This class creates the window and makes its OpenGL context current, provides commonly used operations (poll, swap, close check), and exposes window size (logical units) and framebuffer size (pixel units)
 *
 * HiDPI note:
 * - On Retina/HiDPI displays, framebuffer size can be larger than window size. Use framebuffer size for glViewport, and window size for projection/game coordinates
 */
class GlfwWindow(width: Int, height: Int, title: String, fullscreen: Boolean) {
    val handle: Long

    init {
        GlfwHints.apply()

        val monitor = if(fullscreen) glfwGetPrimaryMonitor() else NULL
        handle = glfwCreateWindow(width, height, title, monitor, NULL)
        require(handle != NULL) { "Failed to create GLFW window" }

        glfwMakeContextCurrent(handle)
        glfwSwapInterval(1)
        glfwShowWindow(handle)
    }

    /** @return true if the user requested the window to close */
    fun shouldClose(): Boolean = glfwWindowShouldClose(handle)
    /** Processes pending OS/window events (input, resize, close, etc.) */
    fun pollEvents() = glfwPollEvents()
    /** Swaps front/back buffers to present the rendered frame */
    fun swapBuffers() = glfwSwapBuffers(handle)

    /**
     * Writes the framebuffer size into [out] as [width, height]
     * This value should be used for glViewport
     */
    fun framebufferSize(out: IntArray) {
        val w = IntArray(1)
        val h = IntArray(1)
        glfwGetFramebufferSize(handle, w, h)
        out[0] = w[0]
        out[1] = h[0]
    }

    /**
     * Writes the window size into [out] as [width, height]
     * This value should be used for projection/game coordinates
     */
    fun windowSize(out: IntArray) {
        val w = IntArray(1)
        val h = IntArray(1)
        glfwGetWindowSize(handle, w, h)

        out[0] = w[0]
        out[1] = h[0]
    }

    /** Destroys the GLFW window, should be called during shutdown */
    fun destroy() = glfwDestroyWindow(handle)
}