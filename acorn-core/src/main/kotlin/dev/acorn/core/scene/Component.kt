package dev.acorn.core.scene

import dev.acorn.core.render.Renderer

abstract class Component {
    lateinit var gameObject: GameObject
        internal set

    open fun onAdded() {}
    open fun onRemoved() {}
    open fun update(dt: Float) {}
    open fun render(renderer: Renderer) {}
}