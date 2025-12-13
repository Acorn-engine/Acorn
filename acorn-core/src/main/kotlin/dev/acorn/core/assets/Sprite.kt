package dev.acorn.core.assets

import dev.acorn.core.math.Color

data class Sprite(
    val texture: TextureHandle,
    var tint: Color = Color.WHITE
)