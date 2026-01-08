package dev.acorn.core.scene

import dev.acorn.core.render.Renderer

/**
 * The core of acorn, a [dev.acorn.core.scene.GameObject] is something in the game. A [dev.acorn.core.scene.GameObject] contains [Component]'s
 *
 * @param transform The transform of the [dev.acorn.core.scene.GameObject]
 */
class GameObject(val transform: Transform = Transform()) {
    val id: Int = nextID()
    val components = mutableListOf<Component>()

    /**
     * Adds a [Component] onto a [dev.acorn.core.scene.GameObject]
     *
     * @param component The [Component] you're adding
     * @return The new [dev.acorn.core.scene.GameObject] with the [Component] applied
     */
    fun addComponent(component: Component): GameObject {
        component.gameObject = this
        components += component
        component.onAdded()
        return this
    }

    /**
     * Removes a [Component] from a [dev.acorn.core.scene.GameObject]
     *
     * @param component The [Component] you're removing
     * @return The new [dev.acorn.core.scene.GameObject] with the [Component] removed
     */
    fun removeComponent(component: Component) {
        if(components.remove(component)) {
            component.onRemoved()
        }
    }

    /**
     * Finds a [Component] from a [dev.acorn.core.scene.GameObject]
     */
    fun <T : Component> getComponent(type: Class<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return components.firstOrNull { type.isInstance(it) } as? T
    }

    /**
     * Finds all types of a [Component] on a [dev.acorn.core.scene.GameObject]
     */
    fun <T : Component> getComponents(type: Class<T>): List<T> {
        @Suppress("UNCHECKED_CAST")
        return components.filter { type.isInstance(it) } as List<T>
    }

    /**
     * Updates all the [Component]'s on the [dev.acorn.core.scene.GameObject]
     *
     * @param dt The delta seconds
     */
    fun update(dt: Float) {
        components.forEach { it.update(dt) }
    }

    /**
     * Renders all the [Component]'s on the [dev.acorn.core.scene.GameObject] onto the screen
     *
     * @param renderer The main renderer
     */
    fun render(renderer: Renderer) {
        components.forEach { it.render(renderer) }
    }

    private companion object {
        private var idCounter = 1
        private fun nextID(): Int = idCounter++
    }
}