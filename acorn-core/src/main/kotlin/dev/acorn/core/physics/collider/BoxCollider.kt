package dev.acorn.core.physics.collider

import dev.acorn.core.math.Vec2

class BoxCollider : Collider() {
    var size: Vec2 = Vec2(0f, 0f)

    fun halfExtents(): Vec2 {
        val w = if(size.x > 0f) size.x else gameObject.transform.scale.x
        val h = if(size.y > 0f) size.y else gameObject.transform.scale.y
        return Vec2(w * 0.5f, h * 0.5f)
    }
}