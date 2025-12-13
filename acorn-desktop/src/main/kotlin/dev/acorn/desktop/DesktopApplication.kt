package dev.acorn.desktop

import dev.acorn.core.Acorn
import dev.acorn.core.content.WindowConfig
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

object DesktopApplication {
    fun run(game: Acorn) {
        if(!glfwInit()) {
            throw IllegalStateException("Failed to initialize GLFW")
        }

        val windowConfig = WindowConfig()
        game.configureWindow(windowConfig)

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

        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glOrtho(
            0.0, windowConfig.width.toDouble(),
            0.0, windowConfig.height.toDouble(),
            -1.0, 1.0
        )

        glMatrixMode(GL_MODELVIEW)
        glLoadIdentity()

        glDisable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        val textureService = DesktopTextureService()
        val context = DesktopGameContext(windowConfig, textureService)
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