package dev.acorn.desktop

import dev.acorn.core.Acorn
import dev.acorn.core.WindowConfig
import org.lwjgl.glfw.GLFW.GLFW_FALSE
import org.lwjgl.glfw.GLFW.GLFW_RESIZABLE
import org.lwjgl.glfw.GLFW.GLFW_TRUE
import org.lwjgl.glfw.GLFW.GLFW_VISIBLE
import org.lwjgl.glfw.GLFW.glfwCreateWindow
import org.lwjgl.glfw.GLFW.glfwDefaultWindowHints
import org.lwjgl.glfw.GLFW.glfwDestroyWindow
import org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.glfw.GLFW.glfwShowWindow
import org.lwjgl.glfw.GLFW.glfwSwapBuffers
import org.lwjgl.glfw.GLFW.glfwSwapInterval
import org.lwjgl.glfw.GLFW.glfwTerminate
import org.lwjgl.glfw.GLFW.glfwWindowHint
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryUtil.NULL

object DesktopApplication {
    fun run(game: Acorn, windowConfig: WindowConfig = WindowConfig()) {
        if(!glfwInit()) {
            throw IllegalStateException("Failed to initialize GLFW")
        }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        val monitor = if(windowConfig.fullscreen) glfwGetPrimaryMonitor() else NULL
        val window = glfwCreateWindow(
            windowConfig.width,
            windowConfig.height,
            windowConfig.title,
            monitor,
            NULL
        )

        if(window == NULL) {
            glfwTerminate()
            throw IllegalStateException("Failed to create the GLFW window")
        }

        glfwMakeContextCurrent(window)
        glfwSwapInterval(1)
        glfwShowWindow(window)

        GL.createCapabilities()

        val context = DesktopGameContext(windowConfig)
        val renderer = DesktopRenderer()
        game.setup(context)

        var lastTime = glfwGetTime()
        while(!glfwWindowShouldClose(window)) {
            val now = glfwGetTime()
            val dt = (now - lastTime).toFloat()
            lastTime = now

            glfwPollEvents()

            game.update(dt)
            game.render(renderer)

            glfwSwapBuffers(window)
        }

        glfwDestroyWindow(window)
        glfwTerminate()
    }
}