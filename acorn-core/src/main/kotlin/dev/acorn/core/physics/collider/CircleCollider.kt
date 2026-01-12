package dev.acorn.core.physics.collider

import kotlin.math.min

class CircleCollider : Collider() {
    var radius: Float = 0f

    fun effectiveRadius(): Float {
        if(radius > 0f) return radius
        val s = gameObject.transform.scale
        return min(s.x, s.y) * 0.5f
    }
}