package dev.acorn.core.math

import kotlin.math.sqrt

operator fun Vec2.plus(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)
operator fun Vec2.minus(other: Vec2): Vec2 = Vec2(x - other.x, y - other.y)
operator fun Vec2.times(s: Float): Vec2 = Vec2(x * s, y * s)

operator fun Vec2.plusAssign(other: Vec2) { x += other.x; y += other.y }
operator fun Vec2.minusAssign(other: Vec2) { x -= other.x; y -= other.y }
operator fun Vec2.timesAssign(s: Float) { x *= s; y *= s }

fun Vec2.length(): Float = sqrt(x * x + y * y)

fun Vec2.normalized(): Vec2 {
    val len = length()
    return if(len > 0f) Vec2(x / len, y / len) else Vec2(0f, 0f)
}