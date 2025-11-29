package dev.acorn.core

import dev.acorn.core.gameobject.GameContext
import dev.acorn.core.gameobject.WindowConfig

interface Acorn {
    fun configureWindow(config: WindowConfig)
    fun setup(context: GameContext)
    fun update(dtSeconds: Float)
    fun render(renderer: Renderer)
}