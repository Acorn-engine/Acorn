package dev.acorn.core

data class Transform(
    var position: Vec2 = Vec2(0f, 0f),
    var rotationDeg: Float = 0f,
    var scale: Vec2 = Vec2(1f, 1f)
)