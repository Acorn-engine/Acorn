package dev.acorn.core.gameobject

import dev.acorn.core.Color
import dev.acorn.core.Renderer

enum class ShapeType {
    RECT,
    CIRCLE
}

class SpriteShapeRenderer(
    var shape: ShapeType = ShapeType.RECT,
    var color: Color = Color.WHITE,
    var circleSegments: Int = 32,
) : Component() {
    override fun render(renderer: Renderer) {
        when(shape) {
            ShapeType.RECT ->
                renderer.drawRect(gameObject.transform, color)

            ShapeType.CIRCLE ->
                renderer.drawCircle(gameObject.transform, color, circleSegments)
        }
    }
}