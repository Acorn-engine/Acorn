package dev.acorn.core

interface Acorn {
    fun setup(context: GameContext)
    fun update(dtSeconds: Float)
    fun render(renderer: Renderer)
}