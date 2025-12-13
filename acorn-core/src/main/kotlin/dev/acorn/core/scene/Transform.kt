package dev.acorn.core.scene

import dev.acorn.core.math.Vec2

data class Transform(
    var position: Vec2 = Vec2(0f, 0f),
    var rotationDeg: Float = 0f,
    var scale: Vec2 = Vec2(1f, 1f)
)