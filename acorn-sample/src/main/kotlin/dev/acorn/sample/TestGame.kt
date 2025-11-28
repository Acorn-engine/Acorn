package dev.acorn.sample

import dev.acorn.core.Acorn
import dev.acorn.core.GameContext
import dev.acorn.core.Renderer
import dev.acorn.core.WindowConfig
import kotlin.math.sin

class TestGame : Acorn {
    private var hueTime = 0f

    override fun configureWindow(config: WindowConfig) {
        config.width = 1920
        config.height = 1080
        config.title = "Acorn test game"
    }

    override fun setup(context: GameContext) {
        println("TestGame started")
    }

    override fun update(dtSeconds: Float) {
        hueTime += dtSeconds
    }

    override fun render(renderer: Renderer) {
        val r = (0.5f + 0.5f * sin(hueTime)).coerceIn(0f, 1f)
        val g = (0.5f + 0.5f * sin(hueTime + 2f)).coerceIn(0f, 1f)
        val b = (0.5f + 0.5f * sin(hueTime + 4f)).coerceIn(0f, 1f)

        renderer.clear(r, g, b, 1f)
    }
}