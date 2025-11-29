package dev.acorn.core.gameobject

import dev.acorn.core.Renderer

class Scene {
    private val objects = mutableListOf<GameObject>()

    fun createGameObject(transform: Transform = Transform()): GameObject {
        val go = GameObject(transform)
        objects += go
        return go
    }

    fun update(dt: Float) {
        objects.forEach { it.update(dt) }
    }

    fun render(renderer: Renderer) {
        objects.forEach { it.render(renderer) }
    }
}