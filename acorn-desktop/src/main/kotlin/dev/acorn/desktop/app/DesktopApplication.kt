package dev.acorn.desktop.app

import dev.acorn.core.Acorn
import dev.acorn.core.content.WindowConfig
import dev.acorn.desktop.gl.texture.DesktopTextureService
import dev.acorn.desktop.render.DesktopRenderer
import dev.acorn.desktop.window.GlfwWindow
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

object DesktopApplication {
    fun run(game: Acorn) {
        require(glfwInit()) { "Unable to initialize GLFW" }

        val config = WindowConfig()
        game.configureWindow(config)

        val window = GlfwWindow(config.width, config.height, config.title, config.fullscreen)
        GL.createCapabilities()

        glDisable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        val textures = DesktopTextureService()
        val context = DesktopGameContext(config, textures)
        val renderer = DesktopRenderer()

        game.setup(context)

        val win = IntArray(2)
        val fb = IntArray(2)
        var last = glfwGetTime()

        while(!window.shouldClose()) {
            val now = glfwGetTime()
            val dt = (now - last).toFloat()
            last = now

            window.pollEvents()
            window.windowSize(win)
            window.framebufferSize(fb)
            renderer.beginFrame(win[0], win[1], fb[0], fb[1])

            game.update(dt)
            game.render(renderer)

            window.swapBuffers()
        }

        window.destroy()
        glfwTerminate()
    }
}