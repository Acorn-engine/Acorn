package dev.acorn.core

interface Renderer {
    fun clear(r: Float, g: Float, b: Float, a: Float)

    fun drawRect(transform: Transform, color: Color)
    fun drawCircle(transform: Transform, color: Color, segments: Int = 32)
}