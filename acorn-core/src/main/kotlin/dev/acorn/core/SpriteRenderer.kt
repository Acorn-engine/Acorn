package dev.acorn.core

import dev.acorn.core.gameobject.Component

class SpriteRenderer(var sprite: Sprite): Component() {
    override fun render(renderer: Renderer) {
        renderer.drawSprite(gameObject.transform, sprite)
    }
}