package dev.acorn.core.components

import dev.acorn.core.math.Color
import dev.acorn.core.render.Renderer
import dev.acorn.core.scene.Component

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