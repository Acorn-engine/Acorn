package dev.acorn.core.assets.font

import dev.acorn.core.math.Color

/**
 * Holds information about a font for text rendering
 *
 * @param handle The underlying font handle containing glyph data
 * @param color The color to render text with
 */
data class Font(
    val handle: FontHandle,
    var color: Color = Color.WHITE
)
