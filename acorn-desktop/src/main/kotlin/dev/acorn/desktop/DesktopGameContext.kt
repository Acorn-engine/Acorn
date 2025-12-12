package dev.acorn.desktop

import dev.acorn.core.gameobject.GameContext
import dev.acorn.core.gameobject.TextureService
import dev.acorn.core.gameobject.WindowConfig

class DesktopGameContext(
    override val window: WindowConfig,
    override val textures: TextureService
) : GameContext