package dev.acorn.core.physics.event

import dev.acorn.core.math.Vec2
import dev.acorn.core.physics.collider.Collider
import dev.acorn.core.scene.GameObject


sealed class CollisionEvent {
    abstract val self: Collider
    abstract val other: Collider
    abstract val selfObject: GameObject
    abstract val otherObject: GameObject
    abstract val normal: Vec2
    abstract val penetration: Float
}

data class CollisionEnter(
    override val self: Collider,
    override val other: Collider,
    override val selfObject: GameObject,
    override val otherObject: GameObject,
    override val normal: Vec2,
    override val penetration: Float
) : CollisionEvent()

data class CollisionStay(
    override val self: Collider,
    override val other: Collider,
    override val selfObject: GameObject,
    override val otherObject: GameObject,
    override val normal: Vec2,
    override val penetration: Float
) : CollisionEvent()

data class CollisionExit(
    override val self: Collider,
    override val other: Collider,
    override val selfObject: GameObject,
    override val otherObject: GameObject,
    override val normal: Vec2 = Vec2(0f, 0f),
    override val penetration: Float = 0f
) : CollisionEvent()