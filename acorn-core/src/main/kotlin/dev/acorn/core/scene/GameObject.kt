package dev.acorn.core.scene

import dev.acorn.core.render.Renderer

class GameObject(val transform: Transform = Transform()) {
    private val components = mutableListOf<Component>()

    fun addComponent(component: Component): GameObject {
        component.gameObject = this
        components += component
        component.onAdded()
        return this
    }

    fun removeComponent(component: Component) {
        if(components.remove(component)) {
            component.onRemoved()
        }
    }

    fun <T : Component> getComponent(type: Class<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return components.firstOrNull { type.isInstance(it) } as? T
    }

    fun update(dt: Float) {
        components.forEach { it.update(dt) }
    }

    fun render(renderer: Renderer) {
        components.forEach { it.render(renderer) }
    }
}