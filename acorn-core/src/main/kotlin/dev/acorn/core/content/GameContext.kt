package dev.acorn.core.content

import dev.acorn.core.assets.TextureService

interface GameContext {
    val window: WindowConfig
    val textures: TextureService
}