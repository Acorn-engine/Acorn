package dev.acorn.core.scene

import dev.acorn.core.physics.colission.CollisionSystem
import dev.acorn.core.render.RenderLayers
import dev.acorn.core.render.Renderable
import dev.acorn.core.render.Renderer

/**
 * The scene contains all the objects in the game, think of this like a "level"
 */
class Scene {
    private val objects = mutableListOf<GameObject>()
    private val collisions = CollisionSystem()
    val layers = RenderLayers()

    /**
     * Creates a [GameObject] in the current [Scene]
     *
     * @param transform The [Transform] of the [GameObject] you're creating
     * @return The created [GameObject]
     */
    fun createGameObject(transform: Transform = Transform()): GameObject {
        val go = GameObject(transform)
        objects += go
        return go
    }

    /**
     * Should be called by your game in update() so that your [GameObject]'s can update
     * This plans to be redone in the future to reduce boilerplate
     *
     * @param dt The delta seconds
     */
    fun update(dt: Float) {
        objects.forEach { it.update(dt) }
        collisions.step(objects)
    }

    /**
     * Should be called by your game in render() so that your [GameObject]'s can render onto the screen
     * This plans to be redone in the future to reduce boilerplate
     *
     * @param renderer The game [Renderer]
     */
    fun render(renderer: Renderer) {
        val batch = ArrayList<RenderCall>(objects.size * 2)
        for(go in objects) {
            for(c in go.components) {
                if(c is Renderable) {
                    batch += RenderCall(go.id, layers.get(c.layer).index, c.orderInLayer, c as Component)
                }
            }
        }

        batch.sortWith(compareBy<RenderCall> { it.layerIndex }
            .thenBy { it.orderInLayer }
            .thenBy { it.goID }
        )

        for(call in batch) {
            call.component.render(renderer)
        }
    }

    fun collisions(): CollisionSystem = collisions

    private data class RenderCall(
        val goID: Int,
        val layerIndex: Int,
        val orderInLayer: Int,
        val component: Component
    )
}