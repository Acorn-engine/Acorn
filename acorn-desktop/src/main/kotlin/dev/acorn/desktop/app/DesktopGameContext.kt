package dev.acorn.desktop.app

import dev.acorn.core.assets.TextureService
import dev.acorn.core.content.GameContext
import dev.acorn.core.content.WindowConfig

/**
 * The overwritten [GameContext] that handles desktop specific window and texture stuff
 */
class DesktopGameContext(
    override val window: WindowConfig,
    override val textures: TextureService
) : GameContext