package dev.acorn.core.content

import dev.acorn.core.assets.TextureService
import dev.acorn.core.debug.DebugDraw
import dev.acorn.core.input.Input
import dev.acorn.core.time.Time
import dev.acorn.core.window.Window

/**
 * Stores information about the game
 *
 * @property window The [WindowConfig]
 * @property textures All the textures in the game
 * @property input Any input from the keyboard or mouse
 * @property time The time service
 * @property debug Debug service
 */
interface GameContext {
    val window: Window
    val textures: TextureService
    val input: Input
    val time: Time
    val debug: DebugDraw
}