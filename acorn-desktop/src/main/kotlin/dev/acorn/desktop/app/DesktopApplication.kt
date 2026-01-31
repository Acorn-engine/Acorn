package dev.acorn.desktop.app

import dev.acorn.core.Acorn
import dev.acorn.core.content.WindowConfig
import dev.acorn.core.time.MutableTime
import dev.acorn.desktop.debug.DesktopDebugDraw
import dev.acorn.desktop.gl.texture.DesktopTextureService
import dev.acorn.desktop.input.DesktopInput
import dev.acorn.desktop.render.DesktopRenderer
import dev.acorn.desktop.window.DesktopWindowState
import dev.acorn.desktop.window.GlfwWindow
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFW.glfwTerminate
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

/**
 * The main application class for the desktop platform
 * This class configures the windows and enables OpenGL, sets up textures, and handles the game loop
 */
object DesktopApplication {
    /**
     * Run the game
     *
     * @param game The acorn game to run
     */
    fun run(game: Acorn) {
        require(glfwInit()) { "Unable to initialize GLFW" }

        val config = WindowConfig().also { game.configureWindow(it) }
        if(config.virtualWidth <= 0) config.virtualWidth = config.width
        if(config.virtualHeight <= 0) config.virtualHeight = config.height

        game.configureWindow(config)

        val window = GlfwWindow(config.width, config.height, config.title, config.fullscreen)
        GL.createCapabilities()

        glDisable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        val textures = DesktopTextureService()
        val input = DesktopInput(window.handle).apply { install() }
        val time = MutableTime(0.12f, 0.05f)
        time.step(nowSeconds(), 0f)
        val debug = DesktopDebugDraw()

        val win = IntArray(2)
        val fb = IntArray(2)
        val windowState = DesktopWindowState(
            config.width,
            config.height,
            config.virtualWidth,
            config.virtualHeight,
            config.scaleMode,
            onIconChanged = { path -> window.setIcon(path) }
        )

        // Set the initial icon (default is "acorn.png")
        window.setIcon(windowState.icon)

        val context = DesktopGameContext(windowState, textures, input, time, debug)
        val renderer = DesktopRenderer()

        game.setup(context)

        var last = nowSeconds()

        while(!window.shouldClose()) {
            val now = nowSeconds()
            val rawDt = (now - last).toFloat()
            last = now

            input.beginFrame()
            time.step(now, rawDt)

            window.pollEvents()

            window.windowSize(win)
            window.framebufferSize(fb)

            windowState.update(win[0], win[1], fb[0], fb[1])
            renderer.beginFrame(windowState)

            debug.beginFrame(time.deltaSeconds)

            game.update(time.deltaSeconds)
            game.render(renderer)
            debug.render(renderer)
            renderer.endFrame()

            window.swapBuffers()
        }

        input.destroy()
        window.destroy()
        glfwTerminate()
    }

    private fun nowSeconds(): Double = System.nanoTime() / 1_000_000_000.0
}