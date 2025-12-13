package dev.acorn.core.scene

import dev.acorn.core.render.Renderer

/**
 * Individual [Component]'s are added to [GameObject]'s
 *
 */
abstract class Component {
    lateinit var gameObject: GameObject
        internal set

    /**
     * What happens when the [Component] is added to the [GameObject]
     */
    open fun onAdded() {}

    /**
     * What happens when the [Component] is removed from the [GameObject]
     */
    open fun onRemoved() {}

    /**
     * What happens to the [Component] every frame
     */
    open fun update(dt: Float) {}

    /**
     * What happens when the [Component] renders to the screen
     */
    open fun render(renderer: Renderer) {}
}