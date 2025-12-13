package dev.acorn.core.render

import dev.acorn.core.Color
import dev.acorn.core.Sprite
import dev.acorn.core.gameobject.Transform

interface Renderer {
    fun clear(r: Float, g: Float, b: Float, a: Float)

    fun drawRect(transform: Transform, color: Color)
    fun drawCircle(transform: Transform, color: Color, segments: Int = 32)

    fun drawSprite(transform: Transform, sprite: Sprite, mask: SpriteMask)
}