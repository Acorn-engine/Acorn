package dev.acorn.desktop.debug

import dev.acorn.core.debug.DebugDraw
import dev.acorn.core.math.Color
import dev.acorn.core.math.Vec2
import dev.acorn.core.physics.collider.BoxCollider
import dev.acorn.core.physics.collider.CircleCollider
import dev.acorn.core.physics.collider.Collider
import dev.acorn.core.scene.GameObject

fun DebugDraw.bounds(go: GameObject, color: Color = Color.BLUE, seconds: Float = 0f) {
    rect(go.transform.position, go.transform.scale, color, seconds)
}

fun DebugDraw.collider(c: Collider, color: Color = Color.GREEN, seconds: Float = 0f) {
    val go = c.gameObject
    val center = Vec2(go.transform.position.x + c.offset.x, go.transform.position.y + c.offset.y)

    when(c) {
        is BoxCollider -> {
            val he = c.halfExtents()
            rect(center, Vec2(he.x * 2f, he.y * 2f), color, seconds)
        }
        is CircleCollider -> {
            circle(center, c.effectiveRadius(), 28, color, seconds)
        }

        else -> {}
    }
}