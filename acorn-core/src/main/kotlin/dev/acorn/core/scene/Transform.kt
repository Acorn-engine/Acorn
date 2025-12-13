package dev.acorn.core.scene

import dev.acorn.core.math.Vec2

/**
 * Transform [Component], this stores the position, rotation, and scale of a [GameObject]
 *
 * @param position The position (in pixels) of a [GameObject]
 * @param rotationDeg The rotation in degrees (0-360) of a [GameObject]
 * @param scale The scale of a [GameObject]
 */
data class Transform(
    var position: Vec2 = Vec2(0f, 0f),
    var rotationDeg: Float = 0f,
    var scale: Vec2 = Vec2(1f, 1f)
)