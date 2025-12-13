package dev.acorn.core.components

import dev.acorn.core.assets.Sprite
import dev.acorn.core.render.Renderer
import dev.acorn.core.render.SpriteMask
import dev.acorn.core.scene.Component

class SpriteRenderer(var sprite: Sprite): Component() {
    override fun render(renderer: Renderer) {
        val shape = gameObject.getComponent(SpriteShapeRenderer::class.java)
        val mask = if(shape?.shape == ShapeType.CIRCLE) SpriteMask.Circle else SpriteMask.None
        renderer.drawSprite(gameObject.transform, sprite, mask)
    }
}