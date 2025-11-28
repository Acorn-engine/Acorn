package dev.acorn.core

interface Acorn {
    fun configureWindow(config: WindowConfig)
    fun setup(context: GameContext)
    fun update(dtSeconds: Float)
    fun render(renderer: Renderer)
}