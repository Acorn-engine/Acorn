package dev.acorn.sample

import dev.acorn.core.app.AcornGame
import dev.acorn.core.input.Keys
import dev.acorn.core.math.Vec2
import dev.acorn.core.math.normalized
import dev.acorn.core.math.plusAssign
import dev.acorn.core.math.times
import dev.acorn.core.scene.GameObject
import dev.acorn.core.scene.spriteObject

class TestGame : AcornGame() {
    private var time = 0f
    private lateinit var player: GameObject

    private val keysDown = HashSet<Int>()
    private var moveSpeed = 250f

    override fun onStart() {
        val playerSprite = sprite("67.jpeg")
        player = scene.spriteObject(playerSprite, center(), Vec2(128f, 128f))
    }

    override fun onUpdate(dt: Float) {
        time += dt
        player.transform.rotationDeg = time * 20f

        if(input.pressed(Keys.Q)) moveSpeed = (moveSpeed - 50f).coerceAtLeast(50f)
        if(input.pressed(Keys.E)) moveSpeed = (moveSpeed + 50f).coerceAtMost(2000f)

        val dir = input.axis2D(Keys.A, Keys.D, Keys.S, Keys.W).normalized()
        player.transform.position += dir * (moveSpeed * dt)
    }
}