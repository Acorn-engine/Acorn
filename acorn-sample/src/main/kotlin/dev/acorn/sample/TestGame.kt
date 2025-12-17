package dev.acorn.sample

import dev.acorn.core.Acorn
import dev.acorn.core.assets.Sprite
import dev.acorn.core.components.SpriteRenderer
import dev.acorn.core.content.GameContext
import dev.acorn.core.content.WindowConfig
import dev.acorn.core.math.Color
import dev.acorn.core.math.Vec2
import dev.acorn.core.render.Renderer
import dev.acorn.core.scene.GameObject
import dev.acorn.core.scene.Scene
import dev.acorn.core.scene.Transform

class TestGame : Acorn {
    private val scene = Scene()
    private var time = 0f

    private lateinit var player: GameObject

    override fun configureWindow(config: WindowConfig) {
        config.width = 1280
        config.height = 720
        config.title = "Acorn test game"
    }

    override fun setup(context: GameContext) {
        val playerTexture = context.textures.load("67.jpeg")
        val playerSprite = Sprite(playerTexture)

        player = scene.createGameObject(
            Transform(
                position = Vec2(640f, 360f),
                scale = Vec2(128f, 128f)
            )
        ).apply {
            addComponent(SpriteRenderer(playerSprite))
        }
    }

    override fun update(dtSeconds: Float) {
        time += dtSeconds
        player.transform.rotationDeg = time * 20f

        scene.update(dtSeconds)
    }

    override fun render(renderer: Renderer) {
        renderer.clear(Color.BLACK)
        scene.render(renderer)
    }
}