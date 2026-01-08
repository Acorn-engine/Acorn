package dev.acorn.core.components

import dev.acorn.core.math.Color
import dev.acorn.core.render.Renderable
import dev.acorn.core.render.Renderer
import dev.acorn.core.scene.Component

/**
 * Renders sprite shapes onto the [Renderer]
 *
 * @param shape The shape of the sprite
 * @param color The color of the sprite
 * @param circleSegments The circle segments of a sprite if the [ShapeType] is a circle
 */
class SpriteShapeRenderer(
    var shape: ShapeType = ShapeType.RECT,
    var color: Color = Color.WHITE,
    var circleSegments: Int = 32,
    override val layer: String = "Default",
    override val orderInLayer: Int = 0
) : Component(), Renderable {
    /**
     * Renders the sprite shape onto the [Renderer]
     */
    override fun render(renderer: Renderer) {
        when(shape) {
            ShapeType.RECT ->
                renderer.drawRect(gameObject.transform, color)

            ShapeType.CIRCLE ->
                renderer.drawCircle(gameObject.transform, color)
        }
    }
}