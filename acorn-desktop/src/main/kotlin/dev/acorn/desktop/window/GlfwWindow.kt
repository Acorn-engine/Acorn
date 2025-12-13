package dev.acorn.desktop.window

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil.NULL

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

    fun shouldClose(): Boolean = glfwWindowShouldClose(handle)
    fun pollEvents() = glfwPollEvents()
    fun swapBuffers() = glfwSwapBuffers(handle)

    fun framebufferSize(out: IntArray) {
        val w = IntArray(1)
        val h = IntArray(1)
        glfwGetFramebufferSize(handle, w, h)
        out[0] = w[0]
        out[1] = h[0]
    }

    fun destroy() = glfwDestroyWindow(handle)
}