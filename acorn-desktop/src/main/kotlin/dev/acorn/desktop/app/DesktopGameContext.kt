package dev.acorn.desktop.app

import dev.acorn.core.assets.TextureService
import dev.acorn.core.content.GameContext
import dev.acorn.core.input.Input
import dev.acorn.core.time.Time
import dev.acorn.core.window.Window

/**
 * The overwritten [GameContext] that handles desktop specific window and texture stuff
 */
class DesktopGameContext(
    override val window: Window,
    override val textures: TextureService,
    override val input: Input,
    override val time: Time
) : GameContext