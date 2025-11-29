package dev.acorn.sample

import dev.acorn.core.Acorn
import dev.acorn.core.Color
import dev.acorn.core.Renderer
import dev.acorn.core.Vec2
import dev.acorn.core.gameobject.*
import kotlin.math.sin

class TestGame : Acorn {
    private val scene = Scene()
    private var time = 0f

    private lateinit var rotatingSquare: GameObject
    private lateinit var movingCircle: GameObject

    override fun configureWindow(config: WindowConfig) {
        config.width = 1280
        config.height = 720
        config.title = "Acorn test game"
    }

    override fun setup(context: GameContext) {
        rotatingSquare = scene.createGameObject(Transform(
            position = Vec2(640f, 360f),
            scale = Vec2(100f, 100f)
        )).addComponent(SpriteShapeRenderer(
            shape = ShapeType.RECT,
            color = Color.fromRGB(0xFFAA00))
        )

        movingCircle = scene.createGameObject(Transform(
            position = Vec2(200f, 200f),
            scale = Vec2(80f, 80f)
        )).addComponent(SpriteShapeRenderer(
            shape = ShapeType.CIRCLE,
            color = Color.fromRGB(0x00CCFF))
        )
    }

    override fun update(dtSeconds: Float) {
        time += dtSeconds

        rotatingSquare.transform.rotationDeg = time * 60f
        movingCircle.transform.position.x = 640f + sin(time) * 200f

        scene.update(dtSeconds)
    }

    override fun render(renderer: Renderer) {
        renderer.clear(0.1f, 0.1f, 0.1f, 1f)
        scene.render(renderer)
    }
}