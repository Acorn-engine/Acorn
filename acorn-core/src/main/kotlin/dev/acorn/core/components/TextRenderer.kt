package dev.acorn.core.components

import dev.acorn.core.assets.Font
import dev.acorn.core.math.Color
import dev.acorn.core.render.Renderable
import dev.acorn.core.render.Renderer
import dev.acorn.core.render.TextAlign
import dev.acorn.core.scene.Component

class TextRenderer(
    var text: String,
    var font: Font,
    var color: Color = Color.WHITE,
    var align: TextAlign = TextAlign.LEFT,
    override val layer: String = "Default",
    override val orderInLayer: Int = 0
) : Component(), Renderable {
    override fun render(renderer: Renderer) {
        renderer.drawText(gameObject.transform, text, font, color, align)
    }
}
