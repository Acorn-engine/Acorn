package dev.acorn.core.content

import dev.acorn.core.assets.TextureService

/**
 * Stores information about the game
 *
 * @property window The [WindowConfig]
 * @property textures All of the textures in the game
 */
interface GameContext {
    val window: WindowConfig
    val textures: TextureService
}