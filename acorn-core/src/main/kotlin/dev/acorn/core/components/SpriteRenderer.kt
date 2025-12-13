package dev.acorn.core.components

import dev.acorn.core.Sprite
import dev.acorn.core.gameobject.Component
import dev.acorn.core.gameobject.ShapeType
import dev.acorn.core.gameobject.SpriteShapeRenderer
import dev.acorn.core.render.Renderer
import dev.acorn.core.render.SpriteMask

class SpriteRenderer(var sprite: Sprite): Component() {
    override fun render(renderer: Renderer) {
        val shape = gameObject.getComponent(SpriteShapeRenderer::class.java)
        val mask = if(shape?.shape == ShapeType.CIRCLE) SpriteMask.Circle else SpriteMask.None
        renderer.drawSprite(gameObject.transform, sprite, mask)
    }
}