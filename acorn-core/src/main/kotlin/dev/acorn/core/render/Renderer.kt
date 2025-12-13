package dev.acorn.core.render

import dev.acorn.core.assets.Sprite
import dev.acorn.core.math.Color
import dev.acorn.core.scene.Transform

interface Renderer {
    fun clear(r: Float, g: Float, b: Float, a: Float)

    fun drawRect(transform: Transform, color: Color)
    fun drawCircle(transform: Transform, color: Color, segments: Int = 32)

    fun drawSprite(transform: Transform, sprite: Sprite, mask: SpriteMask)
}