package dev.acorn.core.components

import dev.acorn.core.assets.font.Font
import dev.acorn.core.math.Color
import dev.acorn.core.render.Renderable
import dev.acorn.core.render.Renderer
import dev.acorn.core.scene.Component
import dev.acorn.core.scene.Transform
import dev.acorn.core.text.TextAlign
import dev.acorn.core.text.TextScaleMode

/**
 * Renders text onto the [dev.acorn.core.render.Renderer]
 *
 * @param text The text string to display
 * @param font The font to use for rendering
 * @param color The color of the text
 * @param align Horizontal text alignment relative to the transform position
 * @param scaleMode How the text size responds to transform scale
 * @param layer The render layer for sorting
 * @param orderInLayer The order within the render layer
 */
class TextRenderer(
    var text: String,
    var font: Font,
    var color: Color = Color.WHITE,
    var align: TextAlign = TextAlign.LEFT,
    var scaleMode: TextScaleMode = TextScaleMode.FIXED,
    override val layer: String = "UI",
    override val orderInLayer: Int = 0
) : Component(), Renderable {
    override fun render(renderer: Renderer) {
        val transform = when(scaleMode) {
            TextScaleMode.FIXED -> Transform(
                gameObject.transform.position,
                gameObject.transform.rotationDeg,
                gameObject.transform.scale
            )

            TextScaleMode.SCALE_WITH_TRANSFORM -> gameObject.transform
        }

        renderer.drawText(text, transform, font, color, align)
    }
}