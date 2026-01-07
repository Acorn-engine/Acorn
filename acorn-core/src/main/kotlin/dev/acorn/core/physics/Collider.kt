package dev.acorn.core.physics

import dev.acorn.core.events.EventBus
import dev.acorn.core.math.Vec2
import dev.acorn.core.scene.Component
import dev.acorn.core.scene.GameObject

abstract class Collider : Component() {
    val id: Int = nextID()

    var enabled: Boolean = true
    var isTrigger: Boolean = false
    var bodyType: BodyType = BodyType.Kinematic

    var offset: Vec2 = Vec2(0f, 0f)

    val events: EventBus<CollisionEvent> = EventBus()

    internal fun worldCenter(go: GameObject): Vec2 =
        Vec2(go.transform.position.x + offset.x, go.transform.position.y + offset.y)

    private companion object {
        private var idCounter = 1
        private fun nextID(): Int = idCounter++
    }
}