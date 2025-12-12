package dev.acorn.sample

import dev.acorn.core.*
import dev.acorn.core.gameobject.*

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
        renderer.clear(0.1f, 0.1f, 0.1f, 1f)
        scene.render(renderer)
    }
}