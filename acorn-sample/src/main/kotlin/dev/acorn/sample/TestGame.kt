package dev.acorn.sample

import dev.acorn.core.app.AcornGame
import dev.acorn.core.input.InputAction
import dev.acorn.core.input.Keys
import dev.acorn.core.input.onKey
import dev.acorn.core.math.Vec2
import dev.acorn.core.scene.GameObject
import dev.acorn.core.scene.spriteObject
import kotlin.math.sqrt

class TestGame : AcornGame() {
    private var time = 0f
    private lateinit var player: GameObject

    private val keysDown = HashSet<Int>()
    private var moveSpeed = 250f // units per second

    override fun onStart() {
        val playerSprite = sprite("67.jpeg")
        player = scene.spriteObject(playerSprite, center(), Vec2(128f, 128f))

        context.input.onKey { e ->
            when(e.action) {
                InputAction.Press -> {
                    keysDown += e.key

                    when(e.key) {
                        Keys.Q -> moveSpeed = (moveSpeed - 50f).coerceAtLeast(50f)
                        Keys.E -> moveSpeed = (moveSpeed + 50f).coerceAtMost(2000f)
                    }
                }

                InputAction.Release -> keysDown -= e.key
                InputAction.Repeat -> {
                    when(e.key) {
                        Keys.Q -> moveSpeed = (moveSpeed - 50f).coerceAtLeast(50f)
                        Keys.E -> moveSpeed = (moveSpeed + 50f).coerceAtMost(2000f)
                    }
                }
            }
        }
    }

    override fun onUpdate(dtSeconds: Float) {
        time += dtSeconds
        player.transform.rotationDeg = time * 20f

        val dir = Vec2()

        if(Keys.W in keysDown) dir.y += 1f
        if(Keys.S in keysDown) dir.y -= 1f
        if(Keys.D in keysDown) dir.x += 1f
        if(Keys.A in keysDown) dir.x -= 1f

        val len = sqrt(dir.x * dir.x + dir.y * dir.y)
        if(len > 0f) {
            dir.x /= len
            dir.y /= len

            player.transform.position.x += dir.x * moveSpeed * dtSeconds
            player.transform.position.y += dir.y * moveSpeed * dtSeconds
        }
    }
}