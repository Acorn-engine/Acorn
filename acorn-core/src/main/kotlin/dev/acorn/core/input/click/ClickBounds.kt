package dev.acorn.core.input.click

import dev.acorn.core.math.Vec2
import dev.acorn.core.physics.collider.BoxCollider
import dev.acorn.core.physics.collider.CircleCollider
import dev.acorn.core.physics.collider.Collider
import dev.acorn.core.scene.GameObject
import kotlin.math.min

interface ClickBounds {
    fun containsPoint(point: Vec2, gameObject: GameObject): Boolean
}

class BoxClickBounds(
    var size: Vec2 = Vec2(0f, 0f),
    var offset: Vec2 = Vec2(0f, 0f)
) : ClickBounds {
    override fun containsPoint(point: Vec2, gameObject: GameObject): Boolean {
        val center = Vec2(
            gameObject.transform.position.x + offset.x,
            gameObject.transform.position.y + offset.y
        )
        val halfW = if (size.x > 0f) size.x * 0.5f else gameObject.transform.scale.x * 0.5f
        val halfH = if (size.y > 0f) size.y * 0.5f else gameObject.transform.scale.y * 0.5f

        return point.x >= center.x - halfW && point.x <= center.x + halfW &&
                point.y >= center.y - halfH && point.y <= center.y + halfH
    }
}

class CircleClickBounds(
    var radius: Float = 0f,
    var offset: Vec2 = Vec2(0f, 0f)
) : ClickBounds {
    override fun containsPoint(point: Vec2, gameObject: GameObject): Boolean {
        val center = Vec2(
            gameObject.transform.position.x + offset.x,
            gameObject.transform.position.y + offset.y
        )
        val r = if (radius > 0f) radius else min(gameObject.transform.scale.x, gameObject.transform.scale.y) * 0.5f

        val dx = point.x - center.x
        val dy = point.y - center.y
        return dx * dx + dy * dy <= r * r
    }
}

class ColliderClickBounds : ClickBounds {
    override fun containsPoint(point: Vec2, gameObject: GameObject): Boolean {
        val collider = gameObject.getComponent(Collider::class.java)
            ?: return BoxClickBounds().containsPoint(point, gameObject)

        return when (collider) {
            is BoxCollider -> {
                val center = collider.worldCenter(gameObject)
                val half = collider.halfExtents()
                point.x >= center.x - half.x && point.x <= center.x + half.x &&
                        point.y >= center.y - half.y && point.y <= center.y + half.y
            }
            is CircleCollider -> {
                val center = collider.worldCenter(gameObject)
                val r = collider.effectiveRadius()
                val dx = point.x - center.x
                val dy = point.y - center.y
                dx * dx + dy * dy <= r * r
            }
            else -> BoxClickBounds().containsPoint(point, gameObject)
        }
    }
}
