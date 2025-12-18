package dev.acorn.core.content

import dev.acorn.core.assets.TextureService
import dev.acorn.core.input.Input

/**
 * Stores information about the game
 *
 * @property window The [WindowConfig]
 * @property textures All the textures in the game
 * @property input Any input from the keyboard or mouse
 */
interface GameContext {
    val window: WindowConfig
    val textures: TextureService
    val input: Input
}