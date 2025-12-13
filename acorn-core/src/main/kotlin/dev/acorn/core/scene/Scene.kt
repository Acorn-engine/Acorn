package dev.acorn.core.scene

import dev.acorn.core.render.Renderer

/**
 * The scene contains all the objects in the game, think of this like a "level"
 */
class Scene {
    private val objects = mutableListOf<GameObject>()

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
    }

    /**
     * Should be called by your game in render() so that your [GameObject]'s can render onto the screen
     * This plans to be redone in the future to reduce boilerplate
     *
     * @param renderer The game [Renderer]
     */
    fun render(renderer: Renderer) {
        objects.forEach { it.render(renderer) }
    }
}