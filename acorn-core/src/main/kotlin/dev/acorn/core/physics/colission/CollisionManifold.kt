package dev.acorn.core.physics.colission

import dev.acorn.core.math.Vec2

data class CollisionManifold(
    val normal: Vec2,
    val penetration: Float
)