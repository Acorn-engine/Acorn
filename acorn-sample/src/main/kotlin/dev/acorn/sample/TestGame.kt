package dev.acorn.sample

import dev.acorn.core.app.AcornGame
import dev.acorn.core.math.Vec2
import dev.acorn.core.scene.GameObject
import dev.acorn.core.scene.spriteObject

class TestGame : AcornGame() {
    private var time = 0f
    private lateinit var player: GameObject

    override fun onStart() {
        val playerSprite = sprite("67.jpeg")
        player = scene.spriteObject(playerSprite, center(), Vec2(128f, 128f))
    }

    override fun onUpdate(dtSeconds: Float) {
        time += dtSeconds
        player.transform.rotationDeg = time * 20f
    }
}