package dev.acorn.core

import dev.acorn.core.content.GameContext
import dev.acorn.core.content.WindowConfig
import dev.acorn.core.render.Renderer

interface Acorn {
    fun configureWindow(config: WindowConfig)
    fun setup(context: GameContext)
    fun update(dtSeconds: Float)
    fun render(renderer: Renderer)
}