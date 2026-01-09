package dev.acorn.sample

import dev.acorn.core.app.AcornGame
import dev.acorn.core.components.SpriteShapeRenderer
import dev.acorn.core.input.Keys
import dev.acorn.core.math.Vec2
import dev.acorn.core.math.normalized
import dev.acorn.core.math.plusAssign
import dev.acorn.core.math.times
import dev.acorn.core.physics.BodyType
import dev.acorn.core.physics.collider.BoxCollider
import dev.acorn.core.physics.event.CollisionEnter
import dev.acorn.core.scene.GameObject
import dev.acorn.core.scene.Transform
import dev.acorn.core.scene.spriteObject

class TestGame : AcornGame() {
    private lateinit var player: GameObject
    private var moveSpeed = 250f

    override fun onStart() {
        val playerSprite = sprite("67.jpeg")
        player = scene.spriteObject(playerSprite, center(), Vec2(128f, 128f)).apply {
            addComponent(BoxCollider())
        }

        spawn(Transform(position = Vec2(center().x + 200f, center().y), scale = Vec2(150f, 300f))).apply {
            addComponent(SpriteShapeRenderer())
            addComponent(BoxCollider().apply { bodyType = BodyType.Static })
        }

        scene.collisions().events.subscribe { e ->
            if(e is CollisionEnter && e.selfObject == player) {
                println("Player hit ${e.otherObject.id}")
            }
        }

        time.every(0.25f) {
            println("tick @ ${time.sinceStartSeconds}")
        }
    }

    override fun onUpdate(dt: Float) {
        player.transform.rotationDeg = (time.sinceStartSeconds.toFloat() * 40f)

        val dir = input.axis2D(Keys.A, Keys.D, Keys.S, Keys.W).normalized()
        player.transform.position += dir * (moveSpeed * dt)
    }
}