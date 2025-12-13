package dev.acorn.desktop

import dev.acorn.core.assets.TextureService
import dev.acorn.core.content.GameContext
import dev.acorn.core.content.WindowConfig

class DesktopGameContext(
    override val window: WindowConfig,
    override val textures: TextureService
) : GameContext