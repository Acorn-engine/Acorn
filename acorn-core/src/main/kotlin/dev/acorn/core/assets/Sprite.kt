package dev.acorn.core.assets

import dev.acorn.core.math.Color

/**
 * Holds information about a [dev.acorn.core.assets.Sprite]
 *
 * @param texture The texture of the sprite, what the image actually is
 * @param tint The tint of the sprite, if you set this to red for example, a white square would appear red
 */
data class Sprite(
    val texture: TextureHandle,
    var tint: Color = Color.WHITE
)